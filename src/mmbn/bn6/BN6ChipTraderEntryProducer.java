package mmbn.bn6;

import rand.ByteStream;
import rand.DataProducer;
import rand.Library;
import mmbn.BattleChip;
import mmbn.ChipTraderEntry;

public class BN6ChipTraderEntryProducer implements DataProducer<ChipTraderEntry> {
    protected final Library<BattleChip> library;
    
    public BN6ChipTraderEntryProducer(final Library<BattleChip> library) {
        this.library = library;
    }
    
    @Override
    public ChipTraderEntry readFromStream(ByteStream stream) {
        BattleChip chip = this.library.getElement(stream.readUInt16());
        byte[] codes = stream.readBytes(4);
        
        return new ChipTraderEntry(chip, codes);
    }

    @Override
    public void writeToStream(ByteStream stream, ChipTraderEntry entry) {
        stream.writeUInt16(entry.getChip().index());
        stream.writeBytes(entry.getCodes());
    }
}
