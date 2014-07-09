package rand.prov;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import rand.ByteStream;
import rand.lib.BattleChip;
import rand.lib.ChipLibrary;
import rand.lib.PALibrary;
import rand.lib.ProgramAdvance;

/**
 * A provider that keeps track of chips from a ROM, randomizes them, then writes
 * them back to a ROM.
 */
public class ChipProvider extends DataProvider {
    private final static int AMOUNT_OF_CODES = 27;
    
    /** A library that keeps track of all chips. */
    private final ChipLibrary chipLibrary;
    /** A library that keeps track of all Program Advances. */
    private final PALibrary paLibrary;
    
    private Map<Integer, List<Byte>> presetCodes;
    
    /**
     * Constructs a new ChipProvider that uses the given chip library and
     * Program Advance library.
     * 
     * @param chipLibrary The chip library to use.
     * @param paLibrary The Program Advance library to use.
     */
    public ChipProvider(ChipLibrary chipLibrary,
            PALibrary paLibrary) {
        this.chipLibrary = chipLibrary;
        this.paLibrary = paLibrary;
    }
    
    protected void presetCodes(int chipIndex, Iterable<Byte> codes) {
        // Get the current list of preset codes, or make a one if there is none.
        List<Byte> preset = this.presetCodes.get(chipIndex);
        if (preset == null) {
            preset = new ArrayList<>();
        }
        
        // Add all the codes to the list, unless they are already in.
        for (byte c : codes) {
            if (!preset.contains(c)) {
                preset.add(c);
            }
        }
        
        // Sort the list and put it in the table.
        Collections.sort(preset);
        this.presetCodes.put(chipIndex, preset);
    }
    
    /**
     * Generates a given amount of random chip codes.
     * 
     * @param amount The amount of chip codes to generate.
     * @param rng The random number generator to use.
     * @param excluded The chip codes that must be excluded. Can be null.
     * @return A byte array of sorted random chip codes with the given size.
     */
    protected byte[] generateRandomCodes(int amount, Random rng,
            Iterable<Byte> excluded) {
        if (amount < 0 || amount > ChipProvider.AMOUNT_OF_CODES) {
            throw new IndexOutOfBoundsException("Code amount out of bounds.");
        }
            
        // Create list of codes A-Z.
        ArrayList<Byte> possible
                = new ArrayList<>(ChipProvider.AMOUNT_OF_CODES);
        for (int i = 0; i < ChipProvider.AMOUNT_OF_CODES; i++) {
            possible.add((byte)i);
        }
        // Remove excluded codes.
        if (excluded != null) {
            for (byte c : excluded) {
                possible.remove((Byte)c);
            }
        }
        
        // Pull out random codes.
        // Could use Collections.shuffle(), but that does not allow a Random
        // parameter.
        byte[] codes = new byte[amount];
        for (int i = 0; i < amount; i++) {
            int codeIndex = rng.nextInt(possible.size());
            codes[i] = possible.get(codeIndex);
            possible.remove(codeIndex);
        }
        
        // Sort and return.
        Arrays.sort(codes);
        return codes;
    }
    
    /**
     * Reads a chip from the given byte stream and registers it.
     * 
     * @param stream The byte stream to read from.
     */
    @Override
    public void execute(ByteStream stream) {
        System.out.println("Collected chip data at 0x"
                + String.format("%06X", stream.getRealPosition()));
        
        // Load the chip.        
        BattleChip chip = new BattleChip(stream);
        // Rewind to register as data.
        stream.advance(-chip.byteCount());
        // Save the chip in the library.
        this.chipLibrary.addElement(stream.getPosition(), chip);
        registerData(stream, chip.byteCount());
    }
    
    /**
     * Randomizes all registered chips with the given random number generator.
     * 
     * @param rng The random number generator to use.
     */
    @Override
    public void randomize(Random rng) {
        // Initialize a new PA code map before each randomization.
        this.presetCodes = new HashMap<>();
        // Guarantee that all PAs work.
        fixPAs(rng);
        
        super.randomize(rng);
    }
    
