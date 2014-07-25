package mmbn.bn6;

import java.util.ArrayList;
import java.util.List;
import mmbn.*;
import rand.ByteStream;
import rand.RandomizerContext;
import rand.strat.*;

public class BN6RandomizerContext extends RandomizerContext {
    public BN6RandomizerContext(int seed) {
        super(seed);
    }
    
    @Override
    public void randomize(ByteStream rom) {
        setProgress((100 * 0) / 7);
        status("Processing chips...");
        ChipLibrary chipLibrary = randomizeChips(rom);
        
        setProgress((100 * 1) / 7);
        status("Processing folders...");
        randomizeFolders(rom, chipLibrary);
        
        setProgress((100 * 2) / 7);
        status("Processing shops...");
        randomizeShops(rom, chipLibrary);
        
        setProgress((100 * 3) / 7);
        status("Processing Mystery Data...");
        randomizeMysteryData(rom, chipLibrary);
        
        setProgress((100 * 4) / 7);
        status("Processing rewards...");
        randomizeRewards(rom, chipLibrary);
        
        setProgress((100 * 5) / 7);
        status("Processing traders...");
        randomizeTraders(rom, chipLibrary);
        
        setProgress((100 * 6) / 7);
        status("Processing battles...");
        randomizeBattles(rom);
        
        setProgress(100);
    }
    
    protected ChipLibrary randomizeChips(ByteStream rom) {
        ChipLibrary chipLibrary = new ChipLibrary();
        PALibrary paLibrary = new PALibrary();
        ChipProducer chipProducer
                = new BN6ChipProducer(chipLibrary);
        BN6ProgramAdvanceProducer paProducer
                = new BN6ProgramAdvanceProducer(paLibrary, chipLibrary);
        
        // Load all chips.
        ChipProvider chipProvider
                = new ChipProvider(this, chipProducer, paLibrary);
        RepeatStrategy chipRepeatStrat
                = new RepeatStrategy(chipProvider, 411);
        
        rom.setRealPosition(0x021AB0);
        rom.setPosition(rom.readInt32());
        chipRepeatStrat.execute(rom);
        
        // Get Program Advances.        
        LoaderStrategy paStrat
                = new LoaderStrategy(paProducer);
        PointerListStrategy paPtrStrat
                = new PointerListStrategy(paStrat, 1, false);
        RepeatStrategy paPtrListStrat
                = new RepeatStrategy(paPtrStrat, new byte[] { 0, 0, 0, 0 });
        PointerListStrategy paPtrListListStrat
                = new PointerListStrategy(paPtrListStrat, 2);
        
        rom.setRealPosition(0x0295B4);
        paPtrListListStrat.execute(rom);
        
        // Make sure the combo from the tutorial works.
        List<BattleChip> comboChips = new ArrayList<>(2);
        comboChips.add(chipLibrary.getElement(72));
        comboChips.add(chipLibrary.getElement(163));
        ProgramAdvance tutorialCombo = new ProgramAdvance(null, comboChips);
        paLibrary.addElement(-1, tutorialCombo);
        
        // Randomize chips.
        runProvider(chipProvider, rom);
        
        // Fix LinkNavi special chips.
        ItemProducer linkNaviChipProducer
                = new BN6RewardProducer(chipLibrary);
        ItemProvider linkNaviChipProvider
                = new ItemProvider(this, linkNaviChipProducer);
        RepeatStrategy linkNaviChipStrat
                = new RepeatStrategy(linkNaviChipProvider, 11);
        
        rom.setRealPosition(0x0280DC);
        rom.setPosition(rom.readInt32());
        linkNaviChipStrat.execute(rom);
        
        List<Item> linkNaviChips = linkNaviChipProvider.allData();
        for (Item linkNaviChip : linkNaviChips) {
            BattleChip chip = linkNaviChip.getChip();
            byte code = chip.getCodes()[0];
            linkNaviChip.setChipCode(chip, code);
        }
        linkNaviChipProvider.produce(rom);
        
        // Remove Library restriction gates.
        rom.setRealPosition(0x76218C);
        if (rom.readInt32() == 0x6434EF00) {
            rom.advance(-1);
            rom.writeUInt8((short)0);
        }
        rom.setRealPosition(0x762E2C);
        if (rom.readInt32() == 0xC8C80034) {
            rom.advance(-2);
            rom.writeUInt8((short)0);
        }
        
        return chipLibrary;
    }
    
