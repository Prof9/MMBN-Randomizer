package mmbn;

import rand.ByteStream;
import rand.Bytes;

public class BN5ItemPanelContentsProducer extends ItemPanelContentsProducer {
	public BN5ItemPanelContentsProducer(ChipLibrary chipLibrary) {
		super(chipLibrary, new Item.Type[] {
			Item.Type.HP,
			Item.Type.BARRIERKEY,
			Item.Type.ORDER_POINTS,
			Item.Type.ZENNY,
			Item.Type.BUGFRAG,
			Item.Type.BATTLECHIP
		});
	}
	
	@Override
	public int getDataSize() {
		return 8;
	}

	@Override
	public Item readFromStream(ByteStream stream) {
		byte[] bytes = stream.readBytes(8);
		Item item = new Item(bytes);

		Item.Type type = getItemType(Bytes.readUInt8(bytes, 0));
		int value, subValue;
		if (type == Item.Type.BATTLECHIP) {
			value = Bytes.readUInt16(bytes, 2);
			subValue = Bytes.readUInt8(bytes, 1);
		} else {
			value = Bytes.readUInt16(bytes, 4);
			subValue = 0xFF;
		}
		
		setItem(item, type, value, subValue);
		return item;
	}

	@Override
	public void writeToStream(ByteStream stream, Item item) {
		byte[] bytes = item.base();

		Bytes.writeUInt8((short) (getItemTypeIndex(item.type())), bytes, 0);
		if (item.type() == Item.Type.BATTLECHIP) {
			Bytes.writeUInt16(item.value(), bytes, 2);
			Bytes.writeUInt8((short)item.subValue(), bytes, 1);
		} else {
			Bytes.writeUInt16(item.value(), bytes, 4);
		}

		stream.writeBytes(bytes);
	}
}
