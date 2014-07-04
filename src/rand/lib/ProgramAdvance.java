package rand.lib;

import rand.Rom;

/** Represents a Program Advance. */
public final class ProgramAdvance {
    public enum Type {
        CONSECUTIVE,
        COMBINATION
    }
    
    private final ProgramAdvance.Type type;
    private final int chipCount;
    private final int[] chips;
    private final int result;
    
    /**
     * Loads a Program Advance from the given ROM.
     * 
     * @param rom The ROM to load from.
     */
    public ProgramAdvance(Rom rom) {
        // Read chip count and PA type.
        this.chipCount = rom.readUInt8();
        int typeValue = rom.readUInt8();
        
        // Check PA type.
        int loadCount;
        if (typeValue == 0) {
            this.type = ProgramAdvance.Type.CONSECUTIVE;
            loadCount = 1;
        } else if (typeValue == 4) {
            this.type = ProgramAdvance.Type.COMBINATION;
            loadCount = this.chipCount;
        } else {
            throw new IllegalStateException("Unknown PA type.");
        }
        
        // Read PA result.
        this.result = rom.readUInt16();
        
        // Read required chips.
        this.chips = new int[loadCount];
        for (int i = 0; i < this.chips.length; i++) {
            this.chips[i] = rom.readUInt16();
        }
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
    public int result() {
        return this.result;
    }
    
    /**
     * Gets the chips required to form this Program Advance. For consecutive
     * codes PAs, one chip is returned.
     * 
     * @return The required chips.
     */
    public int[] chips() {
        return this.chips;
    }
}
