package mmbn;

import java.util.List;
import java.util.Random;
import rand.DataProducer;
import rand.DataProvider;

public class NumberCodeProvider extends DataProvider<NumberCode> {
    protected final ChipLibrary library;
    
    public NumberCodeProvider(final DataProducer<NumberCode> producer, ChipLibrary library) {
        super(producer);
        this.library = library;
    }
    
    @Override
    protected void randomizeData(Random rng, NumberCode code, int position) {
        switch (code.getType()) {
            case BATTLECHIP:
                BattleChip chip = this.library.getElement(code.getValue());
                // Choose a random new battle chip with the same library and
                // rarity.
                List<BattleChip> possibleChips =
                        this.library.query((byte)-1, chip.getRarity(),
                                chip.getRarity(), null, chip.getLibrary(),
                                0, 99);
                
                BattleChip newChip = possibleChips.get(rng.nextInt(
                        possibleChips.size()));
                code.setValue(newChip.index());

                // Choose a random code.
                byte[] codes = newChip.getCodes();
                code.setSubValue(codes[rng.nextInt(codes.length)]);
                break;
        }
    }
}
