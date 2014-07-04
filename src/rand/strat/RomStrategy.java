package rand.strat;

import rand.Rom;

/**
 * A strategy that acts on a ROM.
 */
public interface RomStrategy {
    /**
     * Executes the implemented strategy on the given ROM.
     * 
     * @param rom The ROM to execute on.
     */
    public void execute(Rom rom);
}
