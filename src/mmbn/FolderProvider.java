package mmbn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rand.DataProvider;
import mmbn.ChipLibrary;
import mmbn.BattleChip;
import mmbn.Folder;
import mmbn.Reward;

public class FolderProvider extends DataProvider<Folder> {
    protected ChipLibrary library;
    
    public FolderProvider(FolderProducer producer) {
        super(producer);
        this.library = producer.library();
    }
    
    @Override
    protected void randomizeData(Random rng, Folder folder, int position) {
        folder.clear();
        
        // Set number of chips that should/can be added.
        int megaLeft = 0;
        int gigaLeft = 0;
        
        // Get some libraries.
        List<BattleChip> stdChips = this.library.query(BattleChip.Library.STANDARD);
        List<BattleChip> megaChips = this.library.query(BattleChip.Library.MEGA);
        List<BattleChip> gigaChips = this.library.query(BattleChip.Library.GIGA);
        
        // Merge the three libraries.
        List<BattleChip> allChips = new ArrayList<>();
        allChips.addAll(stdChips);
        if (megaLeft > 0) {
            allChips.addAll(megaChips);
        }
        if (gigaLeft > 0) {
            allChips.addAll(gigaChips);
        }
        
        // Add chips until the folder is full.
        while (!folder.isFull()) {
            // Pick a random chip to add.
            int r = rng.nextInt(allChips.size());
            BattleChip chip = allChips.get(r);
            
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
                folder.addChip(new Reward(chip, code));
            }
            
            // Choose the second code.
            if (canAdd == 5) {
                toAdd = 2;
                code = codes[rng.nextInt(codes.length)];
            }
            
            // Add the second chip codes to the folder.
            while (toAdd-- > 0) {
                folder.addChip(new Reward(chip, code));
            }
            
            // Remove the chip from the selectable chips, if it's still in.
            allChips.remove(chip);
        }
    }
}
