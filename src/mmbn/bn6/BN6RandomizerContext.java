package mmbn.bn6;

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

public class BN6RandomizerContext extends RandomizerContext {
	private static final int[][] offsets = new int[][]{
		// Order: Gregar J, Falzar J, Gregar UE, Falzar UE
		new int[] { 0x00AAC0, 0x00AAC0, 0x00A4D8, 0x00A4D8 },
		new int[] { 0x021EC4, 0x021EC4, 0x021AB0, 0x021AB0 },
		new int[] { 0x0284F0, 0x0284F0, 0x0280DC, 0x0280DC },
		new int[] { 0x0299C8, 0x0299C8, 0x0295B4, 0x0295B4 },
		new int[] { 0x047CEC, 0x047D1C, 0x046CC0, 0x046CF0 },
		new int[] { 0x048474, 0x0484A4, 0x047448, 0x047478 },
		new int[] { 0x049F90, 0x049FC0, 0x048F64, 0x048F94 },
		new int[] { 0x04E84C, 0x04DA88, 0x04CDDC, 0x04C018 },
		// Undernet3 gate (100S)
		new int[] { 0x080622, 0x07F56A,       -1,       -1 },
		new int[] { 0x0A38A8, 0x0A23D0, 0x0A1370, 0x09FE90 },
		new int[] { 0x0AE424, 0x0ACBC4, 0x0ABF04, 0x0AA694 },
		new int[] { 0x0AEC34, 0x0AD3D4, 0x0AC714, 0x0AAEA4 },
		new int[] { 0x0DFAF4, 0x0DE294, 0x0DC284, 0x0DAA14 },
		new int[] { 0x141FF8, 0x140230, 0x139644, 0x137868 },
		new int[] { 0x14A12C, 0x148360, 0x13F260, 0x13D480 },
		new int[] { 0x14A18C, 0x1483C0, 0x13F2C0, 0x13D4E0 },
		// CentralArea1 gate (Reflectr)
		new int[] { 0x779479, 0x77B545, 0x75C5F5, 0x75E6B9 },
		new int[] { 0x77948D, 0x77B559,       -1,       -1 },
		new int[] {       -1,       -1, 0x75C61E, 0x75E6E2 },
		// SeasideArea1 gate (TrnArrw1)
		new int[] { 0x779C58, 0x77BD24,       -1,       -1 },
		new int[] {       -1,       -1, 0x75CFF9, 0x75F0BD },
		new int[] { 0x779CB2, 0x77BD7E, 0x75D08B, 0x75F14F },
		// Undernet2 gate (MchnSwrd)
		new int[] { 0x77C310, 0x77E3DC,       -1,       -1 },
		new int[] { 0x77C31C, 0x77E3E8,       -1,       -1 },
		// Undernet2 gate (100S)
		new int[] {       -1,       -1, 0x7600C9, 0x76218D },
		// Graveyard1 gate (45M)
		new int[] { 0x77CD30, 0x77EDFC,       -1,       -1 },
		// Graveyard2 gate (200S)
		new int[] { 0x77D643, 0x77F70F,       -1,       -1 },
		new int[] {       -1,       -1, 0x760D68, 0x762E2C },
	};
	private static final int[] shopCount = new int[]{18, 18, 19, 19};

