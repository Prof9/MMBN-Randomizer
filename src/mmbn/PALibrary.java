package mmbn;

import rand.Library;
import java.util.ArrayList;
import java.util.List;

/** A library that keeps track of Program Advances. */
public class PALibrary extends Library<ProgramAdvance> {    
    public List<ProgramAdvance> query(ProgramAdvance.Type type) {
        ArrayList<ProgramAdvance> result = new ArrayList<>(this.elements.size());
        
        // Search all program advances.
        for (int i = 0; i < this.size(); i++) {
            ProgramAdvance pa = this.getElement(i);
            
            // If the PA type matches the given type, add it to the result list.
            if (pa.type() == type) {
                result.add(pa);
            }
        }
        
        return result;
    }
    
    public List<ProgramAdvance> queryComponent(BattleChip chip) {
        ArrayList<ProgramAdvance> result = new ArrayList<>(this.elements.size());
        
        // Search all Program Advances.
        for (int i = 0; i < this.size(); i++) {
            ProgramAdvance pa = this.getElement(i);
            
            // Check the components of the Program Advance.
            for (BattleChip component : pa.chips()) {
                // If match found, add to results and break.
                if (chip.index() == component.index()) {
                    result.add(pa);
                    break;
                }
            }
        }
        
        return result;
    }
}
