package rand.strat;

import rand.ByteStream;
import rand.lib.Library;
import rand.lib.ProgramAdvance;

public class PALoaderStrategy implements StreamStrategy {
    private final Library<ProgramAdvance> library;
    
    public PALoaderStrategy(Library<ProgramAdvance> library) {
        this.library = library;
    }
    
    @Override
    public void execute(ByteStream stream) {
        this.library.addElement(stream);
    }
}
