package rand.strat;

import rand.ByteStream;

/**
 * A strategy that modifies the position of the ROM relative to its current
 * position.
 */
public class OffsetStrategy implements StreamStrategy {
    private final StreamStrategy strategy;
    private final int offset;
    
    /**
     * Constructs a new OffsetStrategy that advances the position of the ROM by
     * the given amount, then executes the given strategy.
     * 
     * @param strategy The delegate strategy to use.
     * @param offset The amount to advance the position of the ROM.
     */
    public OffsetStrategy(final StreamStrategy strategy, int offset)
    {
        this.strategy = strategy;
        this.offset = offset;
    }
    
    /**
     * Process the given byte stream.
     * 
     * @param stream The byte stream to execute on.
     */
    @Override
    public void execute(ByteStream stream) {
        stream.advance(this.offset);
        this.strategy.execute(stream);
    }
}
