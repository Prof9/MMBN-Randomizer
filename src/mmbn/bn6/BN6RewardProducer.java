package mmbn.bn6;

import mmbn.ChipLibrary;
import mmbn.Item;
import mmbn.ItemProducer;
import rand.ByteConverter;
import rand.ByteStream;

public class BN6RewardProducer extends ItemProducer<Item> {
    public BN6RewardProducer(ChipLibrary library) {
        super(library, new Item.Type[] {
            Item.Type.BATTLECHIP,
            Item.Type.ZENNY,
            Item.Type.HP,
            Item.Type.BUGFRAG
        });
    }

    @Override
    public Item readFromStream(ByteStream stream) {
        byte[] bytes = stream.readBytes(2);
        Item reward = new Item(bytes);
        
        Item.Type type = getItemType(ByteConverter.readBits(bytes, 0, 14, 2));
        int value, subValue;
        if (type == Item.Type.BATTLECHIP) {
            value = ByteConverter.readBits(bytes, 0, 0, 9);
            subValue = ByteConverter.readBits(bytes, 0, 9, 5);
        } else {
            value = ByteConverter.readBits(bytes, 0, 0, 14);
            subValue = 0;
        }
        
        setItem(reward, type, value, subValue);
        return reward;
    }

    @Override
    public void writeToStream(ByteStream stream, Item reward) {
        byte[] base = reward.base();
        
        Item.Type type = reward.type();
        ByteConverter.writeBits(getItemTypeIndex(type), base, 0, 14, 2);
        if (type == Item.Type.BATTLECHIP) {
            ByteConverter.writeBits(reward.value(), base, 0, 0, 9);
            ByteConverter.writeBits(reward.subValue(), base, 0, 9, 5);
        } else {
            ByteConverter.writeBits(reward.value(), base, 0, 0, 14);
        }
        
        stream.writeBytes(base);
    }
}
