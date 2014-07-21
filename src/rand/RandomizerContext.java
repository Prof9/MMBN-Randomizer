package rand;

import java.util.List;
import java.util.Random;
import mmbn.*;
import mmbn.bn6.*;
import rand.strat.*;

public class RandomizerContext {        
    public ChipLibrary randomizeChips(ByteStream rom) {
        ChipLibrary chipLibrary = new ChipLibrary();
        ChipProducer chipProducer = new BN6ChipProducer(chipLibrary);
        PALibrary paLibrary = new PALibrary();
        BN6ProgramAdvanceProducer paProducer = new BN6ProgramAdvanceProducer(paLibrary, chipLibrary);
        
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
        // Randomize Folder
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
        
        // Get new starting Folder
        rom.setRealPosition(0x0050E8);
        rom.setPosition(rom.readInt32());
        Folder startFolder = producer.readFromStream(rom);
        List<Reward> chips = startFolder.getChips();
        
        // Set a random chip to the CentralArea1 gate
        Reward chipEntry = chips.remove(new Random().nextInt(chips.size()));
        int chipIndex = chipEntry.getChip().index();
        rom.setRealPosition(0x75E6E4);
        if (rom.readInt32() == 0xAA010083) {
            rom.advance(-4);
            rom.writeUInt8((short)(chipIndex & 0xFF));
            rom.writeUInt8((short)(chipIndex >> 8));
        }
        rom.setRealPosition(0x75E6B8);
        if (rom.readInt32() == 0x832AEF00) {
            rom.advance(-1);
            rom.writeUInt8((short)(chipIndex & 0xFF));
            rom.writeUInt8((short)(chipIndex >> 8));
        }
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
    
    public void randomizeMysteryData(ByteStream rom, ChipLibrary library) {
        // Randomize Green, Blue and Purple Mystery Data
        MysteryDataContentsProducer producer
                = new BN6MysteryDataContentsProducer(library);
        MysteryDataContentsProvider provider
                = new MysteryDataContentsProvider(producer, library);
        RepeatStrategy contentsArrayStrat
                = new RepeatStrategy(provider, new byte[] { 0 });
        PointerListStrategy contentsPtrStrat
                = new PointerListStrategy(contentsArrayStrat, 1);
        OffsetStrategy mysteryDataStrat
                = new OffsetStrategy(contentsPtrStrat, 8);
        RepeatStrategy mysteryDataArrayStrat
                = new RepeatStrategy(mysteryDataStrat, new byte[] { 0 });
        PointerListStrategy mysteryDataPtrStrat
                = new PointerListStrategy(mysteryDataArrayStrat, 1);
        RepeatStrategy subAreaArrayStrat
                = new RepeatStrategy(mysteryDataPtrStrat, new byte[] {
                    0, 0, 0, 0
                });
        PointerListStrategy subAreaPtrStrat
                = new PointerListStrategy(subAreaArrayStrat, 1);
        OffsetStrategy subAreaStrat
                = new OffsetStrategy(subAreaPtrStrat, 4);
        FilterStrategy emptyAreaFilter
                = new FilterStrategy(subAreaStrat, new byte[] {
                    0, 0, 0, 0, 0, 0, 0, 0
                }, new byte[] {
                    -1, -1, -1, -1, 0, 0, 0, 0
                }, true);
        RepeatStrategy areaArrayStrat
                = new RepeatStrategy(emptyAreaFilter, new byte[] {
                    1, 0, 0, 0
                });
        PointerListStrategy areasArrayStrat
                = new PointerListStrategy(areaArrayStrat, 2);
        
        rom.setRealPosition(0x09FE94);
        areasArrayStrat.execute(rom);
        
        // Randomize Gold Mystery Data
        OffsetStrategy goldAreaStrat
                = new OffsetStrategy(contentsPtrStrat, 4, 4);
        RepeatStrategy goldAreaArrayStrat
                = new RepeatStrategy(goldAreaStrat, new byte[] { 0, 0, 0, 0 });
        
        rom.setRealPosition(0x09FE90);
        rom.setPosition(rom.readInt32());
        goldAreaArrayStrat.execute(rom);
        
        provider.randomize(new Random());
        provider.produce(rom);
    }
    
    public void randomizeTraders(ByteStream rom, ChipLibrary library) {
        BN6ChipTraderProducer traderProducer = new BN6ChipTraderProducer(library);
        TraderProvider provider = new TraderProvider(traderProducer, library);
        RepeatStrategy repeatStrat = new RepeatStrategy(provider, 5);
        
        rom.setRealPosition(0x04C018);
        rom.setPosition(rom.readInt32());
        repeatStrat.execute(rom);
        
        provider.randomize(new Random());
        provider.produce(rom);
    }
    
    public void randomizeShops(ByteStream rom, ChipLibrary library) {
        DataProducer<ShopItem> producer
                = new BN6ShopItemProducer();
        ShopItemProvider provider
                = new ShopItemProvider(producer, library);
        RepeatStrategy itemArrayStrat
                = new RepeatStrategy(provider, new byte[] { 0 });
        
        rom.setRealPosition(0x047478);
        int itemPoolPtr = rom.readInt32();
        PointerListStrategy itemArrayPtrStrat
                = new PointerListStrategy(itemArrayStrat, 1, itemPoolPtr);
        
        
        OffsetStrategy shopEntryStrat
                = new OffsetStrategy(itemArrayPtrStrat, 8, 4);
        RepeatStrategy shopEntryArrayStrat
                = new RepeatStrategy(shopEntryStrat, 19);
        
        rom.setRealPosition(0x046CF0);
        rom.setPosition(rom.readInt32());
        shopEntryArrayStrat.execute(rom);
        
        rom.setRealPosition(0x048F94);
        rom.setPosition(rom.readInt32());
        itemArrayStrat.execute(rom);
        
        provider.randomize(new Random());
        provider.produce(rom);
    }
}
