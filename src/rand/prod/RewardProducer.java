package rand.prod;

import rand.DataProducer;
import rand.lib.ChipLibrary;
import rand.obj.Reward;

public abstract class RewardProducer implements DataProducer<Reward> {
    protected ChipLibrary library;
    
    public RewardProducer(ChipLibrary library) {
        this.library = library;
    }
    
    public ChipLibrary library() {
        return this.library;
    }
}
