package rand;

import java.util.Observable;
import java.util.Random;

public abstract class RandomizerContext extends Observable {
    private final Random rng;
    
    public RandomizerContext(int seed) {
        this.rng = new Random(seed);
    }
    
    protected int next(int bound) {
        return this.rng.nextInt(bound);
    }
    
    protected void runProvider(DataProvider provider, ByteStream rom) {
        provider.randomize(this.rng);
        provider.produce(rom);
    }
    
    protected void status(String message) {
        setChanged();
        notifyObservers(message);
    }
    
    public abstract void randomize(ByteStream rom);
}
