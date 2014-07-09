package rand.lib;

import java.util.ArrayList;
import java.util.List;
import rand.ByteStream;

/** A library that keeps track of Program Advances. */
public class PALibrary extends Library<ProgramAdvance> {
    @Override
    public ProgramAdvance loadFromStream(ByteStream stream) {
        return new ProgramAdvance(stream);
    }
    
    public List<Integer> query(ProgramAdvance.Type type) {
        ArrayList<Integer> result = new ArrayList<>(this.elements.size());
        
        // Search all program advances.
        for (int i = 0; i < this.size(); i++) {
            ProgramAdvance pa = this.getElement(i);
            
            // If the PA type matches the given type, add it to the result list.
            if (pa.type() == type) {
                result.add(i);
            }
        }
        
        return result;
    }
    
    public List<Integer> queryComponent(int chip) {
        ArrayList<Integer> result = new ArrayList<>(this.elements.size());
        
        // Search all Program Advances.
        for (int i = 0; i < this.size(); i++) {
            ProgramAdvance pa = this.getElement(i);
            
            // Check the components of the Program Advance.
            for (int component : pa.chips()) {
                // If match found, add to results and break.
                if (chip == component) {
                    result.add(i);
                    break;
                }
            }
        }
        
        return result;
    }
}
