package mmbn;

import rand.Library;

public abstract class ProgramAdvanceProducer extends LibraryProducer<ProgramAdvance> {
    protected final ProgramAdvance.Type[] types;
    
    public ProgramAdvanceProducer(final Library<ProgramAdvance> library, ProgramAdvance.Type[] types) {
        super(library);
        this.types = types;
    }
    
    @Override
    public String getDataName() {
        return "Program Advance";
    }
    
    protected ProgramAdvance.Type typeFromIndex(int index) {
        return this.types[index];
    }
    protected int indexFromType(ProgramAdvance.Type type) {
        for (int i = 0; i < this.types.length; i++) {
            if (this.types[i] == type) {
                return i;
            }
        }
        return -1;
    }
}
