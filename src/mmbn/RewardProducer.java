package mmbn;

import rand.DataProducer;

public abstract class RewardProducer implements DataProducer<Reward> {
    protected ChipLibrary library;
    
    public RewardProducer(ChipLibrary library) {
        this.library = library;
    }
    
    public ChipLibrary library() {
        return this.library;
    }
}
