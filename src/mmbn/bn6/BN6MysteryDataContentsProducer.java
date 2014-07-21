package mmbn.bn6;

import mmbn.ChipLibrary;
import mmbn.MysteryDataContents;
import mmbn.MysteryDataContentsProducer;
import rand.ByteConverter;
import rand.ByteStream;

public class BN6MysteryDataContentsProducer extends MysteryDataContentsProducer {
    public BN6MysteryDataContentsProducer(ChipLibrary library) {
        super(library, new MysteryDataContents.Type[] {
            MysteryDataContents.Type.BATTLECHIP,
            MysteryDataContents.Type.SUBCHIP,
            MysteryDataContents.Type.ZENNY,
            MysteryDataContents.Type.ITEM,
            MysteryDataContents.Type.BUGFRAG,
            MysteryDataContents.Type.BATTLECHIP_TRAP,
            MysteryDataContents.Type.ZENNY_TRAP,
            MysteryDataContents.Type.HP_MEMORY,
            MysteryDataContents.Type.NAVICUST_PROGRAM,
            MysteryDataContents.Type.REGUP,
            MysteryDataContents.Type.SUBMEMORY,
            MysteryDataContents.Type.EXPMEMORY
        });
    }
    
    @Override
    public MysteryDataContents readFromStream(ByteStream stream) {
        byte[] bytes = stream.readBytes(8);
        MysteryDataContents contents = new MysteryDataContents(bytes);
        
        contents.setType(this.contentsTypeFromIndex(ByteConverter.readUInt8(bytes, 0)));
        contents.setProbability(ByteConverter.readUInt8(bytes, 1));
        contents.setSubValue(ByteConverter.readUInt8(bytes, 3));
        contents.setValue(ByteConverter.readInt32(bytes, 4));
        
        return contents;
    }

    @Override
    public void writeToStream(ByteStream stream, MysteryDataContents contents) {
        byte[] bytes = contents.base();
        
        ByteConverter.writeUInt8((short)this.indexFromContentsType(contents.getType()), bytes, 0);
        ByteConverter.writeUInt8((short)contents.getProbability(), bytes, 1);
        ByteConverter.writeUInt8((short)contents.getSubValue(), bytes, 3);
        ByteConverter.writeInt32(contents.getValue(), bytes, 4);
        
        stream.writeBytes(bytes);
    }
}
