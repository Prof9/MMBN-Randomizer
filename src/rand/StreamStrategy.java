package rand;

/**
 * A strategy that acts on a byte stream.
 */
public interface StreamStrategy {
    /**
     * Executes the implemented strategy on the given byte stream.
     * 
     * @param stream The ROM to execute on.
     */
    public void execute(ByteStream stream);
}
