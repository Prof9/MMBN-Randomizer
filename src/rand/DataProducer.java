package rand;

/**
 * A producer that supports reading and writing data objects of the given type
 * to and from a byte stream.
 * 
 * @param <T> The type of data objects this producer can read and write.
 */
public interface DataProducer<T> {
    /**
     * Gets the name of the type of data objects this producer recognizes.
     * 
     * @return The name in lowercase (unless it is a proper noun).
     */
    public String getDataName();
    
    /**
     * Gets the size of the data objects this producer recognizes in amount of
     * bytes, or -1 if the data objects have a variable size.
     * 
     * @return The size, or -1.
     */
    public int getDataSize();
    
    /**
     * Reads a data object from the given byte stream.
     * 
     * @param stream The byte stream to read from.
     * @return The data object that was read.
     */
    public T readFromStream(ByteStream stream);
    
    /**
     * Writes the given data object to the given byte stream.
     * 
     * @param stream The byte stream to write to.
     * @param data The data object to write.
     */
    public void writeToStream(ByteStream stream, T data);
}