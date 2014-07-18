package rand;

import rand.strat.LoaderStrategy;
import rand.strat.RepeatStrategy;
import rand.strat.PointerListStrategy;
import mmbn.TraderProvider;
import mmbn.RewardProvider;
import mmbn.FolderProvider;
import mmbn.ChipProvider;
import mmbn.bn6.BN6BattleProvider;
import mmbn.PALibrary;
import mmbn.ChipLibrary;
import java.util.Random;
import rand.lib.*;
import mmbn.bn6.BN6BattleProducer;
import mmbn.bn6.BN6ChipProducer;
import mmbn.bn6.BN6ProgramAdvanceProducer;
import mmbn.bn6.BN6RewardProducer;
import mmbn.ChipProducer;
import mmbn.bn6.BN6ChipTraderProducer;
import mmbn.FolderProducer;

public class RandomizerContext {    
    public RandomizerContext() {
    }
    
    public ChipLibrary randomizeChips(ByteStream rom) {
        ChipLibrary chipLibrary = new ChipLibrary();
        ChipProducer chipProducer = new BN6ChipProducer(chipLibrary);
        PALibrary paLibrary = new PALibrary();
        BN6ProgramAdvanceProducer paProducer = new BN6ProgramAdvanceProducer(chipLibrary);
        
        // Get chips.
        ChipProvider chipProvider = new ChipProvider(chipProducer, paLibrary);
        // Load all chips.
        RepeatStrategy chipRepeatStrat = new RepeatStrategy(chipProvider, 411);
        
        rom.setRealPosition(0x021AB0);
        rom.setPosition(rom.readInt32());
        chipRepeatStrat.execute(rom);
        
        // Get Program Advances.        
        LoaderStrategy paStrat = new LoaderStrategy(paProducer);
        // Load single PA pointer.
        PointerListStrategy paPtrStrat = new PointerListStrategy(paStrat, 1,
                false);
        // Load pointers until 0x00000000.
        RepeatStrategy paPtrListStrat = new RepeatStrategy(paPtrStrat,
                ByteConverter.convertInt32(0));
        // Load two pointers.
        PointerListStrategy paPtrListListStrat
                = new PointerListStrategy(paPtrListStrat, 2);
        
        rom.setRealPosition(0x0295B4);
        paPtrListListStrat.execute(rom);
        
        // Randomize chips.
        //chipProvider.randomize(new Random());
        //chipProvider.produce(rom);
        
        return chipLibrary;
    }
    
    public void randomizeFolders(ByteStream rom, ChipLibrary library) {
        FolderProducer producer = new FolderProducer(
                new BN6RewardProducer(library));
        FolderProvider provider = new FolderProvider(producer);
        PointerListStrategy processListStrat
                = new PointerListStrategy(provider, 2);
        
        rom.setRealPosition(0x0050E8);
        rom.setPosition(rom.readInt32());
        provider.execute(rom);
        
        rom.setRealPosition(0x137868);
        processListStrat.execute(rom);
        
        provider.randomize(new Random());
        provider.produce(rom);
    }
    
    public void randomizeBattles(ByteStream rom) {
        BN6BattleProducer producer = new BN6BattleProducer();
        BN6BattleProvider provider = new BN6BattleProvider(producer);
        RepeatStrategy repeatStrat = new RepeatStrategy(provider,
                ByteConverter.convertUInt8((short)0xFF));
        PointerListStrategy processListStrat
                = new PointerListStrategy(repeatStrat, 16);
        PointerListStrategy processListListStrat
                = new PointerListStrategy(processListStrat, 21);
        PointerListStrategy processListListListStrat
                = new PointerListStrategy(processListListStrat, 8);
        
        rom.setRealPosition(0x020170);
        processListListListStrat.execute(rom);
        
        provider.randomize(new Random());
        provider.produce(rom);
    }
    
    public void randomizeRewards(ByteStream rom, ChipLibrary library) {
        // Randomize enemy drops
        BN6RewardProducer producer = new BN6RewardProducer(library);
        RewardProvider provider = new RewardProvider(producer, library);
        RepeatStrategy dropRepeatStrat = new RepeatStrategy(provider,
                802 * 2 * 5);
        
        rom.setRealPosition(0x0AAEA8);
        dropRepeatStrat.execute(rom);
        
        // Randomize battle Mystery Data
        RepeatStrategy mdRepeatStrat = new RepeatStrategy(provider, 8 * 8);
        
        rom.setRealPosition(0x0211A0);
        mdRepeatStrat.execute(rom);
        
        provider.randomize(new Random());
        provider.produce(rom);
    }
    
    public void randomizeTraders(ByteStream rom, ChipLibrary library) {
        BN6ChipTraderProducer traderProducer = new BN6ChipTraderProducer(library);
        TraderProvider provider = new TraderProvider(traderProducer, library);
        RepeatStrategy repeatStrat = new RepeatStrategy(provider, 5);
        
        rom.setRealPosition(0x04C01C);
        repeatStrat.execute(rom);
        
        provider.randomize(new Random());
        provider.produce(rom);
    }
}
