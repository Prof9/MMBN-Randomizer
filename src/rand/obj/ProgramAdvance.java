package rand.obj;

import java.util.ArrayList;
import java.util.List;

/** Represents a Program Advance. */
public class ProgramAdvance {
    private static final int MAX_CHIP_COUNT = 5;
    
    public enum Type {
        CONSECUTIVE,
        COMBINATION
    }
    
    private final ProgramAdvance.Type type;
    private final BattleChip result;
    private final List<BattleChip> chips;
    private final int chipCount;
    
    /**
     * Constructs a new Program Advance formed by the given combination of chips
     * with the same code.
     * 
     * @param result The resulting chip.
     * @param chips The component chips.
     */
    public ProgramAdvance(BattleChip result, List<BattleChip> chips) {
        this.type = ProgramAdvance.Type.COMBINATION;
        this.result = result;
        this.chips = new ArrayList<>(MAX_CHIP_COUNT);
        this.chips.addAll(chips);
        this.chipCount = chips.size();
    }
    
    /**
     * Constructs a new Program Advance formed by the given number of
     * consecutive codes of the same chip.
     * 
     * @param result The resulting chip.
     * @param chip The component chip.
     * @param chipCount The amount of component chips.
     */
    public ProgramAdvance(BattleChip result, BattleChip chip, int chipCount) {
        this.type = ProgramAdvance.Type.CONSECUTIVE;
        this.result = result;
        this.chips = new ArrayList<>(MAX_CHIP_COUNT);
        this.chips.add(chip);
        this.chipCount = chipCount;
    }
    
    /**
     * Gets this Program Advance's type.
     * 
     * @return The PA type.
     */
    public ProgramAdvance.Type type() {
        return this.type;
    }
    
    /**
     * Gets the amount of chips required to form this Program Advance.
     * 
     * @return The amount of chips.
     */
    public int chipCount() {
        return this.chipCount;
    }
    
    /**
     * Gets the resulting chip of this Program Advance.
     * 
     * @return The resulting chip.
     */
    public BattleChip result() {
        return this.result;
    }
    
    /**
     * Gets the chips required to form this Program Advance. For consecutive
     * codes PAs, one chip is returned.
     * 
     * @return The required chips.
     */
    public List<BattleChip> chips() {
        return this.chips;
    }
}
