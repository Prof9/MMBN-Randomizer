package rand.strat;

import rand.Rom;

/**
 * A strategy that modifies the position of the ROM relative to its current
 * position.
 */
public class OffsetStrategy implements RomStrategy {
    private final RomStrategy strategy;
    private final int offset;
    
    /**
     * Constructs a new OffsetStrategy that advances the position of the ROM by
     * the given amount, then executes the given strategy.
     * 
     * @param strategy The delegate strategy to use.
     * @param offset The amount to advance the position of the ROM.
     */
    public OffsetStrategy(final RomStrategy strategy, int offset)
    {
        this.strategy = strategy;
        this.offset = offset;
    }
    
    /**
     * Process the given ROM.
     * 
     * @param rom The ROM to execute on.
     */
    @Override
    public void execute(Rom rom) {
        rom.advance(this.offset);
        this.strategy.execute(rom);
    }
}