	@Override
	public void randomize(String romId, ByteStream rom) {
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
			case "BR5J0":
				return 0;
			case "BR6J0":
				return 1;
			case "BR5E0":
			case "BR5P0":
				return 2;
			case "BR6E0":
			case "BR6P0":
				return 3;
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
				= new BN6ChipProducer(chipLibrary);
		ProgramAdvanceProducer paProducer
				= new BN56ProgramAdvanceProducer(paLibrary, chipLibrary);

		// Load all chips.
		ChipProvider chipProvider
				= new ChipProvider(this, chipProducer, paLibrary);
		RepeatStrategy chipRepeatStrat
				= new RepeatStrategy(chipProvider, 411);

		rom.setRealPosition(getVersionAddress(0x021EC4, romId));
		rom.setPosition(rom.readInt32());
		chipRepeatStrat.execute(rom);

		// Get Program Advances.        
		LoaderStrategy paStrat
				= new LoaderStrategy(paProducer);
		PointerListStrategy paPtrStrat
				= new PointerListStrategy(paStrat, 1, false);
		RepeatStrategy paPtrListStrat
				= new RepeatStrategy(paPtrStrat, new byte[]{0, 0, 0, 0});
		PointerListStrategy paPtrListListStrat
				= new PointerListStrategy(paPtrListStrat, 2);

		rom.setRealPosition(getVersionAddress(0x0299C8, romId));
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
				= new BN456RewardProducer(chipLibrary);
		ItemProvider linkNaviChipProvider
				= new ItemProvider(this, linkNaviChipProducer);
		RepeatStrategy linkNaviChipStrat
				= new RepeatStrategy(linkNaviChipProvider, 11);

		rom.setRealPosition(getVersionAddress(0x0284F0, romId));
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
		int ptr;
		// Japanese 100S
		ptr = getVersionAddress(0x080622, romId);
		if (ptr != -1) {
			rom.setRealPosition(ptr);
			if (rom.readInt32() == 0xDB072864) {
				rom.advance(-4);
				rom.writeUInt8((short) 0x00);
			} else {
				status("WARNING: Could not remove 100S gate.");
			}
		}
		// English 100S
		ptr = getVersionAddress(0x7600C9, romId);
		if (ptr != -1) {
			rom.setRealPosition(ptr);
			if (rom.readInt32() == 0x006434EF) {
				rom.advance(-2);
				rom.writeUInt8((short) 0x00);
			} else {
				status("WARNING: Could not remove 100S gate.");
			}
		}
		// Japanese 45M
		ptr = getVersionAddress(0x77CD30, romId);
		if (ptr != -1) {
			rom.setRealPosition(ptr);
			if (rom.readInt32() == 0x2D2D35EF) {
				rom.advance(-2);
				rom.writeUInt8((short) 0x00);
			} else {
				status("WARNING: Could not remove 45M gate.");
			}
		}
		// Japanese 200S
		ptr = getVersionAddress(0x77D643, romId);
		if (ptr != -1) {
			rom.setRealPosition(ptr);
			if (rom.readInt32() == 0xC820C834) {
				rom.advance(-3);
				rom.writeUInt8((short) 0x00);
			} else {
				status("WARNING: Could not remove 200S gate.");
			}
		}
		// English 200S
		ptr = getVersionAddress(0x760D68, romId);
		if (ptr != -1) {
			rom.setRealPosition(ptr);
			if (rom.readInt32() == 0xC8C80034) {
				rom.advance(-2);
				rom.writeUInt8((short) 0x00);
			} else {
				status("WARNING: Could not remove 200S gate.");
			}
		}

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
		PointerListStrategy processListStrat
				= new PointerListStrategy(provider, 6);

		rom.setRealPosition(getVersionAddress(0x141FF8, romId));
		processListStrat.execute(rom);

		runProvider(provider, rom);

		// Get new starting Folder
		Folder startFolder = provider.allData().get(0);
		List<Item> chips = startFolder.getChips();

		// Remove chip check gates.
		int ptr;
		Item chipEntry;
		int chipIndex;
		// Set a random chip to the CentralArea1 gate
		chipEntry = chips.remove(next(chips.size()));
		chipIndex = chipEntry.getChip().index();
		ptr = getVersionAddress(0x779479, romId);
		if (ptr != -1) {
			rom.setRealPosition(ptr);
			if (rom.readInt32() == 0x00832AEF) {
				rom.advance(-2);
				rom.writeUInt16(chipIndex);
			} else {
				status("WARNING: Could not remove Rflectr1 gate.");
			}
		}
		ptr = getVersionAddress(0x77948D, romId);
		if (ptr != -1) {
			rom.setRealPosition(ptr);
			if (rom.readInt32() == 0x018300FA) {
				rom.advance(-2);
				rom.writeUInt16(chipIndex + 0x100);
			} else {
				status("WARNING: Could not replace Rflectr1 gate message.");
			}
		}
		ptr = getVersionAddress(0x75C61E, romId);
		if (ptr != -1) {
			rom.setRealPosition(ptr);
			if (rom.readInt32() == 0x008300FA) {
				rom.advance(-2);
				rom.writeUInt8((short) (chipIndex & 0xFF));
				rom.advance(1);
				rom.writeUInt8((short) ((chipIndex >> 8) + 1));
			} else {
				status("WARNING: Could not replace Rflectr1 gate message.");
			}
		}

		// Set another random chip to the SeasideArea1 gate
		chipEntry = chips.remove(next(chips.size()));
		chipIndex = chipEntry.getChip().index();
		ptr = getVersionAddress(0x779C58, romId);
		if (ptr != -1) {
			rom.setRealPosition(ptr);
			if (rom.readInt32() == 0x00182AEF) {
				rom.advance(-2);
				rom.writeUInt16(chipIndex);
			} else {
				status("WARNING: Could not remove TrnArrw1 gate.");
			}
		}
		ptr = getVersionAddress(0x75CFF9, romId);
		if (ptr != -1) {
			rom.setRealPosition(ptr);
			if (rom.readInt32() == 0x18002AEF) {
				rom.advance(-1);
				rom.writeUInt16(chipIndex);
			} else {
				status("WARNING: Could not remove TrnArrw1 gate.");
			}
		}
		ptr = getVersionAddress(0x779CB2, romId);
		if (ptr != -1) {
			rom.setRealPosition(ptr);
			if (rom.readInt32() == 0x011800FA) {
				rom.advance(-2);
				rom.writeUInt16(chipIndex + 0x100);
			} else {
				status("WARNING: Could not replace TrnArrw1 gate message.");
			}
		}
		// Set another random chip to the Undernet2 gate
		chipEntry = chips.remove(next(chips.size()));
		chipIndex = chipEntry.getChip().index();
		ptr = getVersionAddress(0x77C310, romId);
		if (ptr != -1) {
			rom.setRealPosition(ptr);
			if (rom.readInt32() == 0x562A00EF) {
				rom.advance(-1);
				rom.writeUInt16(chipIndex);
			} else {
				status("WARNING: Could not remove MchnSwrd gate.");
			}
		}
		ptr = getVersionAddress(0x77C31C, romId);
		if (ptr != -1) {
			rom.setRealPosition(ptr);
			if (rom.readInt32() == 0x015600FA) {
				rom.advance(-2);
				rom.writeUInt16(chipIndex + 0x100);
			} else {
				status("WARNING: Could not replace MchnSwrd gate message.");
			}
		}

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
				= new RepeatStrategy(filterEmptyStrat, 12);
		rom.setRealPosition(getVersionAddress(0x00AAC0, romId));
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
		for (Folder folder : tutorialProvider.allData()) {
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

	protected void randomizeBattles(String romId, ByteStream rom) {
		BN56BattleProducer producer
				= new BN56BattleProducer();
		BN6BattleProvider provider
				= new BN6BattleProvider(this, producer);
		RepeatStrategy battleListStrat
				= new RepeatStrategy(provider, new byte[]{-1});
		PointerListStrategy subAreaListStrat
				= new PointerListStrategy(battleListStrat, 16);

		PointerListStrategy lanAreaListStrat
				= new PointerListStrategy(subAreaListStrat, 21);
		PointerListStrategy lanStrat
				= new PointerListStrategy(lanAreaListStrat, 1);

		PointerListStrategy megaAreaListStrat
				= new PointerListStrategy(subAreaListStrat, 23);
		PointerListStrategy megaStrat
				= new PointerListStrategy(megaAreaListStrat, 1);

		rom.setRealPosition(getVersionAddress(0x0AE424, romId));
		for (int i = 0; i < 4; i++) {
			int ptr = rom.readInt32();
			rom.push();
			rom.setPosition(ptr);
			lanStrat.execute(rom);
			megaStrat.execute(rom);
			rom.pop();
		}

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
				= new RepeatStrategy(provider, 401 * 2 * 5 * 2);

		rom.setRealPosition(getVersionAddress(0x0AEC34, romId));
		rom.setPosition(rom.readInt32());
		dropRepeatStrat.execute(rom);

		// Randomize battle Mystery Data
		RepeatStrategy mdRepeatStrat
				= new RepeatStrategy(provider, 8 * 8);

		rom.setRealPosition(getVersionAddress(0x0DFAF4, romId));
		rom.setPosition(rom.readInt32());
		mdRepeatStrat.execute(rom);

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
				= new PointerListStrategy(mysteryDataArrayStrat, 16);
		PointerListStrategy subAreaPtrStrat
				= new PointerListStrategy(mysteryDataPtrStrat, 1);
		OffsetStrategy subAreaStrat
				= new OffsetStrategy(subAreaPtrStrat, 4);
		FilterStrategy emptyAreaFilter
				= new FilterStrategy(subAreaStrat, new byte[]{
					0, 0, 0, 0, 0, 0, 0, 0
				}, new byte[]{
					-1, -1, -1, -1, 0, 0, 0, 0
				}, true);
		RepeatStrategy areaArrayStrat
				= new RepeatStrategy(emptyAreaFilter, new byte[]{
					1, 0, 0, 0
				});
		PointerListStrategy areasArrayStrat
				= new PointerListStrategy(areaArrayStrat, 2);

		rom.setRealPosition(getVersionAddress(0x0A38A8, romId));
		rom.advance(4);
		areasArrayStrat.execute(rom);

		// Randomize Gold Mystery Data
		OffsetStrategy goldAreaStrat
				= new OffsetStrategy(contentsPtrStrat, 4, 4);
		RepeatStrategy goldAreaArrayStrat
				= new RepeatStrategy(goldAreaStrat, new byte[]{0, 0, 0, 0});

		rom.setRealPosition(getVersionAddress(0x0A38A8, romId));
		rom.setPosition(rom.readInt32());
		goldAreaArrayStrat.execute(rom);

		runProvider(provider, rom);
	}

	protected void randomizeTraders(String romId, ByteStream rom,
			ChipLibrary library) {
		// Randomize chip traders.
		ChipTraderProducer traderProducer
				= new BN56ChipTraderProducer(library);
		TraderProvider traderProvider
				= new TraderProvider(this, traderProducer, library);
		RepeatStrategy traderArrayStrat
				= new RepeatStrategy(traderProvider, 5);

		rom.setRealPosition(getVersionAddress(0x04E84C, romId));
		rom.setPosition(rom.readInt32());
		traderArrayStrat.execute(rom);

		runProvider(traderProvider, rom);

		// Randomize Number Trader.
		rom.setRealPosition(getVersionAddress(0x14A12C, romId));
		rom.advance(4);
		rom.setPosition(rom.readInt32());
		ItemProducer numberProducer
				= new BN56NumberCodeProducer(library, rom.readBytes(10));
		ItemProvider numberProvider
				= new ItemProvider(this, numberProducer);
		RepeatStrategy numberArrayStrat
				= new RepeatStrategy(numberProvider, new byte[]{-1});

		rom.setRealPosition(getVersionAddress(0x14A12C, romId));
		rom.setPosition(rom.readInt32());
		numberArrayStrat.execute(rom);

		runProvider(numberProvider, rom);

		// Randomize Boktai Trader.
		BoktaiTraderEntryProducer boktaiTraderEntryProducer
				= new BN56BoktaiTraderEntryProducer(library);
		ItemProvider boktaiTraderEntryProvider
				= new ItemProvider(this, boktaiTraderEntryProducer);
		RepeatStrategy boktaiTraderStrat
				= new RepeatStrategy(boktaiTraderEntryProvider,
						new byte[]{-1});
		PointerListStrategy boktaiTraderArrayStrat
				= new PointerListStrategy(boktaiTraderStrat, 4);
		rom.setRealPosition(getVersionAddress(0x14A18C, romId));
		rom.setPosition(rom.readInt32());
		boktaiTraderArrayStrat.execute(rom);

		runProvider(boktaiTraderEntryProvider, rom);
	}

	protected void randomizeShops(String romId, ByteStream rom,
			ChipLibrary library) {
		ItemProducer producer
				= new BN456ShopItemProducer(library);
		ItemProvider provider
				= new ItemProvider(this, producer);
		RepeatStrategy itemArrayStrat
				= new RepeatStrategy(provider, new byte[]{0});

		rom.setRealPosition(getVersionAddress(0x048474, romId));
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
				= new RepeatStrategy(chipOrderFilterStrat,
						shopCount[getVersionIndex(romId)]);

		rom.setRealPosition(getVersionAddress(0x047CEC, romId));
		rom.setPosition(rom.readInt32());
		shopEntryArrayStrat.execute(rom);

		runProvider(provider, rom);
		List<Item> shopItems = provider.allData();

		// Fix Chip Order.
		chipOrderFilterStrat.setSkip(false);
		provider.setCodeOnly(true);
		provider.clear();
		rom.setRealPosition(getVersionAddress(0x047CEC, romId));
		rom.setPosition(rom.readInt32());
		shopEntryArrayStrat.execute(rom);

		runProvider(provider, rom);

		// Fix shop extensions.
		rom.setRealPosition(getVersionAddress(0x049F90, romId));
		rom.setPosition(rom.readInt32());
		provider.clear();
		itemArrayStrat.execute(rom);
		List<Item> extItems = provider.allData();

		int itemIndex = 0;
		for (int i = 0; i < extItems.size(); i++) {
			// Find next item with amount = 0.
			ShopItem placeholder = null;
			while (itemIndex < shopItems.size()) {
				ShopItem item = (ShopItem) shopItems.get(itemIndex++);
				if (item.getStock() == 0) {
					placeholder = item;
					break;
				}
			}
			if (placeholder == null) {
				break;
			}

			ShopItem replacement = (ShopItem) extItems.get(i);
			replacement.setItem(placeholder);
		}

		provider.produce(rom);
	}

	@Override
	public String[] getSupportedRomIds() {
		return new String[] {
			// Rockman EXE 6: Cyber Beast Gregar (J)
			"BR5J0",
			// Rockman EXE 6: Cyber Beast Falzar (J)
			"BR6J0",
			// Mega Man Battle Network 6: Cybeast Gregar (U)
			"BR5E0",
			// Mega Man Battle Network 6: Cybeast Gregar (E)
			"BR5P0",
			// Mega Man Battle Network 6: Cybeast Falzar (U)
			"BR6E0",
			// Mega Man Battle Network 6: Cybeast Falzar (E)
			"BR6P0"
		};
	}
}
