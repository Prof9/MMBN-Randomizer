package mmbn;

import java.util.List;
import java.util.Random;
import rand.DataProvider;
import rand.DataProducer;

public class RewardProvider extends DataProvider<Reward> {
    private final ChipLibrary library;
    
    public RewardProvider(final DataProducer<Reward> producer,
            final ChipLibrary library) {
        super(producer);
        this.library = library;
    }
    
    @Override
    protected void randomizeData(Random rng, Reward reward, int position) {        
        switch (reward.getType()) {
            case BATTLECHIP:
                BattleChip chip = reward.getChip();
                // Choose a random new battle chip with the same library and
                // rarity.
                List<BattleChip> possibleChips =
                        this.library.query((byte)-1, chip.getRarity(),
                                chip.getRarity(), null, chip.getLibrary(),
                                0, 99);

                BattleChip newChip = possibleChips.get(rng.nextInt(
                        possibleChips.size()));
                reward.setChip(newChip);

                // Choose a random code.
                byte[] codes = newChip.getCodes();
                reward.setCode(codes[rng.nextInt(codes.length)]);
                break;
        }
    }
}
