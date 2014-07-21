package mmbn;

import rand.DataProducer;

public abstract class MysteryDataContentsProducer implements DataProducer<MysteryDataContents> {
    protected final ChipLibrary library;
    protected final MysteryDataContents.Type[] contentsTypes;
    
    public MysteryDataContentsProducer(ChipLibrary library, MysteryDataContents.Type[] contentsTypes) {
        this.library = library;
        this.contentsTypes = contentsTypes;
    }
    
    public ChipLibrary library() {
        return this.library;
    }
    
    protected MysteryDataContents.Type contentsTypeFromIndex(int index) {
        return this.contentsTypes[index - 1];
    }
    protected int indexFromContentsType(MysteryDataContents.Type contentsType) {
        for (int i = 0; i < this.contentsTypes.length; i++) {
            if (this.contentsTypes[i] == contentsType) {
                return i + 1;
            }
        }
        return -1;
    }
}
