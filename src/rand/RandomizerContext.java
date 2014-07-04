package rand;

import java.util.Random;
import rand.lib.*;
import rand.prov.*;
import rand.strat.*;

public class RandomizerContext {    
    public RandomizerContext() {
    }
    
    public ChipLibrary randomizeChips(Rom rom) {
        PALibrary paLibrary = new PALibrary();
        ChipLibrary chipLibrary = new ChipLibrary();
        
        // Get Program Advance.
        PALoaderStrategy paStrat = new PALoaderStrategy(paLibrary);
        // Load single PA pointer.
        PointerListStrategy paPtrStrat = new PointerListStrategy(paStrat, 1,
                false);
        // Load pointers until 0x00000000.
        RepeatStrategy paPtrListStrat = new RepeatStrategy(paPtrStrat,
                new byte[] { 0x00, 0x00, 0x00, 0x00 });
        // Load two pointers.
        PointerListStrategy paPtrListListStrat
                = new PointerListStrategy(paPtrListStrat, 2);
        
        rom.setPosition(0x0295B4);
        paPtrListListStrat.execute(rom);
        
        // Get chip.
        ChipProvider chipProvider = new ChipProvider(chipLibrary, paLibrary);
        // Load all chips.
        RepeatStrategy chipRepeatStrat = new RepeatStrategy(chipProvider, 411);
        
        rom.setPosition(0x021AB0);
        rom.setPosition(rom.readPtr());
        chipRepeatStrat.execute(rom);
        
        // Randomize chips.
        //chipProvider.randomize(new Random());
        //chipProvider.produce(rom);
        
        return chipLibrary;
    }
    
    public void randomizeFolders(Rom rom, ChipLibrary library) {
        FolderProvider provider = new FolderProvider(library);
        PointerListStrategy processListStrat
                = new PointerListStrategy(provider, 2);
        
        rom.setPosition(0x0050E8);
        rom.setPosition(rom.readPtr());
        provider.execute(rom);
        
        rom.setPosition(0x137868);
        processListStrat.execute(rom);
        
        provider.randomize(new Random());
        provider.produce(rom);
    }
    
    public void randomizeBattles(Rom rom) {
        BattleProvider provider = new BattleProvider();
        RepeatStrategy repeatStrat = new RepeatStrategy(provider,
                new byte[] { -1 });
        PointerListStrategy processListStrat
                = new PointerListStrategy(repeatStrat, 16);
        PointerListStrategy processListListStrat
                = new PointerListStrategy(processListStrat, 21);
        PointerListStrategy processListListListStrat
                = new PointerListStrategy(processListListStrat, 8);
        
        rom.setPosition(0x020170);
        processListListListStrat.execute(rom);
        
        provider.randomize(new Random());
        provider.produce(rom);
    }
    
    public void randomizeRewards(Rom rom, ChipLibrary library) {
        RewardProvider provider = new RewardProvider(library);
        RepeatStrategy repeatStrat = new RepeatStrategy(provider,
                802 * 2 * 5);
        
        rom.setPosition(0x0AAEA8);
        repeatStrat.execute(rom);
        
        provider.randomize(new Random());
        provider.produce(rom);
    }
    
    public void randomizeTraders(Rom rom, ChipLibrary library) {
        TraderProvider provider = new TraderProvider(library);
        RepeatStrategy repeatStrat = new RepeatStrategy(provider, 5);
        
        rom.setPosition(0x04C01C);
        repeatStrat.execute(rom);
        
        provider.randomize(new Random());
        provider.produce(rom);
    }
}
