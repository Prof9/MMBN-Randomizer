package rand.strat;

import rand.Rom;
import rand.lib.Library;
import rand.lib.ProgramAdvance;

public class PALoaderStrategy implements RomStrategy {
    private final Library<ProgramAdvance> library;
    
    public PALoaderStrategy(Library<ProgramAdvance> library) {
        this.library = library;
    }
    
    @Override
    public void execute(Rom rom) {
        this.library.addElement(rom);
    }
}
