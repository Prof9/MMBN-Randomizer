package rand.prov;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import rand.ByteStream;
import rand.lib.BattleChip;
import rand.lib.ChipLibrary;

public class FolderProvider extends DataProvider {
    protected final class ChipCodePair implements Comparable<ChipCodePair> {
        private final int index;
        private final BattleChip chip;
        private final byte code;
        
        public ChipCodePair(int index, BattleChip chip, byte code) {
            this.index = index;
            this.chip = chip;
            this.code = code;
        }
        
        public int index() {
            return this.index;
        }
        
        public BattleChip chip() {
            return this.chip;
        }
        
        public byte code() {
            return this.code;
        }        

        @Override
        public int compareTo(ChipCodePair o) {
            int result = Integer.compare(this.index, o.index);
            if (result == 0) {
                result = Integer.compare(this.code, o.code);
            }
            
            return result;
        }        
    }
    
    private final ChipLibrary library;
    
    public FolderProvider(ChipLibrary library) {
        this.library = library;
    }
    
    protected ChipCodePair[] extractChips(DataEntry data) {
        byte[] bytes = data.getBytes();
        int chipCount = bytes.length / 2;
        ChipCodePair[] chips = new ChipCodePair[chipCount];
        
        // Load all chips.
        for (int i = 0; i < chipCount; i++) {
            // Separate chip and code values.
            int entry = (bytes[i * 2 + 0] & 0xFF)
                    + ((bytes[i * 2 + 1] << 8) & 0xFF);
            int chipIndex = entry & 0x1FF;
            byte code = (byte)(entry >> 9);
            
            BattleChip chip = this.library.getElement(chipIndex);
            chips[i] = new ChipCodePair(chipIndex, chip, code);
        }
        
        return chips;
    }
    
    protected void insertChips(DataEntry data, ChipCodePair[] chips) {
        byte[] bytes = data.getBytes();
        int chipCount = Math.max(chips.length, bytes.length / 2);
        
        // Sort the chips by ID.
        Arrays.sort(chips);
        
        // Insert all chips.
        for (int i = 0; i < chipCount; i++) {
            ChipCodePair pair = chips[i];
            int chipIndex = pair.index();
            int code = pair.code();
            
            // Merge chip and code values.
            int entry = (chipIndex + (code << 9)) & 0xFFFF;
            bytes[i * 2 + 0] = (byte)(entry & 0xFF);
            bytes[i * 2 + 1] = (byte)((entry >> 8) & 0xFF);
        }
        
        data.setBytes(bytes);
    }
    
    @Override
    protected void randomizeData(Random rng, int position, DataEntry data) {
        ChipCodePair[] folder = extractChips(data);
        ChipCodePair[] newFolder = new ChipCodePair[folder.length];
        
        // Set number of chips that should/can be added.
        int megaLeft = 0;
        int gigaLeft = 0;
        
        // Get some libraries.
        List<Integer> stdChips
                = this.library.query(BattleChip.Library.STANDARD);
        List<Integer> megaChips
                = this.library.query(BattleChip.Library.MEGA);
        List<Integer> gigaChips
                = this.library.query(BattleChip.Library.GIGA);
        
        // Merge the three libraries.
        List<Integer> allChips = new ArrayList<>();
        allChips.addAll(stdChips);
        if (megaLeft > 0) {
            allChips.addAll(megaChips);
        }
        if (gigaLeft > 0) {
            allChips.addAll(gigaChips);
        }
        
        // Add chips until the folder is full.
        int addedChips = 0;
        int folderSize = newFolder.length;
        while (addedChips < folderSize) {
            // Pick a random chip to add.
            int r = rng.nextInt(allChips.size());
            int chipIndex = allChips.get(r);
            BattleChip chip = this.library.getElement(chipIndex);
            
            // Get the max amount of this chip that can be added.
            int canAdd = 1;
            switch (chip.library()) {
                case STANDARD:
                    // Check MB to determine the maximum.
                    int mb = chip.getMB();
                    if (mb >= 50) {
                        canAdd = 1;
                    } else if (mb >= 40) {
                        canAdd = 2;
                    } else if (mb >= 30) {
                        canAdd = 3;
                    } else if (mb >= 20) {
                        canAdd = 4;
                    } else {
                        canAdd = 5;
                    }
                    break;
                case MEGA:
                    // Only add one, and decrement amount of Mega chips left.
                    canAdd = 1;
                    // Remove Mega chips if all have been added.
                    if (--megaLeft <= 0) {
                        allChips.removeAll(megaChips);
                    }
                    break;
                case GIGA:
                    // Only add one, and decrement amount of Giga chips left.
                    canAdd = 1;
                    // Remove Giga chips if all have been added.
                    if (--gigaLeft <= 0) {
                        allChips.removeAll(gigaChips);
                    }
                    break;
            }
            
            // Make sure not to exceed the folder limit.
            if (addedChips + canAdd > folderSize) {
                canAdd = folderSize - addedChips;
            }
            
            // Choose a random code.
            byte[] codes = chip.getCodes();
            byte code = codes[rng.nextInt(codes.length)];
            
            // Choose how many to add.
            int toAdd = canAdd;
            // Never add 5 chips.
            if (canAdd > 1) {
                toAdd--;
            }
            // If 4 will be added, choose two random codes instead of one.
            if (toAdd == 4) {
                toAdd = 2;
            }
            
            // Add the first chip codes to the folder.
            while (toAdd-- > 0) {
                newFolder[addedChips++] = new ChipCodePair(chipIndex, chip,
                        code);
            }
            
            // Choose the second code.
            if (canAdd == 5) {
                toAdd = 2;
                code = codes[rng.nextInt(codes.length)];
            }
            
            // Add the second chip codes to the folder.
            while (toAdd-- > 0) {
                newFolder[addedChips++] = new ChipCodePair(chipIndex, chip,
                        code);
            }
            
            // Remove the chip from the selectable chips, if it's still in.
            allChips.remove((Integer)chipIndex);
        }
        
        // Insert the new folder.
        insertChips(data, newFolder);
    }

    @Override
    public void execute(ByteStream stream) {
        System.out.println("Collected folder data at 0x"
                + String.format("%06X", stream.getRealPosition()));
        
        registerData(stream, 30 * 2);
    }
}
