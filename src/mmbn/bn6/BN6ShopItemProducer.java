package mmbn.bn6;

import mmbn.ShopItem;
import mmbn.ShopItemProducer;
import rand.ByteConverter;
import rand.ByteStream;

public class BN6ShopItemProducer extends ShopItemProducer {
    public BN6ShopItemProducer() {
        super(new ShopItem.Type[] {
            ShopItem.Type.ITEM,
            ShopItem.Type.BATTLECHIP,
            ShopItem.Type.NAVICUST_PROGRAM
        });
    }
    
    @Override
    public ShopItem readFromStream(ByteStream stream) {
        byte[] bytes = stream.readBytes(8);
        
        ShopItem.Type itemType = itemTypeFromIndex(ByteConverter.readUInt8(bytes, 0));
        ShopItem item = new ShopItem(itemType, bytes);
        
        item.setAmount(ByteConverter.readUInt8(bytes, 1));
        item.setValue(ByteConverter.readUInt16(bytes, 2));
        item.setSubValue(ByteConverter.readUInt8(bytes, 4));
        item.setPrice(ByteConverter.readUInt16(bytes, 6));
        
        return item;
    }

    @Override
    public void writeToStream(ByteStream stream, ShopItem item) {
        byte[] bytes = item.base();
        
        int itemTypeIndex = indexFromItemType(item.getType());
        ByteConverter.writeUInt8((short)itemTypeIndex, bytes, 0);
        
        ByteConverter.writeUInt8((short)item.getAmount(), bytes, 1);
        ByteConverter.writeUInt16(item.getValue(), bytes, 2);
        ByteConverter.writeUInt8((short)item.getSubValue(), bytes, 4);
        ByteConverter.writeUInt16(item.getPrice(), bytes, 6);
        
        stream.writeBytes(bytes);
    }
}
