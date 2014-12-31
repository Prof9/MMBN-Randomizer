package mmbn;

import rand.ByteStream;
import rand.Bytes;

public class BN56ShopItemProducer extends ShopItemProducer {
	public BN56ShopItemProducer(final ChipLibrary chipLibrary) {
		super(chipLibrary, new Item.Type[]{
			Item.Type.ITEM,
			Item.Type.BATTLECHIP,
			Item.Type.NAVICUST_PROGRAM
		});
	}

	@Override
	public int getDataSize() {
		return 8;
	}

	@Override
	public ShopItem readFromStream(ByteStream stream) {
		byte[] bytes = stream.readBytes(8);
		ShopItem item = new ShopItem(bytes);

		Item.Type type = getItemType(Bytes.readUInt8(bytes, 0) - 1);
		int stock = Bytes.readUInt8(bytes, 1);
		int value = Bytes.readUInt16(bytes, 2);
		int subValue = Bytes.readUInt8(bytes, 4);
		int price = Bytes.readUInt16(bytes, 6);

		setItem(item, type, value, subValue);
		item.setStock(stock);
		item.setPrice(price);

		return item;
	}

	@Override
	public void writeToStream(ByteStream stream, ShopItem item) {
		byte[] bytes = item.base();

		int itemTypeIndex = getItemTypeIndex(item.type()) + 1;
		Bytes.writeUInt8((short) itemTypeIndex, bytes, 0);
		Bytes.writeUInt8((short) item.getStock(), bytes, 1);
		Bytes.writeUInt16(item.value(), bytes, 2);

		if (item.isChip() || item.isProgram()) {
			Bytes.writeUInt8((short) item.subValue(), bytes, 4);
		} else {
			Bytes.writeUInt8((short) 0xFF, bytes, 4);
		}

		Bytes.writeUInt16(item.getPrice(), bytes, 6);

		stream.writeBytes(bytes);
	}
}
