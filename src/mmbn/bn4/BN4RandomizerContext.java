package mmbn.bn4;

import java.util.ArrayList;
import java.util.List;
import mmbn.ChipLibrary;
import mmbn.PALibrary;
import mmbn.multi.*;
import mmbn.prod.*;
import mmbn.prov.*;
import mmbn.types.*;
import rand.ByteStream;
import rand.RandomizerContext;
import rand.strat.*;

public class BN4RandomizerContext extends RandomizerContext {
		private static final int[][] offsets = new int[][]{
		// Order: Red Sun J, Blue Moon J, Red Sun U, Red Sun E, Blue Moon U,
		//        Blue Moon E, Red Sun J Rev1, Blue Moon J Rev1
		new int[] { 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0x000000 },
		new int[] { 0x007B34, 0x007B34, 0x007B68, 0x007B60, 0x007B68, 0x007B60, 0x007B38, 0x000000 },
		new int[] { 0x018FE8, 0x018FE8, 0x0190DC, 0x0190D4, 0x0190DC, 0x0190D4, 0x01901C, 0x000000 },	
		new int[] { 0x01F3E4, 0x01F3E4, 0x01F4E0, 0x01F4D8, 0x01F4E0, 0x01F4D8, 0x01F420, 0x000000 },
		new int[] { 0x043058, 0x043060, 0x04318C, 0x043194, 0x043194, 0x04319C, 0x043094, 0x000000 },
		new int[] { 0x049CA0, 0x049CA8, 0x049DE0, 0x049DE8, 0x049DE8, 0x049DF0, 0x049DCD, 0x000000 },
		new int[] { 0x04BA30, 0x04BA38, 0x04BB70, 0x04BB78, 0x04BB78, 0x04BB80, 0x04BA6C, 0x000000 },
		new int[] { 0x04DCC8, 0x04DCD0, 0x04DE08, 0x04DE10, 0x04DE10, 0x04DE18, 0x04DD04, 0x000000 },
		new int[] { 0x04DEF8, 0x04DF00, 0x04E038, 0x04E040, 0x0CFA78, 0x0CFA5C, 0x0CF95C, 0x000000 },
		new int[] { 0x0AE00C, 0x0AE018, 0x0AE048, 0x0AE050, 0x0AE054, 0x0AE05C, 0x0AE084, 0x000000 },
		new int[] { 0x0CF8E0, 0x0CF8EC, 0x0CFA6C, 0x0CFA50, 0x0CFA78, 0x0CFA5C, 0x0CF95C, 0x000000 },
		new int[] { 0x0F52F4, 0x0F5300, 0x0F54B4, 0x0F5494, 0x0F54C0, 0x0F54A0, 0x0F5370, 0x000000 },
		new int[] { 0x0F5DA4, 0x0F5DB0, 0x0F5F4C, 0x0F5F2C, 0x0F5F58, 0x0F5F38, 0x0F5E20, 0x000000 },
		new int[] { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x000000 },
	};
	
	@Override
	protected void randomize(String romId, ByteStream rom) {
		setProgress((100 * 0) / 7);
		status("Processing chips...");
		ChipLibrary chipLibrary = randomizeChips(romId, rom);

		setProgress((100 * 1) / 7);
		status("Processing folders...");
		randomizeFolders(romId, rom, chipLibrary);

		setProgress((100 * 2) / 7);
		status("Processing shops...");
		randomizeShops(romId, rom, chipLibrary);

		setProgress((100 * 3) / 7);
		status("Processing Mystery Data...");
		randomizeMysteryData(romId, rom, chipLibrary);

		setProgress((100 * 4) / 7);
		status("Processing rewards...");
		randomizeRewards(romId, rom, chipLibrary);

		setProgress((100 * 5) / 7);
		status("Processing traders...");
		randomizeTraders(romId, rom, chipLibrary);

		setProgress((100 * 6) / 7);
		status("Processing battles...");
		randomizeBattles(romId, rom);

		setProgress(100);
	}
	
	protected int getVersionIndex(String romId) {
		switch (romId) {
			case "B4WJ0":
				return 0;
			case "B4BJ0":
				return 1;
			case "B4WE0":
				return 2;
			case "B4WP0":
				return 3;
			case "B4BE0":
				return 4;
			case "B4BP0":
				return 5;
			case "B4WJ1":
				return 6;
			//case "B4BJ1":
			//	return 7;
			default:
				throw new IllegalArgumentException("Unknown ROM ID \"" + romId
						+ "\".");
		}
	}

