package mmbn.multi;

import mmbn.ChipLibrary;
import mmbn.prod.RewardProducer;
import mmbn.types.Item;
import rand.ByteStream;
import rand.Bytes;

public class BN456RewardProducer extends RewardProducer {
	public BN456RewardProducer(ChipLibrary library) {
		super(library, new Item.Type[]{
			Item.Type.BATTLECHIP,
			Item.Type.ZENNY,
			Item.Type.HP,
			Item.Type.BUGFRAG
		});
	}

	@Override
	public int getDataSize() {
		return 2;
	}

	@Override
	public Item readFromStream(ByteStream stream) {
		byte[] bytes = stream.readBytes(2);
		Item reward = new Item(bytes);

		Item.Type type = getItemType(Bytes.readBits(bytes, 0, 14, 2));
		int value, subValue;
		if (type == Item.Type.BATTLECHIP) {
			value = Bytes.readBits(bytes, 0, 0, 9);
			subValue = Bytes.readBits(bytes, 0, 9, 5);
		} else {
			value = Bytes.readBits(bytes, 0, 0, 14);
			subValue = 0;
		}

		setItem(reward, type, value, subValue);
		return reward;
	}

	@Override
	public void writeToStream(ByteStream stream, Item reward) {
		byte[] base = reward.base();

		Item.Type type = reward.type();
		Bytes.writeBits(getItemTypeIndex(type), base, 0, 14, 2);
		if (type == Item.Type.BATTLECHIP) {
			Bytes.writeBits(reward.value(), base, 0, 0, 9);
			Bytes.writeBits(reward.subValue(), base, 0, 9, 5);
		} else {
			Bytes.writeBits(reward.value(), base, 0, 0, 14);
		}

		stream.writeBytes(base);
	}
}
