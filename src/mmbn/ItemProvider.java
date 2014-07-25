package mmbn;

import java.util.List;
import java.util.Random;
import rand.DataProvider;
import rand.RandomizerContext;

public class ItemProvider extends DataProvider<Item> {
    private final ChipLibrary chipLibrary;
    
    public ItemProvider(RandomizerContext context, ItemProducer producer) {
        super(context, producer);
        this.chipLibrary = producer.chipLibrary();
    }
    
    @Override
    protected void randomizeData(Random rng, Item item, int position) {
        if (item.isChip()) {
            BattleChip oldChip = item.getChip();
            
            // Choose a random new battle chip with the same library and rarity.
            List<BattleChip> possibleChips = this.chipLibrary.query(
                    (byte)-1,
                    oldChip.getRarity(), oldChip.getRarity(),
                    null,
                    oldChip.getLibrary(),
                    0, 99
            );
            int r = rng.nextInt(possibleChips.size());
            BattleChip newChip = possibleChips.get(r);
            
            // Choose a random code.
            byte[] possibleCodes = newChip.getCodes();
            r = rng.nextInt(possibleCodes.length);
            byte newCode = possibleCodes[r];
            
            if (item.type == Item.Type.BATTLECHIP_TRAP) {
                item.setChipCodeTrap(newChip, newCode);
            } else {
                item.setChipCode(newChip, newCode);
            }
        }
    }
}