    protected void randomizeFolders(ByteStream rom, ChipLibrary library) {
        // Randomize Folder
        ItemProducer chipProducer
                = new BN6RewardProducer(library);
        FolderProducer producer
                = new FolderProducer(chipProducer);
        FolderProvider provider
                = new FolderProvider(this, producer);
        PointerListStrategy processListStrat
                = new PointerListStrategy(provider, 6);
        
        rom.setRealPosition(0x137868);
        processListStrat.execute(rom);
        
        runProvider(provider, rom);
        
        // Get new starting Folder
        rom.setRealPosition(0x137868);
        rom.setPosition(rom.readInt32());
        Folder startFolder = producer.readFromStream(rom);
        List<Item> chips = startFolder.getChips();
        
        // Set a random chip to the CentralArea1 gate
        Item chipEntry = chips.remove(next(chips.size()));
        int chipIndex = chipEntry.getChip().index();
        rom.setRealPosition(0x75E6E4);
        if (rom.readInt32() == 0xAA010083) {
            rom.advance(-5);
            rom.writeUInt8((short)(chipIndex >> 8));
            rom.writeUInt8((short)(chipIndex & 0xFF));
        }
        rom.setRealPosition(0x75E6B8);
        if (rom.readInt32() == 0x832AEF00) {
            rom.advance(-1);
            rom.writeUInt8((short)(chipIndex & 0xFF));
            rom.writeUInt8((short)(chipIndex >> 8));
        }
        
        // Set another random chip to the SeasideArea1 gate
        chipEntry = chips.remove(next(chips.size()));
        chipIndex = chipEntry.getChip().index();
        rom.setRealPosition(0x75F0C0);
        if (rom.readInt32() == 0x03010018) {
            rom.advance(-4);
            rom.writeUInt8((short)(chipIndex & 0xFF));
            rom.writeUInt8((short)(chipIndex >> 8));
        }
        rom.setRealPosition(0x75F150);
        if (rom.readInt32() == 0xAA011800) {
            rom.advance(-4);
            rom.writeUInt8((short)(chipIndex >> 8));
            rom.writeUInt8((short)(chipIndex & 0xFF));
        }
        
        // Fix tutorial folders.
        FolderProvider tutorialProvider
                = new FolderProvider(this, producer);
        PointerListStrategy tutorialPtrStrat
                = new PointerListStrategy(tutorialProvider, 1);
        FilterStrategy filterEmptyStrat
                = new FilterStrategy(tutorialPtrStrat, new byte[] {
                    0, 0, 0, 0
                }, new byte[] {
                    -1, -1, -1, -1
                }, true);
        RepeatStrategy tutorialPtrArrayStrat
                = new RepeatStrategy(filterEmptyStrat, 12);
        rom.setRealPosition(0x00A4D8);
        rom.setPosition(rom.readInt32());
        tutorialPtrArrayStrat.execute(rom);
        byte[] codesWideSwrd = library.getElement(72).getCodes();
        byte[] codesAreaGrab = library.getElement(163).getCodes();
        List<Byte> possibleCodes = new ArrayList<>(4);
        for (byte a : codesWideSwrd) {
            for (byte b : codesAreaGrab) {
                if (a == b) {
                    possibleCodes.add(a);
                }
            }
        }
        byte comboCode = possibleCodes.get(next(possibleCodes.size()));
        for (Folder folder : tutorialProvider.folders()) {
            for (Item chip : folder.getChips()) {
                if (chip.getChip().index() == 72
                        || chip.getChip().index() == 163) {
                    chip.setChipCode(chip.getChip(), comboCode);
                } else {
                    byte[] codes = chip.getChip().getCodes();
                    byte code = codes[next(codes.length)];
                    chip.setChipCode(chip.getChip(), code);
                }
            }
            tutorialProvider.produce(rom);
        }
    }
    
    protected void randomizeBattles(ByteStream rom) {
        BN6BattleProducer producer
                = new BN6BattleProducer();
        BN6BattleProvider provider
                = new BN6BattleProvider(this, producer);
        RepeatStrategy repeatStrat
                = new RepeatStrategy(provider, new byte[] { -1 });
        PointerListStrategy processListStrat
                = new PointerListStrategy(repeatStrat, 16);
        PointerListStrategy processListListStrat
                = new PointerListStrategy(processListStrat, 21);
        PointerListStrategy processListListListStrat
                = new PointerListStrategy(processListListStrat, 8);
        
        rom.setRealPosition(0x020170);
        processListListListStrat.execute(rom);
        
        runProvider(provider, rom);
    }
    
    protected void randomizeRewards(ByteStream rom, ChipLibrary library) {
        // Randomize enemy drops.
        ItemProducer producer
                = new BN6RewardProducer(library);
        ItemProvider provider
                = new ItemProvider(this, producer);
        RepeatStrategy dropRepeatStrat
                = new RepeatStrategy(provider, 802 * 2 * 5);
        
        rom.setRealPosition(0x0AAEA8);
        dropRepeatStrat.execute(rom);
        
        // Randomize battle Mystery Data
        RepeatStrategy mdRepeatStrat
                = new RepeatStrategy(provider, 8 * 8);
        
        rom.setRealPosition(0x0211A0);
        mdRepeatStrat.execute(rom);
        
        runProvider(provider, rom);
    }
    
    protected void randomizeMysteryData(ByteStream rom, ChipLibrary library) {
        // Randomize Green, Blue and Purple Mystery Data
        ItemProducer producer
                = new BN6MysteryDataContentsProducer(library);
        ItemProvider provider
                = new ItemProvider(this, producer);
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
        
        runProvider(provider, rom);
    }
    
    protected void randomizeTraders(ByteStream rom, ChipLibrary library) {
        // Randomize chip traders.
        BN6ChipTraderProducer traderProducer
                = new BN6ChipTraderProducer(library);
        TraderProvider traderProvider
                = new TraderProvider(this, traderProducer, library);
        RepeatStrategy traderArrayStrat
                = new RepeatStrategy(traderProvider, 5);
        
        rom.setRealPosition(0x04C018);
        rom.setPosition(rom.readInt32());
        traderArrayStrat.execute(rom);
        
        runProvider(traderProvider, rom);
        
        // Randomize Number Trader.
        rom.setRealPosition(0x13D484);
        rom.setPosition(rom.readInt32());
        ItemProducer numberProducer
                = new BN6NumberCodeProducer(library, rom.readBytes(10));
        ItemProvider numberProvider
                = new ItemProvider(this, numberProducer);
        RepeatStrategy numberArrayStrat
                = new RepeatStrategy(numberProvider, new byte[] { -1 });
        
        rom.setRealPosition(0x13D480);
        rom.setPosition(rom.readInt32());
        numberArrayStrat.execute(rom);
        
        runProvider(numberProvider, rom);
    }
    
    protected void randomizeShops(ByteStream rom, ChipLibrary library) {
        ItemProducer producer
                = new BN6ShopItemProducer(library);
        ItemProvider provider
                = new ItemProvider(this, producer);
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
        
        runProvider(provider, rom);
    }
}
