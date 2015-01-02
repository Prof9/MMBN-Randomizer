package mmbn.bn5;

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

public class BN5RandomizerContext extends RandomizerContext {
	private static final int[][] offsets = new int[][]{
		new int[] { 0x008D44, 0x008D44, 0x008D44, 0x008D44 },
		new int[] { 0x009134, 0x009134, 0x009134, 0x009134 },
		new int[] { 0x01DED8, 0x01DED4, 0x01DF1C, 0x01DF18 },
		new int[] { 0x023F0C, 0x023F08, 0x023F50, 0x023F4C },
		new int[] { 0x025218, 0x02521C, 0x02525C, 0x025260 },
		new int[] { 0x047F00, 0x047F08, 0x0486C4, 0x0486CC },
		new int[] { 0x048264, 0x04826C, 0x048A28, 0x048A30 },
		new int[] { 0x04826C, 0x048274, 0x048A30, 0x048A38 },
		new int[] { 0x04B0B8, 0x04B0C0, 0x04B87C, 0x04B884 },
		new int[] { 0x04B70C, 0x04B714, 0x04BED4, 0x04BEDC },
		new int[] { 0x0A6EC4, 0x0A6FA8, 0x0A7560, 0x0A7644 },
		new int[] { 0x0DB2EC, 0x0DB3D4, 0x0DB5C0, 0x0DB6A8 },
		new int[] { 0x10F348, 0x10F430, 0x10F72C, 0x10F814 },
		new int[] { 0x10FC04, 0x10FCEC, 0x10FFE8, 0x1100D0 },
		new int[] { 0x1344EC, 0x1345D4, 0x134934, 0x134A1C },
		new int[] { 0x140544, 0x14062C, 0x1409C0, 0x140AA8 },
		new int[] { 0x1405A4, 0x14068C, 0x140A20, 0x140B08 },
	};
	
	@Override
	protected void randomize(String romId, ByteStream rom) {
		setProgress((100 * 0) / 8);
		status("Processing chips...");
		ChipLibrary chipLibrary = randomizeChips(romId, rom);

		setProgress((100 * 1) / 8);
		status("Processing folders...");
		randomizeFolders(romId, rom, chipLibrary);

		setProgress((100 * 2) / 8);
		status("Processing shops...");
		randomizeShops(romId, rom, chipLibrary);

		setProgress((100 * 3) / 8);
		status("Processing Mystery Data...");
		randomizeMysteryData(romId, rom, chipLibrary);
		
		setProgress((100 * 4) / 8);
		status("Processing item panels...");
		randomizeItemPanels(romId, rom, chipLibrary);

		setProgress((100 * 5) / 8);
		status("Processing rewards...");
		randomizeRewards(romId, rom, chipLibrary);

		setProgress((100 * 6) / 8);
		status("Processing traders...");
		randomizeTraders(romId, rom, chipLibrary);

		setProgress((100 * 7) / 8);
		status("Processing battles...");
		randomizeBattles(romId, rom);

		setProgress((100 * 8) / 8);
	}
	
