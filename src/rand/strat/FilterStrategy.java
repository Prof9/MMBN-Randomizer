package rand.strat;

import java.util.Arrays;
import rand.ByteStream;
import rand.StreamStrategy;

/**
 * A strategy that selectively applies a delegate strategy based on a set
 * filter.
 */
public class FilterStrategy implements StreamStrategy {
    private final StreamStrategy strategy;
    private final byte[] match;
    private final byte[] mask;
    private final boolean skip;
    
    /**
     * Constructs a new FilterStrategy that filters bytes if they match the
     * given pattern after applying the given bit mask.
     * 
     * @param strategy The delegate strategy to use.
     * @param match The matching byte pattern to filter.
     * @param mask The bit mask to apply to bytes read from the stream.
     * @param skip true if matching bytes should be skipped; false if only 
     * matching bytes should be executed on.
     */
    public FilterStrategy(StreamStrategy strategy, byte[] match, byte[] mask,
            boolean skip) {
        if (match.length != mask.length) {
            throw new IllegalArgumentException("Match and mask byte arrays must"
                    + " have the same length.");
        }
        
        this.strategy = strategy;
        this.match = match.clone();
        this.mask = mask.clone();
        this.skip = skip;
    }
    
    /**
     * Filters the given byte stream.
     * 
     * @param stream The byte stream to execute on.
     */
    @Override
    public void execute(ByteStream stream) {
        byte[] temp = stream.readBytes(this.match.length);
        
        // Apply bit mask.
        for (int i = 0; i < this.mask.length; i++) {
            temp[i] &= this.mask[i];
        }
        boolean matched = Arrays.equals(temp, match);
        
        if (matched != skip) {
            stream.advance(-this.match.length);
            this.strategy.execute(stream);
        }
    }
}
