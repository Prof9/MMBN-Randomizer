package rand;

import java.util.ArrayList;
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
                Bytes.convertInt32(0));
        // Load two pointers.
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
        chipProvider.randomize(new Random());
        chipProvider.produce(rom);
        
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
    
    public void randomizeFolders(ByteStream rom, ChipLibrary library) {        
        // Randomize Folder
        FolderProducer producer = new FolderProducer(
                new BN6RewardProducer(library));
        FolderProvider provider = new FolderProvider(producer);
        PointerListStrategy processListStrat
                = new PointerListStrategy(provider, 6);
        
        rom.setRealPosition(0x137868);
        processListStrat.execute(rom);
        
        provider.randomize(new Random());
        provider.produce(rom);
        
        // Get new starting Folder
        rom.setRealPosition(0x137868);
        rom.setPosition(rom.readInt32());
        Folder startFolder = producer.readFromStream(rom);
        List<Item> chips = startFolder.getChips();
        
        // Set a random chip to the CentralArea1 gate
        Item chipEntry = chips.remove(new Random().nextInt(chips.size()));
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
        chipEntry = chips.remove(new Random().nextInt(chips.size()));
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
                = new FolderProvider(producer);
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
        byte comboCode = possibleCodes.get(new Random().nextInt(
                possibleCodes.size()));
        for (Folder folder : tutorialProvider.folders()) {
            for (Item chip : folder.getChips()) {
                if (chip.getChip().index() == 72
                        || chip.getChip().index() == 163) {
                    chip.setChipCode(chip.getChip(), comboCode);
                } else {
                    byte[] codes = chip.getChip().getCodes();
                    byte code = codes[new Random().nextInt(codes.length)];
                    chip.setChipCode(chip.getChip(), code);
                }
            }
            tutorialProvider.produce(rom);
        }
    }
    
    public void randomizeBattles(ByteStream rom) {
        BN6BattleProducer producer = new BN6BattleProducer();
        BN6BattleProvider provider = new BN6BattleProvider(producer);
        RepeatStrategy repeatStrat = new RepeatStrategy(provider,
                Bytes.convertUInt8((short)0xFF));
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
        ItemProducer producer = new BN6RewardProducer(library);
        ItemProvider provider = new ItemProvider(producer);
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
        ItemProducer producer
                = new BN6MysteryDataContentsProducer(library);
        ItemProvider provider
                = new ItemProvider(producer);
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
        // Randomize chip traders.
        BN6ChipTraderProducer traderProducer = new BN6ChipTraderProducer(library);
        TraderProvider traderProvider = new TraderProvider(traderProducer, library);
        RepeatStrategy traderArrayStrat = new RepeatStrategy(traderProvider, 5);
        
        rom.setRealPosition(0x04C018);
        rom.setPosition(rom.readInt32());
        traderArrayStrat.execute(rom);
        
        traderProvider.randomize(new Random());
        traderProvider.produce(rom);
        
        // Randomize Number Trader.
        rom.setRealPosition(0x13D484);
        rom.setPosition(rom.readInt32());
        ItemProducer numberProducer
                = new BN6NumberCodeProducer(library, rom.readBytes(10));
        ItemProvider numberProvider
                = new ItemProvider(numberProducer);
        RepeatStrategy numberArrayStrat
                = new RepeatStrategy(numberProvider, new byte[] { -1 });
        
        rom.setRealPosition(0x13D480);
        rom.setPosition(rom.readInt32());
        numberArrayStrat.execute(rom);
        
        numberProvider.randomize(new Random());
        numberProvider.produce(rom);
    }
    
    public void randomizeShops(ByteStream rom, ChipLibrary library) {
        ItemProducer producer
                = new BN6ShopItemProducer(library);
        ItemProvider provider
                = new ItemProvider(producer);
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
