package rand;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public abstract class DataProvider<T> implements StreamStrategy {
    protected final DataProducer<T> producer;
    // Use LinkedHashMap to keep the original ordering.
    private final LinkedHashMap<Integer, T> pointerMap;
    
    /**
     * Constructs a new random data provider with no registered data using the
     * given data producer.
     * 
     * @param producer The producer to use.
     */
    public DataProvider(final DataProducer<T> producer) {
        this.producer = producer;
        this.pointerMap = new LinkedHashMap<>();
    }
    
    /**
     * Registers a new data entry in the given byte stream at its current
     * position and returns it.
     * 
     * @param stream The byte stream to read from.
     * @return The data entry that was read.
     */
    protected final T registerData(ByteStream stream) {
        int pointer = stream.getRealPosition();
        T data = this.producer.readFromStream(stream);
        System.out.println("Registered " + data.getClass().getName() + " at 0x"
                + String.format("%06X", pointer));
        this.pointerMap.put(pointer, data);
        return data;
    }
    
    /**
     * Randomizes all registered data with the given random number generator.
     * 
     * @param rng The random number generator to use.
     */
    public void randomize(Random rng) {
        for (Map.Entry<Integer, T> entry : this.pointerMap.entrySet()) {
            T data = entry.getValue();
            randomizeData(rng, data, entry.getKey());
            entry.setValue(data);
        }
    }
    
    /**
     * Randomizes the given data entry.
     * 
     * @param rng The random number generator to use.
     * @param data The data to randomize.
     * @param position The position of the data to randomize.
     */
    protected abstract void randomizeData(Random rng, T data, int position);
    
    /**
     * Writes all registered data to the given byte stream.
     * 
     * @param stream The byte stream to write to.
     */
    public void produce(ByteStream stream) {
        for (Map.Entry<Integer, T> entry : this.pointerMap.entrySet()) {
            stream.setRealPosition(entry.getKey());
            produceData(stream, entry.getValue());
        }
    }
    
    /**
     * Writes the given data entry to the given byte stream.
     * 
     * @param stream The byte stream to write to.
     * @param data The data to write.
     */
    protected void produceData(ByteStream stream, T data) {
        this.producer.writeToStream(stream, data);
    }
    
    /**
     * Reads data from the given byte stream and registers it.
     * 
     * @param stream The ROM to read from.
     */
    @Override
    public void execute(ByteStream stream) {
        registerData(stream);
    }
}
