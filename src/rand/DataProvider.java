package rand;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class DataProvider<T> implements StreamStrategy {
	protected final DataProducer<T> producer;
	protected final RandomizerContext context;
	private final int dataSize;
	// Use LinkedHashMap to keep the original ordering.
	private final LinkedHashMap<Integer, T> pointerMap;

	/**
	 * Constructs a new random data provider with no registered data using the
	 * given data producer.
	 *
	 * @param context The randomizer context constructing this provider.
	 * @param producer The producer to use.
	 */
	public DataProvider(RandomizerContext context, DataProducer<T> producer) {
		this.producer = producer;
		this.context = context;
		this.dataSize = producer.getDataSize();
		this.pointerMap = new LinkedHashMap<>();
	}

	/**
	 * Registers a new data entry in the given byte stream at its current
	 * position, if it was not registered already.
	 *
	 * @param stream The byte stream to read from.
	 */
	protected final void registerData(ByteStream stream) {
		int pointer = stream.getRealPosition();

		if (this.pointerMap.containsKey(pointer) && dataSize > 0) {
			stream.advance(this.producer.getDataSize());
		} else {
			T data = this.producer.readFromStream(stream);
			this.context.status("Found " + this.producer.getDataName() + " at "
					+ "0x" + String.format("%06X", pointer));
			this.pointerMap.put(pointer, data);
		}
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
	 * Gets all registered data.
	 *
	 * @return The collection of data.
	 */
	public List<T> allData() {
		return new ArrayList<>(this.pointerMap.values());
	}

	/**
	 * Clears all registered data.
	 */
	public void clear() {
		this.pointerMap.clear();
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
