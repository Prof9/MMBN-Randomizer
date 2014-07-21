package mmbn;

import java.util.List;
import java.util.Random;
import rand.DataProducer;
import rand.DataProvider;

public class ShopItemProvider extends DataProvider<ShopItem> {
    protected final ChipLibrary library;
    
    public ShopItemProvider(final DataProducer<ShopItem> producer, ChipLibrary library) {
        super(producer);
        this.library = library;
    }
    
    @Override
    protected void randomizeData(Random rng, ShopItem item, int position) {
        switch (item.getType()) {
            case BATTLECHIP:
                BattleChip chip = this.library.getElement(item.getValue());
                // Choose a random new battle chip with the same library and
                // rarity.
                List<BattleChip> possibleChips =
                        this.library.query((byte)-1, chip.getRarity(),
                                chip.getRarity(), null, chip.getLibrary(),
                                0, 99);
                
                BattleChip newChip = possibleChips.get(rng.nextInt(
                        possibleChips.size()));
                item.setValue(newChip.index());

                // Choose a random code.
                byte[] codes = newChip.getCodes();
                item.setSubValue(codes[rng.nextInt(codes.length)]);
                break;
        }
    }
    
}