	protected int getVersionAddress(int firstAddr, String romId) {
		int index = getVersionIndex(romId);
		for (int[] offsetSet : offsets) {
			int i = 0;
			while (offsetSet[i] == -1) {
				i++;
			}
			if (offsetSet[i] == firstAddr) {
				return offsetSet[index];
			}
		}
		throw new IllegalArgumentException("Unknown address " + firstAddr
				+ ".");
	}
	
	protected ChipLibrary randomizeChips(String romId, ByteStream rom) {
		ChipLibrary chipLibrary = new ChipLibrary();
		PALibrary paLibrary = new PALibrary();
		ChipProducer chipProducer
				= new BN4ChipProducer(chipLibrary);
		ProgramAdvanceProducer paProducer
				= new BN4ProgramAdvanceProducer(paLibrary, chipLibrary);

		// Load all chips.
		ChipProvider chipProvider
				= new ChipProvider(this, chipProducer, paLibrary);
		RepeatStrategy chipRepeatStrat
				= new RepeatStrategy(chipProvider, 389);

		rom.setRealPosition(getVersionAddress(0x018FE8, romId));
		rom.setPosition(rom.readInt32());
		chipRepeatStrat.execute(rom);

		// Get Program Advances.        
		LoaderStrategy paStrat
				= new LoaderStrategy(paProducer);
		PointerListStrategy paPtrStrat
				= new PointerListStrategy(paStrat, 1, false);
		RepeatStrategy paPtrListStrat
				= new RepeatStrategy(paPtrStrat, new byte[] {0, 0, 0, 0});
		
		rom.setRealPosition(getVersionAddress(0x01F3E4, romId));
		rom.setPosition(rom.readInt32());
		paPtrListStrat.execute(rom);

		// Make sure the combo from the tutorial works.
		List<BattleChip> comboChips = new ArrayList<>(2);
		comboChips.add(chipLibrary.getElement(49));
		comboChips.add(chipLibrary.getElement(117));
		ProgramAdvance tutorialCombo = new ProgramAdvance(null, comboChips);
		paLibrary.addElement(-1, tutorialCombo);

		// Randomize chips.
		runProvider(chipProvider, rom);

		return chipLibrary;
	}
	
