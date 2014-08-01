package mmbn.bn5;

import mmbn.BN456RewardProducer;
import mmbn.BN56ProgramAdvanceProducer;
import mmbn.ChipLibrary;
import mmbn.ChipProducer;
import mmbn.ChipProvider;
import mmbn.FolderProducer;
import mmbn.FolderProvider;
import mmbn.ItemProducer;
import mmbn.PALibrary;
import mmbn.ProgramAdvanceProducer;
import rand.ByteStream;
import rand.RandomizerContext;
import rand.strat.LoaderStrategy;
import rand.strat.PointerListStrategy;
import rand.strat.RepeatStrategy;

public class BN5RandomizerContext extends RandomizerContext {
    @Override
    protected void randomize(String romId, ByteStream rom) {
        setProgress((100 * 0) / 2);
        status("Processing chips...");
        ChipLibrary chipLibrary = randomizeChips(romId, rom);
        
        setProgress((100 * 1) / 2);
        status("Processing folders...");
        randomizeFolders(romId, rom, chipLibrary);
        
        setProgress((100 * 2) / 2);
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
        
        rom.setRealPosition(0x01DF18);
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
        
        rom.setRealPosition(0x025260);
        paPtrListListStrat.execute(rom);
        
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
        PointerListStrategy processListStrat
                = new PointerListStrategy(provider, 6);
        
        rom.setRealPosition(0x134A1C);
        rom.setPosition(rom.readInt32());
        processListStrat.execute(rom);
        
        runProvider(provider, rom);
    }

    @Override
    public String[] getSupportedRomIds() {
        return new String[] {
            // Mega Man Battle Network 5: Team Colonel (U)
            "BRKE",
        };
    }
}
