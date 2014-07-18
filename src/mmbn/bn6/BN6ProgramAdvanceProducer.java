package mmbn.bn6;

import java.util.ArrayList;
import java.util.List;
import rand.ByteStream;
import rand.DataProducer;
import rand.Library;
import mmbn.BattleChip;
import mmbn.ProgramAdvance;

public class BN6ProgramAdvanceProducer implements DataProducer<ProgramAdvance> {
    protected final Library<BattleChip> library;
    
    public BN6ProgramAdvanceProducer(final Library<BattleChip> library) {
        this.library = library;
    }
    
    @Override
    public ProgramAdvance readFromStream(ByteStream stream) {
        // Read chip count and PA type.
        int chipCount = stream.readUInt8();
        int typeValue = stream.readUInt8();
        
        // Read PA result.
        BattleChip result = this.library.getElement(stream.readUInt16());
        
        // Check PA type.
        switch (typeValue) {
            case 0:
                BattleChip chip = this.library.getElement(stream.readUInt16());
                return new ProgramAdvance(result, chip, chipCount);
            case 4:
                List<BattleChip> chips = new ArrayList<>(chipCount);
                for (int i = 0; i < chipCount; i++) {
                    chips.add(this.library.getElement(stream.readUInt16()));
                }
                return new ProgramAdvance(result, chips);
            default:
                throw new IllegalArgumentException("Unknown PA type.");
        }
    }

    @Override
    public void writeToStream(ByteStream stream, ProgramAdvance pa) {
        stream.writeUInt8((short)pa.chipCount());
        
        switch (pa.type()) {
            case CONSECUTIVE:
                stream.writeUInt8((short)0);
                break;
            case COMBINATION:
                stream.writeUInt8((short)4);
                break;
            default:
                throw new IllegalArgumentException("Unknown PA type.");
        }
        
        stream.writeUInt16(pa.result().index());
        
        for (BattleChip chip : pa.chips()) {
            stream.writeUInt16(chip.index());
        }
    }
}
