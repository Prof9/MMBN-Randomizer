package mmbn;

import rand.DataProducer;

public abstract class BattleProducer implements DataProducer<Battle> {
    protected final DataProducer<BattleObject> objectProducer;
    
    public BattleProducer(final DataProducer<BattleObject> objectProducer) {
        this.objectProducer = objectProducer;
    }
    
    @Override
    public String getDataName() {
        return "battle";
    }
}
