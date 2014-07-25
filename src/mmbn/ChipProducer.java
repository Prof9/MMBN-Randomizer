package mmbn;

import rand.ByteStream;

public abstract class ChipProducer extends LibraryProducer<BattleChip> {
    private final BattleChip.Element[] elements;
    private final BattleChip.Library[] libraries;
    
    public ChipProducer(final ChipLibrary library,
            final BattleChip.Element[] elements,
            final BattleChip.Library[] libraries) {
        super(library);
        this.elements = elements;
        this.libraries = libraries;
    }
    
    @Override
    public String getDataName() {
        return "BattleChip";
    }
    
    protected BattleChip.Element elementFromIndex(byte index) {
        return this.elements[index];
    }
    protected byte indexFromElement(BattleChip.Element element) {
        for (int i = 0; i < this.elements.length; i++) {
            if (this.elements[i] == element) {
                return (byte)i;
            }
        }
        return -1;
    }
    protected BattleChip.Library libraryFromIndex(byte index) {
        return this.libraries[index];
    }
    protected byte indexFromLibrary(BattleChip.Library library) {
        for (int i = 0; i < this.libraries.length; i++) {
            if (this.libraries[i] == library) {
                return (byte)i;
            }
        }
        return -1;
    }
    
    @Override
    protected BattleChip deferredReadFromStream(ByteStream stream) {
        return this.deferredReadFromStream(stream, this.library.size());
    }
    
    protected abstract BattleChip deferredReadFromStream(ByteStream stream, int chipIndex);
}