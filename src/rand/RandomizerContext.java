package rand;

import java.util.Random;
import rand.lib.*;
import rand.prov.*;
import rand.strat.*;

public class RandomizerContext {    
    public RandomizerContext() {
    }
    
    public ChipLibrary randomizeChips(ByteStream rom) {
        PALibrary paLibrary = new PALibrary();
        ChipLibrary chipLibrary = new ChipLibrary();
        
        // Get Program Advance.
        PALoaderStrategy paStrat = new PALoaderStrategy(paLibrary);
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
        
        // Get chip.
        ChipProvider chipProvider = new ChipProvider(chipLibrary, paLibrary);
        // Load all chips.
        RepeatStrategy chipRepeatStrat = new RepeatStrategy(chipProvider, 411);
        
        rom.setRealPosition(0x021AB0);
        rom.setPosition(rom.readInt32());
        chipRepeatStrat.execute(rom);
        
        // Randomize chips.
        //chipProvider.randomize(new Random());
        //chipProvider.produce(rom);
        
        return chipLibrary;
    }
    
    public void randomizeFolders(ByteStream rom, ChipLibrary library) {
        FolderProvider provider = new FolderProvider(library);
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
        BattleProvider provider = new BattleProvider();
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
        RewardProvider provider = new RewardProvider(library);
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
        TraderProvider provider = new TraderProvider(library);
        RepeatStrategy repeatStrat = new RepeatStrategy(provider, 5);
        
        rom.setRealPosition(0x04C01C);
        repeatStrat.execute(rom);
        
        provider.randomize(new Random());
        provider.produce(rom);
    }
}
