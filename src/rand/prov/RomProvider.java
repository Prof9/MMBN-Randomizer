package rand.prov;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import rand.Randomizable;
import rand.Rom;
import rand.strat.RomStrategy;

/**
 * A provider that reads data from a ROM, randomizes it, then writes it back to
 * a ROM.
 */
public abstract class RomProvider
implements Randomizable, RomStrategy {
    /**
     * A wrapper for a byte array with optional type.
     */
    protected final class DataEntry {
        private final Enum type;
        private final byte[] bytes;
        
        /**
         * Constructs a new data entry with no type name.
         * 
         * @param bytes The byte array to wrap.
         */
        public DataEntry(final byte[] bytes) {
            this(null, bytes);
        }
        
        /**
         * Constructs a new data entry with the given type.
         * 
         * @param type The type of the data entry.
         * @param bytes The byte array to wrap.
         */
        public DataEntry(final Enum type, final byte[] bytes) {            
            this.type = type;
            this.bytes = bytes;
        }
        
        /**
         * Gets the data entry's type string, or null if there is none.
         * 
         * @return The data entry's type.
         */
        public Enum type() {
            return this.type;
        }
        
        /**
         * Gets the data entry's byte array length.
         * 
         * @return The byte length of the data entry.
         */
        public int length() {
            return this.bytes.length;
        }
        
        /**
         * Gets the data entry's current byte array.
         * 
         * @return The wrapped bytes.
         */
        public byte[] getBytes() {
            return this.bytes;
        }
        
        /**
         * Sets the data entry's current byte array. The new byte array must be
         * of the same length as the old one.
         * 
         * @param bytes The new bytes to wrap.
         */
        public void setBytes(byte[] bytes) {
            // Check preconditions.
            if (bytes == null) {
                throw new NullPointerException("New bytes cannot be null.");
            }
            if (bytes.length != this.bytes.length) {
                throw new IllegalArgumentException("New byte array must be "
                        + "the same length.");
            }
            
            // Copy all new bytes to the internal array.
            System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
        }
    }
    
    // Use LinkedHashMap to keep the original ordering.
    private final LinkedHashMap<Integer, DataEntry> dataMap;
    
    /**
     * Constructs a new RandomProvider with no registered data.
     */
    public RomProvider() {
        this.dataMap = new LinkedHashMap<>();
    }
    
    /**
     * Gets the data entry registered with the given ROM position, or null if
     * there is none.
     * 
     * @param position The ROM position to get the data entry of.
     * @return The data entry, or null.
     */
    protected DataEntry getEntry(int position) {
        return this.dataMap.get(position);
    }
    
    /**
     * Registers a new data entry at the current ROM position with a given
     * amount of bytes read from the ROM.
     * 
     * @param rom The ROM to read from.
     * @param byteCount The amount of bytes to read.
     */
    protected void registerData(Rom rom, int byteCount) {
        registerData(rom, null, byteCount);
    }
    
    /**
     * Registers a new data entry at the current ROM position with the given
     * type and a given amount of bytes read from the ROM.
     * 
     * @param rom The ROM to read from.
     * @param type The type of the data entry.
     * @param byteCount The amount of bytes to read.
     */
    protected final void registerData(Rom rom, Enum type, int byteCount) {
        this.dataMap.put(rom.getPosition(),
                new DataEntry(type, rom.readBytes(byteCount)));
    }
    
    /**
     * Writes all registered data to the given ROM.
     * 
     * @param rom The ROM to write to.
     */
    @Override
    public final void produce(Rom rom) {
        for (Map.Entry<Integer, DataEntry> entry : this.dataMap.entrySet()) {
            rom.setPosition(entry.getKey());
            rom.writeBytes(entry.getValue().getBytes());
        }
    }
    
    /**
     * Randomizes the given data entry.
     * 
     * @param rng The random number generator to use.
     * @param position The ROM position of the data to be randomized.
     * @param data The data to randomize.
     */
    protected abstract void randomizeData(Random rng, int position,
            DataEntry data);
    
    /**
     * Randomizes all registered data with the given random number generator.
     * 
     * @param rng The random number generator to use.
     */
    @Override
    public void randomize(Random rng) {
        for (Map.Entry<Integer, DataEntry> entry : this.dataMap.entrySet()) {
            DataEntry dataEntry = entry.getValue();
            randomizeData(rng, entry.getKey(), dataEntry);
            entry.setValue(dataEntry);
        }
    }
    
    /**
     * Reads data from the ROM and registers it.
     * 
     * @param rom The ROM to read from.
     */
    @Override
    public abstract void execute(Rom rom);
}
