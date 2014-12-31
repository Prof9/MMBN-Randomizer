package mmbn;

import rand.ByteStream;
import rand.Bytes;

public class BN5BonusPanelContentsProducer extends BonusPanelContentsProducer {
	public BN5BonusPanelContentsProducer(ChipLibrary chipLibrary) {
		super(chipLibrary, new Item.Type[] {
			Item.Type.ONE_PHASE_INVINCIBILITY,
			Item.Type.HP,
			Item.Type.ORDER_POINTS,
			Item.Type.BATTLECHIP,
			Item.Type.MAJOR_HIT
		});
	}
	
	@Override
	public int getDataSize() {
		return 8;
	}

	@Override
	public MysteryDataContents readFromStream(ByteStream stream) {
		byte[] bytes = stream.readBytes(8);
		MysteryDataContents contents = new MysteryDataContents(bytes);

		Item.Type type = getItemType(Bytes.readUInt8(bytes, 0));
		int value, subValue;
		if (type == Item.Type.BATTLECHIP) {
			value = Bytes.readUInt16(bytes, 2);
			subValue = Bytes.readUInt8(bytes, 1);
		} else {
			value = Bytes.readUInt16(bytes, 4);
			subValue = 0xFF;
		}
		int probability = Bytes.readUInt8(bytes, 6);
		
		setItem(contents, type, value, subValue);
		contents.setProbability(probability);
		return contents;
	}

	@Override
	public void writeToStream(ByteStream stream, MysteryDataContents contents) {
		byte[] bytes = contents.base();

		Bytes.writeUInt8((short) (getItemTypeIndex(contents.type())), bytes, 0);
		if (contents.type() == Item.Type.BATTLECHIP) {
			Bytes.writeUInt16(contents.value(), bytes, 2);
			Bytes.writeUInt8((short)contents.subValue(), bytes, 1);
		} else {
			Bytes.writeUInt16(contents.value(), bytes, 4);
		}
		Bytes.writeUInt8((short)contents.getProbability(), bytes, 6);

		stream.writeBytes(bytes);
	}
    
}
