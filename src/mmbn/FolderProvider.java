package mmbn;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import rand.DataProvider;
import rand.RandomizerContext;

public class FolderProvider extends DataProvider<Folder> {
    protected ChipLibrary library;
    
    public FolderProvider(RandomizerContext context, FolderProducer producer) {
        super(context, producer);
        this.library = producer.chipLibrary();
    }
    
    @Override
    protected void randomizeData(Random rng, Folder folder, int position) {
        List<Item> oldEntries = folder.getChips();        
        folder.clear();
        
        // Set number of chips that should/can be added.
        int megaLeft = 5;
        int gigaLeft = 1;
        
        // Get some libraries.
        List<BattleChip> megaChips = this.library.query(BattleChip.Library.MEGA);
        List<BattleChip> gigaChips = this.library.query(BattleChip.Library.GIGA);
        
        // Add chips until the folder is full.
        while (!folder.isFull()) {
            // Pick a random chip from the old folder.
            int r = rng.nextInt(oldEntries.size());
            Item oldEntry = oldEntries.get(r);
            BattleChip oldChip = oldEntry.getChip();
            
            // Find all chips with the same library and rarity.
            List<BattleChip> possibleChips = this.library.query(
                    (byte)-1,
                    oldChip.getRarity(), oldChip.getRarity(),
                    null,
                    null,
                    0, 99
            );
            
            // Remove all chips already in the folder.
            for (Item entry : folder.getChips()) {
                possibleChips.remove(entry.getChip());
            }
            
            // Remove Mega and Giga chips, if no more can be added.
            if (megaLeft <= 0) {
                possibleChips.removeAll(megaChips);
            }
            if (gigaLeft <= 0) {
                possibleChips.removeAll(gigaChips);
            }
            
            // Pick a random chip to add.
            r = rng.nextInt(possibleChips.size());
            BattleChip chip = possibleChips.get(r);
            
            // Get the max amount of this chip that can be added.
            int canAdd = 1;
            switch (chip.getLibrary()) {
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
                    megaLeft--;
                    break;
                case GIGA:
                    // Only add one, and decrement amount of Giga chips left.
                    canAdd = 1;
                    gigaLeft--;
                    break;
            }
            
            // Make sure not to exceed the folder limit.
            if (folder.size() + canAdd > folder.maxSize()) {
                canAdd = folder.maxSize() - folder.size();
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
                Item item = new Item(new byte[2]);
                item.setChipCode(chip, code);
                folder.addChip(item);
            }
            
            // Choose the second code.
            if (canAdd == 5) {
                toAdd = 2;
                code = codes[rng.nextInt(codes.length)];
            }
            
            // Add the second chip codes to the folder.
            while (toAdd-- > 0) {
                Item item = new Item(new byte[2]);
                item.setChipCode(chip, code);
                folder.addChip(item);
            }
            
            // Remove chip from old folder.
            oldEntries.remove(oldEntry);
        }
        folder.sort();
        folder.setChips(folder.getChips());
    }
}
