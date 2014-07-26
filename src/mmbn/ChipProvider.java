package mmbn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import rand.DataProvider;
import rand.RandomizerContext;

/**
 * A provider that keeps track of chips from a ROM, randomizes them, then writes
 * them back to a ROM.
 */
public class ChipProvider extends DataProvider<BattleChip> {
    /** A library that keeps track of all Program Advances. */
    private final PALibrary paLibrary;
    
    /** Maps chip indices to their preset chip codes. */
    private Map<Integer, List<Byte>> presetCodes;
    
    /**
     * Constructs a new ChipProvider using the given chip producer that utilizes
     * the given Program Advance library.
     * 
     * @param context The randomizer context constructing this provider.
     * @param producer The chip producer to use.
     * @param paLibrary The Program Advance library to use.
     */
    public ChipProvider(RandomizerContext context, ChipProducer producer, PALibrary paLibrary) {
        super(context, producer);
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
     * @param chip The chip to randomize.
     * @param position The position of the chip to randomize.
     */
    @Override
    protected void randomizeData(Random rng, BattleChip chip, int position) {
        // Get the current codes of the chip.
        byte[] oldCodes = chip.getCodes();
        
        // Get the preset codes for this chip.
        List<Byte> codes = this.presetCodes.get(chip.index());
        if (codes == null) {
            codes = new ArrayList<>(0);
        }
        
        // If necessary, generate random codes, excluding the preset codes.
        if (codes.size() < oldCodes.length) {
            byte[] randomCodes = chip.generateRandomCodes(rng, oldCodes.length - codes.size(), codes);
            
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
    }
    
    private void fixPAs(Random rng) {
        // Get the PAs as two separate lists.
        List<ProgramAdvance> pas = this.paLibrary.elements();
        
        // Sort all Program Advances by type and chip counts.
        // Fix all consecutive PAs first.
        Collections.sort(pas, new Comparator<ProgramAdvance>() {
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
        List<BattleChip> processed = new ArrayList<>();
        
        // Fix all consecutive PAs first.
        for (ProgramAdvance pa : pas) {
            // If any chips in the PA have already been processed, skip it.
            boolean skip = false;
            for (BattleChip chip : pa.chips()) {
                if (processed.contains(chip)) {
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
            for (BattleChip chip : pa.chips()) {
                if (!processed.contains(chip)) {
                    processed.add(chip);
                }
            }
        }
    }
    
    protected void fixCodePA(ProgramAdvance pa, Random rng) {
        BattleChip chip = pa.chips().get(0);
        int chipCount = pa.chipCount();
        
        // Select a random starting code.
        int startingCode = rng.nextInt(26 - chipCount);
        
        // Preset the codes necessary to form this PA.
        List<Byte> codes = new ArrayList<>(chipCount);
        for (int i = 0; i < chipCount; i++) {
            codes.add((byte)(startingCode + i));
        }
        
        presetCodes(chip.index(), codes);
    }
    
    protected void fixMultiPA(ProgramAdvance pa, Random rng) {
        // Select a random PA code.
        byte code = (byte)rng.nextInt(26);
        List<Byte> codes = new ArrayList<>(1);
        codes.add(code);
        
        // Keep track of which components have been processed.
        List<BattleChip> processed = new ArrayList<>(pa.chipCount());
        
        // Preset the code for all PA components.
        for (BattleChip chip : pa.chips()) {
            presetCodes(chip.index(), codes);
            
            // Mark the component as processed.
            if (!processed.contains(chip)) {
                processed.add(chip);
            }
        }
    }
}