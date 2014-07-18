package mmbn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rand.DataProducer;
import rand.DataProvider;
import mmbn.ChipLibrary;
import mmbn.BattleChip;
import mmbn.ChipTrader;
import mmbn.ChipTraderEntry;

public class TraderProvider extends DataProvider<ChipTrader> {
    private final ChipLibrary library;
    
    public TraderProvider(DataProducer<ChipTrader> reader, ChipLibrary library) {
        super(reader);
        this.library = library;
    }
    
    @Override
    protected void randomizeData(Random rng, ChipTrader trader, int position) {
        // Get some libraries.
        List<BattleChip> stdChips = this.library.query(BattleChip.Library.STANDARD);
        List<BattleChip> megaChips = this.library.query(BattleChip.Library.MEGA);
        // Merge the libraries.
        List<BattleChip> allChips = new ArrayList<>();
        allChips.addAll(stdChips);
        allChips.addAll(megaChips);
        
        // Add chips until the trader is full.
        for (int i = 0; i < trader.size(); i++) {
            ChipTraderEntry entry = trader.getEntry(i);
            
            // Pick a random chip to add.
            int r = rng.nextInt(allChips.size());
            BattleChip chip = allChips.get(r);
            
            // Insert the chip.
            entry.setChip(chip);
            entry.setCodes(chip.getCodes());
            
            // Remove the chip from the selectable chips.
            allChips.remove(chip);
        }
    }
}
