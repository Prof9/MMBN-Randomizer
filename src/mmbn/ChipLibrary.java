package mmbn;

import rand.Library;
import java.util.ArrayList;
import java.util.List;

/** A library that keeps track of chips. */
public class ChipLibrary extends Library<BattleChip> {
    /**
     * Queries this library for all BattleChips satisfying the given conditions.
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
    public List<BattleChip> query(byte code, int minRarity, int maxRarity,
            BattleChip.Element element, BattleChip.Library library, int minMB,
            int maxMB) {
        ArrayList<BattleChip> result = new ArrayList<>(this.elements.size());
        
        // Search all chips.
        for (int i = 0; i < this.size(); i++) {
            BattleChip chip = this.getElement(i);
            
            int rarity = chip.getRarity();
            int mb = chip.getMB();
            
            if (    // Check valid chip.
                    chip.getIsInLibrary() &&
                    // If no library specified, filter Standard, Mega and Giga.
                    (library != null || (
                    chip.getLibrary() == BattleChip.Library.STANDARD ||
                    chip.getLibrary() == BattleChip.Library.MEGA ||
                    chip.getLibrary() == BattleChip.Library.GIGA)) &&
                    // Avoid chips without codes.
                    (chip.getCodes().length > 0) &&
                    // Check chip codes.
                    (code == -1 || chip.hasCode(code)) &&
                    // Check chip rarity.
                    (rarity >= minRarity && rarity <= maxRarity) &&
                    // Check chip element.
                    (element == null || chip.getElement() == element) &&
                    // Check chip library.
                    (library == null || chip.getLibrary() == library) &&
                    // Check MB.
                    (mb >= minMB && mb <= maxMB)) {
                result.add(chip);
            }
        }
        
        return result;
    }
    
    /**
     * Queries this library for all BattleChips in the given game library.
     * 
     * @param library The game library.
     * @return The chips.
     */
    public List<BattleChip> query(BattleChip.Library library) {
        return query((byte)-1, Integer.MIN_VALUE, Integer.MAX_VALUE, null,
                library, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    /**
     * Queries this library for all chips with the given code.
     * 
     * @param code The code.
     * @return The chips.
     */
    public List<BattleChip> query(byte code) {
        return query(code, Integer.MIN_VALUE, Integer.MAX_VALUE, null, null,
                Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}