	protected int getVersionIndex(String romId) {
		switch (romId) {
			case "BRBJ":
				return 0;
			case "BRKJ":
				return 1;
			case "BRBE":
			case "BRBP":
				return 2;
			case "BRKE":
			case "BRKP":
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
				= new BN5ChipProducer(chipLibrary);
		ProgramAdvanceProducer paProducer
				= new BN56ProgramAdvanceProducer(paLibrary, chipLibrary);

		// Load all chips.
		ChipProvider chipProvider
				= new ChipProvider(this, chipProducer, paLibrary);
		RepeatStrategy chipRepeatStrat
				= new RepeatStrategy(chipProvider, 424);

		rom.setRealPosition(getVersionAddress(0x01DED8, romId));
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

		rom.setRealPosition(getVersionAddress(0x025218, romId));
		paPtrListListStrat.execute(rom);

		// Make sure the combo from the tutorial works.
		List<BattleChip> comboChips = new ArrayList<>(2);
		comboChips.add(chipLibrary.getElement(49));
		comboChips.add(chipLibrary.getElement(117));
		ProgramAdvance tutorialCombo = new ProgramAdvance(null, comboChips);
		paLibrary.addElement(-1, tutorialCombo);

		// Randomize chips.
		runProvider(chipProvider, rom);

		// Fix LinkNavi special chips.
		ItemProducer naviChipProducer
				= new BN456RewardProducer(chipLibrary);
		ItemProvider naviChipProvider
				= new ItemProvider(this, naviChipProducer);
		RepeatStrategy naviChipStrat
				= new RepeatStrategy(naviChipProvider, 12 * 2);

		rom.setRealPosition(getVersionAddress(0x023F0C, romId));
		rom.setPosition(rom.readInt32());
		naviChipStrat.execute(rom);

		List<Item> linkNaviChips = naviChipProvider.allData();
		for (Item linkNaviChip : linkNaviChips) {
			BattleChip chip = linkNaviChip.getChip();
			byte code = chip.getCodes()[0];
			linkNaviChip.setChipCode(chip, code);
		}
		naviChipProvider.produce(rom);

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

		rom.setRealPosition(getVersionAddress(0x1344EC, romId));
		rom.setPosition(rom.readInt32());
		processListStrat.execute(rom);

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
				= new RepeatStrategy(filterEmptyStrat, 8);
		rom.setRealPosition(getVersionAddress(0x008D44, romId));
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
				= new BN56ShopItemProducer(library);
		ItemProvider provider
				= new ItemProvider(this, producer);
		RepeatStrategy itemArrayStrat
				= new RepeatStrategy(provider, new byte[]{0});

		rom.setRealPosition(getVersionAddress(0x047F00, romId));
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
				= new RepeatStrategy(chipOrderFilterStrat, 18);

		rom.setRealPosition(getVersionAddress(0x04826C, romId));
		rom.setPosition(rom.readInt32());
		shopEntryArrayStrat.execute(rom);

		runProvider(provider, rom);
		List<Item> shopItems = provider.allData();

		// Fix Chip Order.
		chipOrderFilterStrat.setSkip(false);
		provider.setCodeOnly(true);
		provider.clear();
		rom.setRealPosition(getVersionAddress(0x04826C, romId));
		rom.setPosition(rom.readInt32());
		shopEntryArrayStrat.execute(rom);

		runProvider(provider, rom);

		// Fix shop extensions.
		PointerListStrategy shopAddonStrat
				= new PointerListStrategy(itemArrayStrat, 2);
		rom.setRealPosition(getVersionAddress(0x048264, romId));
		provider.clear();
		shopAddonStrat.execute(rom);
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

	protected void randomizeMysteryData(String romId, ByteStream rom,
			ChipLibrary library) {
		// Randomize Green, Blue and Purple Mystery Data
		ItemProducer producer
				= new BN56MysteryDataContentsProducer(library);
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

		rom.setRealPosition(getVersionAddress(0x0A6EC4, romId));
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
				= new RepeatStrategy(provider, 377 * 2 * 5 * 2);

		rom.setRealPosition(getVersionAddress(0x10FC04, romId));
		rom.setPosition(rom.readInt32());
		dropRepeatStrat.execute(rom);

		// Randomize battle Mystery Data
		RepeatStrategy mdRepeatStrat
				= new RepeatStrategy(provider, 8 * 8);

		rom.setRealPosition(getVersionAddress(0x0DB2EC, romId));
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

		rom.setRealPosition(getVersionAddress(0x04B0B8, romId));
		rom.setPosition(rom.readInt32());
		traderArrayStrat.execute(rom);

		runProvider(traderProvider, rom);

		// Randomize Number Trader.
		rom.setRealPosition(getVersionAddress(0x140544, romId));
		rom.advance(4);
		rom.setPosition(rom.readInt32());
		ItemProducer numberProducer
				= new BN56NumberCodeProducer(library, rom.readBytes(10));
		ItemProvider numberProvider
				= new ItemProvider(this, numberProducer);
		RepeatStrategy numberArrayStrat
				= new RepeatStrategy(numberProvider, new byte[]{-1});

		rom.setRealPosition(getVersionAddress(0x140544, romId));
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
		rom.setRealPosition(getVersionAddress(0x1405A4, romId));
		rom.setPosition(rom.readInt32());
		boktaiTraderArrayStrat.execute(rom);

		runProvider(boktaiTraderEntryProvider, rom);
	}
	
	protected void randomizeItemPanels(String romId, ByteStream rom,
			ChipLibrary library) {
		// Randomize Item Panels
		BN5ItemPanelContentsProducer itemPanelContentsProducer
				= new BN5ItemPanelContentsProducer(library);
		ItemProvider itemPanelContentsProvider
				= new ItemProvider(this, itemPanelContentsProducer);
		RepeatStrategy itemPanelArrayStrat
				= new RepeatStrategy(itemPanelContentsProvider,
						new byte[] { -1 });
		PointerListStrategy itemPanelArrayPointerStrat
				= new PointerListStrategy(itemPanelArrayStrat, 1);
		OffsetStrategy liberationMissionStrat
				= new OffsetStrategy(itemPanelArrayPointerStrat, 52, 36);
		RepeatStrategy liberationMissionArrayStrat
				= new RepeatStrategy(liberationMissionStrat, 9);
		
		rom.setRealPosition(getVersionAddress(0x04B70C, romId));
		rom.setPosition(rom.readInt32());
		liberationMissionArrayStrat.execute(rom);
		runProvider(itemPanelContentsProvider, rom);
		
		// Randomize Bonus Panels
		BN5BonusPanelContentsProducer bonusPanelContentsProducer
				= new BN5BonusPanelContentsProducer(library);
		ItemProvider bonusPanelContentsProvider
				= new ItemProvider(this, bonusPanelContentsProducer);
		RepeatStrategy bonusPanelContentsArrayStrat
				= new RepeatStrategy(bonusPanelContentsProvider,
						new byte[] { -1 });
		PointerListStrategy bonusPanelContentsArrayPointerStrat
				= new PointerListStrategy(bonusPanelContentsArrayStrat, 1);
		OffsetStrategy bonusPanelStrat
				= new OffsetStrategy(bonusPanelContentsArrayPointerStrat,
						16, 4);
		RepeatStrategy bonusPanelArrayStrat
				= new RepeatStrategy(bonusPanelStrat, new byte[] { 0, 0 });
		PointerListStrategy bonusPanelArrayPointerStrat
				= new PointerListStrategy(bonusPanelArrayStrat, 1);
		liberationMissionStrat
				= new OffsetStrategy(bonusPanelArrayPointerStrat, 44, 44);
		liberationMissionArrayStrat
				= new RepeatStrategy(liberationMissionStrat, 9);
		
		rom.setRealPosition(getVersionAddress(0x04B70C, romId));
		rom.setPosition(rom.readInt32());
		liberationMissionArrayStrat.execute(rom);
		
		runProvider(bonusPanelContentsProvider, rom);
	}

	protected void randomizeBattles(String romId, ByteStream rom) {
		BN56BattleProducer producer
				= new BN56BattleProducer();
		BN5BattleProvider provider
				= new BN5BattleProvider(this, producer);
		RepeatStrategy battleListStrat
				= new RepeatStrategy(provider, new byte[]{-1});
		PointerListStrategy subAreaListStrat
				= new PointerListStrategy(battleListStrat, 16);

		PointerListStrategy lanAreaListStrat
				= new PointerListStrategy(subAreaListStrat, 21);
		PointerListStrategy lanStrat
				= new PointerListStrategy(lanAreaListStrat, 1);

		PointerListStrategy megaAreaListStrat1
				= new PointerListStrategy(subAreaListStrat, 11);
		PointerListStrategy megaAreaListStrat2
				= new PointerListStrategy(subAreaListStrat, 9);

		rom.setRealPosition(getVersionAddress(0x10F348, romId));
		rom.setPosition(rom.readInt32());
		lanStrat.execute(rom);

		rom.setPosition(rom.readInt32());
		megaAreaListStrat1.execute(rom);
		rom.advance(4);
		megaAreaListStrat2.execute(rom);

		// Randomize Liberation Mission battles
		rom.setRealPosition(getVersionAddress(0x009134, romId));
		rom.setPosition(rom.readInt32());
		battleListStrat.execute(rom);

		runProvider(provider, rom);
	}

	@Override
	public String[] getSupportedRomIds() {
		return new String[] {
			// Rockman EXE 5: Team of Blues (J)
			"BRBJ",
			// Rockman EXE 5: Team of Colonel (J)
			"BRKJ",
			// Mega Man Battle Network 5: Team ProtoMan (U)
			"BRBE",
			// Mega Man Battle Network 5: Team Colonel (U)
			"BRKE",
			// Mega Man Battle Network 5: Team ProtoMan (E)
			"BRBP",
			// Mega Man Battle Network 5: Team Colonel (E)
			"BRKP"
		};
	}
}
