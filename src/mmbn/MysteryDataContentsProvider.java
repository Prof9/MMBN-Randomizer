package mmbn;

import java.util.List;
import java.util.Random;
import rand.DataProducer;
import rand.DataProvider;

public class MysteryDataContentsProvider extends DataProvider<MysteryDataContents> {
    protected final ChipLibrary library;
    
    public MysteryDataContentsProvider(final DataProducer<MysteryDataContents> producer, ChipLibrary library) {
        super(producer);
        this.library = library;
    }
    
    @Override
    protected void randomizeData(Random rng, MysteryDataContents contents, int position) {
        switch (contents.getType()) {
            case BATTLECHIP:
                BattleChip chip = this.library.getElement(contents.getValue());
                // Choose a random new battle chip with the same library and
                // rarity.
                List<BattleChip> possibleChips =
                        this.library.query((byte)-1, chip.getRarity(),
                                chip.getRarity(), null, chip.getLibrary(),
                                0, 99);
                
                BattleChip newChip = possibleChips.get(rng.nextInt(
                        possibleChips.size()));
                contents.setValue(newChip.index());

                // Choose a random code.
                byte[] codes = newChip.getCodes();
                contents.setSubValue(codes[rng.nextInt(codes.length)]);
                break;
        }
    }
}
