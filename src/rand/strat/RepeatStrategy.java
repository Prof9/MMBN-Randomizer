package rand.strat;

import rand.Rom;
import java.util.Arrays;

/**
 * A strategy that repeatedly applies a delegate strategy to a ROM for a set
 * number of iterations or until the given byte pattern is reached.
 */
public class RepeatStrategy implements RomStrategy {
    private final RomStrategy strategy;
    private final int amount;
    private final byte[] until;
    
    /**
     * Constructs a new RepeatStrategy that executes a set number of iterations.
     * 
     * @param strategy The delegate strategy to use.
     * @param amount The amount of iterations.
     */
    public RepeatStrategy(final RomStrategy strategy, int amount) {
        this(strategy, amount, null);
    }
    
    /**
     * Constructs a new RepeatStrategy that executes until the given byte
     * pattern is reached.
     * 
     * @param strategy The delegate strategy to use.
     * @param until The byte pattern to stop at.
     */
    public RepeatStrategy(final RomStrategy strategy, byte[] until) {
        this(strategy, -1, until);
    }
    
    /**
     * Constructs a new RepeatStrategy that executes a set number of iterations
     * or until the given byte pattern is reached.
     * 
     * @param strategy The delegate strategy to use.
     * @param amount The maximum amount of iterations, or -1 for no maximum.
     * @param until The byte pattern to stop at, or null.
     */
    public RepeatStrategy(final RomStrategy strategy, int amount,
            byte[] until) {
        if (until != null && until.length < 1) {
            throw new IllegalArgumentException("The ending byte pattern cannot "
                    + "be empty.");
        }
        if (until == null && amount < 0) {
            throw new IllegalArgumentException("The given arguments result in "
                    + "an infinite amount of iterations.");
        }
        
        this.strategy = strategy;
        this.amount = amount;
        this.until = until;
    }
    
    /**
     * Gets the maximum amount of iterations this strategy processes, or -1 if
     * there is no maximum.
     * 
     * @return The maximum amount of iterations, or -1.
     */
    public int amount() {
        return this.amount;
    }
    
    /**
     * Gets the byte pattern this strategy stops at, or null if there is no such
     * byte pattern.
     * 
     * @return The ending byte pattern, or null.
     */
    public byte[] until() {
        return this.until;
    }
    
    /**
     * Repeatedly processes the position of the given ROM.
     * 
     * @param rom The ROM to execute on.
     */
    @Override
    public void execute(Rom rom) {
        int i = 0;
        while (true) {
            if (this.amount >= 0 && i >= this.amount) {
                break;
            }
            if (this.until != null) {
                if (Arrays.equals(rom.readBytes(this.until.length),
                        this.until)) {
                    break;
                } else {
                    rom.advance(-this.until.length);
                }
            }
            
            this.strategy.execute(rom);
            i++;
        }
    }
}
