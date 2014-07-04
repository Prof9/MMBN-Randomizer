package rand;

import java.util.Random;

public interface Randomizable {
    public void randomize(Random rng);
    
    public void produce(Rom rom);
}