    /**
     * Randomizes a single chip.
     * 
     * @param rng The random number generator to use.
     * @param position The ROM pointer of the chip to be randomized.
     * @param data The data to randomize.
     */
    @Override
    protected void randomizeData(Random rng, int position, DataEntry data) {
        // Get index of chip.
        int index = this.chipLibrary.getIndexFromPointer(position);
        
        // Get the chip.
        BattleChip chip = this.chipLibrary.getElement(index);
        byte[] oldCodes = chip.getCodes();
        
        // Get the preset codes and determine how much codes are left to choose.
        List<Byte> codes = this.presetCodes.getOrDefault((Integer)index,
                new ArrayList<Byte>());
        
        // If necessary, generate random codes, excluding the preset codes.
        if (codes.size() < oldCodes.length) {
            byte[] randomCodes = generateRandomCodes(oldCodes.length
                    - codes.size(), rng, codes);
            
            // Append the random codes to the new code array.
            for (int i = 0; i < randomCodes.length; i++) {
                codes.add(randomCodes[i]);
            }
        }
        
        // Do not add more codes than previously existed.
        byte[] newCodes = new byte[oldCodes.length];
        for (int i = 0; i < newCodes.length; i++) {
            newCodes[i] = codes.get(i);
        }
        
        chip.setCodes(newCodes);
        data.setBytes(chip.toBytes());
    }
    
    private void fixPAs(Random rng) {
        // Get the PAs as two separate lists.
        List<ProgramAdvance> pas = this.paLibrary.elements();
        
        // Sort all Program Advances by type and chip counts.
        // Fix all consecutive PAs first.
        pas.sort(new Comparator<ProgramAdvance>() {
            @Override
            public int compare(ProgramAdvance a, ProgramAdvance b) {
                if (a.type() == b.type()) {
                    return Integer.compare(a.chipCount(), b.chipCount());
                } else {
                    if (a.type() == ProgramAdvance.Type.CONSECUTIVE) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        });
        
        // Keep track of all processed chips.
        List<Integer> processed = new ArrayList<>();
        
        // Fix all consecutive PAs first.
        for (ProgramAdvance pa : pas) {
            // If any chips in the PA have already been processed, skip it.
            boolean skip = false;
            for (int c : pa.chips()) {
                if (processed.contains(c)) {
                    skip = true;
                    break;
                }
            }
            if (skip) {
                continue;
            }
            
            // Fix the PA.
            switch (pa.type()) {
                case CONSECUTIVE:
                    fixCodePA(pa, rng);
                    break;
                case COMBINATION:
                    fixMultiPA(pa, rng);
                    break;
            }
            
            // Mark all components as processed.
            for (int c : pa.chips()) {
                if (!processed.contains(c)) {
                    processed.add(c);
                }
            }
        }
    }
    
    protected void fixCodePA(ProgramAdvance pa, Random rng) {
        int chipIndex = pa.chips()[0];
        int chipCount = pa.chipCount();
        
        // Select a random starting code.
        int startingCode = rng.nextInt(26 - chipCount);
        
        // Preset the codes necessary to form this PA.
        List<Byte> codes = new ArrayList<>(chipCount);
        for (int i = 0; i < chipCount; i++) {
            codes.add((byte)(startingCode + i));
        }
        
        presetCodes(chipIndex, codes);
    }
    
    protected void fixMultiPA(ProgramAdvance pa, Random rng) {
        // Select a random PA code.
        byte code = (byte)rng.nextInt(26);
        List<Byte> codes = new ArrayList<>(1);
        codes.add(code);
        
        // Keep track of which components have been processed.
        List<Integer> processed = new ArrayList<>(pa.chipCount());
        
        // Preset the code for all PA components.
        for (int chipIndex : pa.chips()) {
            presetCodes(chipIndex, codes);
            
            // Mark the component as processed.
            if (!processed.contains(chipIndex)) {
                processed.add(chipIndex);
            }
        }
    }
}