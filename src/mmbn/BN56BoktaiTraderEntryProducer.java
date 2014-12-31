package mmbn;

import rand.ByteStream;
import rand.Bytes;

public class BN56BoktaiTraderEntryProducer extends BoktaiTraderEntryProducer {
	public BN56BoktaiTraderEntryProducer(ChipLibrary chipLibrary) {
		super(chipLibrary, new Item.Type[] {
            Item.Type.BATTLECHIP,
            Item.Type.SUBCHIP,
            Item.Type.BUGFRAG
        });
	}
	
	@Override
	public int getDataSize() {
		return 12;
	}

	@Override
	public BoktaiTraderEntry readFromStream(ByteStream stream) {
		byte[] bytes = stream.readBytes(12);
		BoktaiTraderEntry entry = new BoktaiTraderEntry(bytes);
		
		Item.Type type = getItemType(Bytes.readUInt8(bytes, 0));
		int value = Bytes.readUInt16(bytes, 2);
		int ratio = Bytes.readUInt8(bytes, 4);
		
		setItem(entry, type, value, -1);
		entry.setRatio(ratio);
		
		return entry;
	}

	@Override
	public void writeToStream(ByteStream stream, BoktaiTraderEntry entry) {
		byte[] bytes = entry.base();
        
        Bytes.writeUInt8((short)getItemTypeIndex(entry.type()), bytes, 0);
        Bytes.writeUInt16(entry.value(), bytes, 2);
        Bytes.writeUInt8((short)entry.ratio, bytes, 4);
        
        stream.writeBytes(bytes);
	}
    
}
