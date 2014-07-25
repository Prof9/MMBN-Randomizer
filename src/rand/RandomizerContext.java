package rand;

import java.util.Observable;
import java.util.Random;

public abstract class RandomizerContext extends Observable {
    private final Random rng;
    private int progress;
    
    public RandomizerContext(int seed) {
        this.rng = new Random(seed);
        this.progress = 0;
    }
    
    protected int next(int bound) {
        return this.rng.nextInt(bound);
    }
    
    protected void runProvider(DataProvider provider, ByteStream rom) {
        provider.randomize(this.rng);
        provider.produce(rom);
    }
    
    public int getProgress() {
        return this.progress;
    }
    
    protected void setProgress(int progress) {
        this.progress = progress;
        setChanged();
    }
    
    protected void status(String message) {
        setChanged();
        notifyObservers(message);
    }
    
    public abstract void randomize(ByteStream rom);
}
