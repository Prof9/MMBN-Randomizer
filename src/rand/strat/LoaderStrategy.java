package rand.strat;

import rand.ByteStream;
import rand.DataProducer;
import rand.StreamStrategy;

public class LoaderStrategy implements StreamStrategy {
    protected final DataProducer producer;
    
    public LoaderStrategy(final DataProducer producer) {
        this.producer = producer;
    }
    
    public DataProducer producer() {
        return this.producer;
    }
    
    @Override
    public void execute(ByteStream stream) {
        this.producer.readFromStream(stream);
    }
}
