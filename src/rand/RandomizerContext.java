package rand;

import java.util.Random;

public abstract class RandomizerContext {
    private final Random rng;
    
    public RandomizerContext(Random rng) {
        this.rng = rng;
    }
    
    protected int next(int bound) {
        return this.rng.nextInt(bound);
    }
    
    protected void process(DataProvider provider, ByteStream rom) {
        provider.randomize(this.rng);
        provider.produce(rom);
    }
    
    public abstract void randomize(ByteStream rom);
}
