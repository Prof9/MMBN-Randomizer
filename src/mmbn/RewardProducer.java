package mmbn;

import rand.DataProducer;
import mmbn.ChipLibrary;
import mmbn.Reward;

public abstract class RewardProducer implements DataProducer<Reward> {
    protected ChipLibrary library;
    
    public RewardProducer(ChipLibrary library) {
        this.library = library;
    }
    
    public ChipLibrary library() {
        return this.library;
    }
}
