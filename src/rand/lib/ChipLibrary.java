package rand.lib;

import java.util.ArrayList;
import java.util.List;
import rand.ByteStream;

/** A library that keeps track of chips. */
public class ChipLibrary extends Library<BattleChip> {    
    @Override
    protected BattleChip loadFromStream(ByteStream stream) {
        return new BattleChip(stream);
    }
    
    /**
     * Queries the library for all indices of chips satisfying the given
     * conditions.
     * 
     * @param code The code the chips must have, or -1 for any code.
     * @param minRarity The minimum rarity of the chips.
     * @param maxRarity The maximum rarity of the chips.
     * @param element The element of the chips, or null for any element.
     * @param library The library of the chips, or null for any library.
     * @param minMB The minimum MB of the chips.
     * @param maxMB The maximum MB of the chips.
     * @return The chips.
     */
    public List<Integer> query(byte code, int minRarity, int maxRarity,
            BattleChip.Element element, BattleChip.Library library, int minMB,
            int maxMB) {
        ArrayList<Integer> result = new ArrayList<>(this.elements.size());
        
        // Search all chips.
        for (int i = 0; i < this.size(); i++) {
            BattleChip chip = this.getElement(i);
            
            int rarity = chip.rarity();
            int mb = chip.getMB();
            
            if (    // Check chip codes.
                    (code == -1 || chip.hasCode(code)) &&
                    // Check chip rarity.
                    (rarity >= minRarity && rarity <= maxRarity) &&
                    // Check chip element.
                    (element == null || chip.element() == element) &&
                    // Check chip library.
                    (library == null || (chip.isInLibrary() &&
                        chip.library() == library)) &&
                    // Check MB.
                    (mb >= minMB && mb <= maxMB)) {
                result.add(i);
            }
        }
        
        return result;
    }
    
    /**
     * Queries the library for all indices of chips in the given game library.
     * 
     * @param library The game library.
     * @return The chips.
     */
    public List<Integer> query(BattleChip.Library library) {
        return query((byte)-1, Integer.MIN_VALUE, Integer.MAX_VALUE, null,
                library, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    /**
     * Queries the library for all indices of chips with the given code.
     * 
     * @param code The code.
     * @return The chips.
     */
    public List<Integer> query(byte code) {
        return query(code, Integer.MIN_VALUE, Integer.MAX_VALUE, null, null,
                Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}