	protected void randomizeFolders(String romId, ByteStream rom,
			ChipLibrary library) {
		// Randomize Folder
		ItemProducer chipProducer
				= new BN456RewardProducer(library);
		FolderProducer producer
				= new FolderProducer(chipProducer);
		FolderProvider provider
				= new FolderProvider(this, producer);
		RepeatStrategy folderListStrat
				= new RepeatStrategy(provider, 7);

		rom.setRealPosition(getVersionAddress(0x043058, romId));
		rom.setPosition(rom.readInt32());
		folderListStrat.execute(rom);

		runProvider(provider, rom);

		// Fix tutorial folders.
		FolderProvider tutorialProvider
				= new FolderProvider(this, producer);
		PointerListStrategy tutorialPtrStrat
				= new PointerListStrategy(tutorialProvider, 1);
		FilterStrategy filterEmptyStrat
				= new FilterStrategy(tutorialPtrStrat, new byte[]{
					0, 0, 0, 0
				}, new byte[]{
					-1, -1, -1, -1
				}, true);
		RepeatStrategy tutorialPtrArrayStrat
				= new RepeatStrategy(filterEmptyStrat, 68);
		rom.setRealPosition(getVersionAddress(0x007B34, romId));
		rom.setPosition(rom.readInt32());
		tutorialPtrArrayStrat.execute(rom);
		byte[] codesWideSwrd = library.getElement(49).getCodes();
		byte[] codesAreaGrab = library.getElement(117).getCodes();
		List<Byte> possibleCodes = new ArrayList<>(4);
		for (byte a : codesWideSwrd) {
			for (byte b : codesAreaGrab) {
				if (a == b) {
					possibleCodes.add(a);
				}
			}
		}
		byte comboCode = possibleCodes.get(next(possibleCodes.size()));
		for (Folder folder : tutorialProvider.allData()) {
			for (Item chip : folder.getChips()) {
				if (chip.getChip().index() == 49
						|| chip.getChip().index() == 117) {
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
	
	protected void randomizeShops(String romId, ByteStream rom,
			ChipLibrary library) {
		ItemProducer producer
				= new BN456ShopItemProducer(library);
		ItemProvider provider
				= new ItemProvider(this, producer);
		RepeatStrategy itemArrayStrat
				= new RepeatStrategy(provider, new byte[]{0});

		rom.setRealPosition(getVersionAddress(0x04BA30, romId));
		int itemPoolPtr = rom.readInt32();
		PointerListStrategy itemArrayPtrStrat
				= new PointerListStrategy(itemArrayStrat, 1, itemPoolPtr);

		OffsetStrategy shopEntryStrat
				= new OffsetStrategy(itemArrayPtrStrat, 8, 4);
		FilterStrategy chipOrderFilterStrat
				= new FilterStrategy(shopEntryStrat, new byte[]{
					2, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0
				}, new byte[]{
					-1, -1, -1, -1, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0
				}, true);
		RepeatStrategy shopEntryArrayStrat
				= new RepeatStrategy(chipOrderFilterStrat, 17);

		rom.setRealPosition(getVersionAddress(0x049CA0, romId));
		rom.setPosition(rom.readInt32());
		shopEntryArrayStrat.execute(rom);

		runProvider(provider, rom);

		// Fix Chip Order.
		chipOrderFilterStrat.setSkip(false);
		provider.setCodeOnly(true);
		provider.clear();
		rom.setRealPosition(getVersionAddress(0x049CA0, romId));
		rom.setPosition(rom.readInt32());
		shopEntryArrayStrat.execute(rom);

		runProvider(provider, rom);
	}
	
	protected void randomizeMysteryData(String romId, ByteStream rom,
			ChipLibrary library) {
		// Randomize Green, Blue and Purple Mystery Data
		ItemProducer producer
				= new BN456MysteryDataContentsProducer(library);
		ItemProvider provider
				= new ItemProvider(this, producer);
		RepeatStrategy contentsArrayStrat
				= new RepeatStrategy(provider, new byte[]{0});
		PointerListStrategy contentsPtrStrat
				= new PointerListStrategy(contentsArrayStrat, 1);
		OffsetStrategy mysteryDataStrat
				= new OffsetStrategy(contentsPtrStrat, 8);
		RepeatStrategy mysteryDataArrayStrat
				= new RepeatStrategy(mysteryDataStrat, new byte[]{0});
		PointerListStrategy mysteryDataPtrStrat
				= new PointerListStrategy(mysteryDataArrayStrat, 1);
		RepeatStrategy subAreaArrayStrat
				= new RepeatStrategy(mysteryDataPtrStrat, new byte[]{
					0, 0, 0, 0
				});
		PointerListStrategy subAreaPtrStrat
				= new PointerListStrategy(subAreaArrayStrat, 1);
		FilterStrategy emptyAreaFilter
				= new FilterStrategy(subAreaPtrStrat, new byte[]{
					0, 0, 0, 0
				}, new byte[]{
					-1, -1, -1, -1
				}, true);
		RepeatStrategy areaArrayStrat
				= new RepeatStrategy(emptyAreaFilter, new byte[]{
					1, 0, 0, 0
				});
		PointerListStrategy areasArrayStrat
				= new PointerListStrategy(areaArrayStrat, 2);

		rom.setRealPosition(getVersionAddress(0x0AE00C, romId));
		areasArrayStrat.execute(rom);

		runProvider(provider, rom);
	}
	
	protected void randomizeRewards(String romId, ByteStream rom,
			ChipLibrary library) {
		// Randomize enemy drops.
		ItemProducer producer
				= new BN456RewardProducer(library);
		ItemProvider provider
				= new ItemProvider(this, producer);
		RepeatStrategy dropRepeatStrat
				= new RepeatStrategy(provider, 276 * 2 * 5 * 2);

		rom.setRealPosition(getVersionAddress(0x0F5DA4, romId));
		rom.setPosition(rom.readInt32());
		dropRepeatStrat.execute(rom);

		// Randomize battle Mystery Data
		RepeatStrategy mdRepeatStrat
				= new RepeatStrategy(provider, 8 * 8);

		rom.setRealPosition(getVersionAddress(0x0CF8E0, romId));
		rom.setPosition(rom.readInt32());
		mdRepeatStrat.execute(rom);

		runProvider(provider, rom);
	}
	
	protected void randomizeTraders(String romId, ByteStream rom,
			ChipLibrary library) {
		// Randomize chip traders.
		BN56ChipTraderProducer traderProducer
				= new BN56ChipTraderProducer(library);
		TraderProvider traderProvider
				= new TraderProvider(this, traderProducer, library);
		RepeatStrategy traderArrayStrat
				= new RepeatStrategy(traderProvider, 4);

		rom.setRealPosition(getVersionAddress(0x04DCC8, romId));
		rom.setPosition(rom.readInt32());
		traderArrayStrat.execute(rom);

		runProvider(traderProvider, rom);

		// Randomize Number Trader.
		rom.setRealPosition(getVersionAddress(0x04DEF8, romId));
		rom.advance(4);
		rom.setPosition(rom.readInt32());
		ItemProducer numberProducer
				= new BN56NumberCodeProducer(library, rom.readBytes(10));
		ItemProvider numberProvider
				= new ItemProvider(this, numberProducer);
		RepeatStrategy numberArrayStrat
				= new RepeatStrategy(numberProvider, new byte[]{-1});

		rom.setRealPosition(getVersionAddress(0x04DEF8, romId));
		rom.setPosition(rom.readInt32());
		numberArrayStrat.execute(rom);

		runProvider(numberProvider, rom);
	}
	
	protected void randomizeBattles(String romId, ByteStream rom) {
		BN4BattleProducer producer
				= new BN4BattleProducer();
		BN4BattleProvider provider
				= new BN4BattleProvider(this, producer);
		RepeatStrategy battleListStrat
				= new RepeatStrategy(provider, new byte[] { -1 });
		PointerListStrategy subAreaListStrat
				= new PointerListStrategy(battleListStrat, 16);
		PointerListStrategy subAreaListStrat2
				= new PointerListStrategy(battleListStrat, 64);
		PointerListStrategy subAreaListStrat3
				= new PointerListStrategy(battleListStrat, 48);
		PointerListStrategy subAreaListStrat4
				= new PointerListStrategy(battleListStrat, 32);

		PointerListStrategy lanAreaListStrat
				= new PointerListStrategy(subAreaListStrat, 21);
		PointerListStrategy compAreaListStrat
				= new PointerListStrategy(subAreaListStrat, 16);

		// Lan areas
		rom.setRealPosition(getVersionAddress(0x0F52F4, romId));
		rom.setPosition(rom.readInt32());
		rom.setPosition(rom.readInt32());
		lanAreaListStrat.execute(rom);
		
		// Comps
		rom.setRealPosition(getVersionAddress(0x0F52F4, romId));
		rom.setPosition(rom.readInt32());
		rom.advance(4);		
		rom.setPosition(rom.readInt32());
		compAreaListStrat.execute(rom);
		
		// ACDC Area
		rom.setRealPosition(getVersionAddress(0x0F52F4, romId));
		rom.setPosition(rom.readInt32());
		rom.advance(4);		
		rom.setPosition(rom.readInt32());
		rom.advance(64);
		rom.setPosition(rom.readInt32());
		subAreaListStrat2.execute(rom);
		
		// Town Area
		rom.setRealPosition(getVersionAddress(0x0F52F4, romId));
		rom.setPosition(rom.readInt32());
		rom.advance(4);		
		rom.setPosition(rom.readInt32());
		rom.advance(68);
		rom.setPosition(rom.readInt32());
		subAreaListStrat3.execute(rom);
		
		// Park Area
		rom.setRealPosition(getVersionAddress(0x0F52F4, romId));
		rom.setPosition(rom.readInt32());
		rom.advance(4);		
		rom.setPosition(rom.readInt32());
		rom.advance(72);
		rom.setPosition(rom.readInt32());
		subAreaListStrat2.execute(rom);
		
		// Foreign Areas
		rom.setRealPosition(getVersionAddress(0x0F52F4, romId));
		rom.setPosition(rom.readInt32());
		rom.advance(4);		
		rom.setPosition(rom.readInt32());
		rom.advance(76);
		rom.setPosition(rom.readInt32());
		subAreaListStrat4.execute(rom);
		
		// Undernet/Black Earth
		rom.setRealPosition(getVersionAddress(0x0F52F4, romId));
		rom.setPosition(rom.readInt32());
		rom.advance(4);		
		rom.setPosition(rom.readInt32());
		rom.advance(80);
		rom.setPosition(rom.readInt32());
		subAreaListStrat3.execute(rom);

		runProvider(provider, rom);
	}

	@Override
	public String[] getSupportedRomIds() {
		return new String[] {
			// Rockman EXE 4: Tournament Red Sun (J)
			"B4WJ0",
			// Rockman EXE 4: Tournament Blue Moon (J)
			"B4BJ0",
			// Mega Man Battle Network 4: Red Sun (U)
			"B4WE0",
			// Mega Man Battle Network 4: Red Sun (E)
			"B4WP0",
			// Mega Man Battle Network 4: Blue Moon (U)
			"B4BE0",
			// Mega Man Battle Network 4: Blue Moon (E)
			"B4BP0",
			// Rockman EXE 4: Tournament Red Sun (J) (Rev 1)
			"B4WJ1",
			// Rockman EXE 4: Tournament Blue Moon (J) (Rev 1)
			//"B4BJ1"
		};
	}
    
}
