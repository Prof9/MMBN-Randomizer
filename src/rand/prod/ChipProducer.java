package rand.prod;

import rand.ByteStream;
import rand.DataProducer;
import rand.lib.ChipLibrary;
import rand.lib.Library;
import rand.obj.BattleChip;

public abstract class ChipProducer implements DataProducer<BattleChip> {
    private final BattleChip.Element[] elements;
    private final BattleChip.Library[] libraries;
    private final Library<BattleChip> library;
    
    public ChipProducer(final ChipLibrary library,
            final BattleChip.Element[] elements,
            final BattleChip.Library[] libraries) {
        this.library = library;
        this.elements = elements;
        this.libraries = libraries;
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
    public final BattleChip readFromStream(ByteStream stream) {
        int ptr = stream.getPosition();
        BattleChip chip = deferredReadFromStream(stream, this.library.size());
        this.library.addElement(ptr, chip);
        return chip;
    }
    
    protected abstract BattleChip deferredReadFromStream(ByteStream stream,
            int chipIndex);
}