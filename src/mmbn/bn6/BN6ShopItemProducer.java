package mmbn.bn6;

import mmbn.ChipLibrary;
import mmbn.Item;
import mmbn.ItemProducer;
import mmbn.ShopItem;
import rand.ByteConverter;
import rand.ByteStream;

public class BN6ShopItemProducer extends ItemProducer<ShopItem> {
    public BN6ShopItemProducer(final ChipLibrary chipLibrary) {
        super(chipLibrary, new Item.Type[] {
            Item.Type.ITEM,
            Item.Type.BATTLECHIP,
            Item.Type.NAVICUST_PROGRAM
        });
    }
    
    @Override
    public ShopItem readFromStream(ByteStream stream) {
        byte[] bytes = stream.readBytes(8);
        ShopItem item = new ShopItem(bytes);
        
        Item.Type type = getItemType(ByteConverter.readUInt8(bytes, 0) - 1);
        int stock = ByteConverter.readUInt8(bytes, 1);
        int value = ByteConverter.readUInt16(bytes, 2);
        int subValue = ByteConverter.readUInt8(bytes, 4);
        int price = ByteConverter.readUInt16(bytes, 6);
        
        setItem(item, type, value, subValue);
        item.setStock(stock);
        item.setPrice(price);
        
        return item;
    }

    @Override
    public void writeToStream(ByteStream stream, ShopItem item) {
        byte[] bytes = item.base();
        
        int itemTypeIndex = getItemTypeIndex(item.type()) + 1;
        ByteConverter.writeUInt8((short)itemTypeIndex, bytes, 0);
        ByteConverter.writeUInt8((short)item.getStock(), bytes, 1);
        ByteConverter.writeUInt16(item.value(), bytes, 2);
        ByteConverter.writeUInt8((short)item.subValue(), bytes, 4);
        ByteConverter.writeUInt16(item.getPrice(), bytes, 6);
        
        stream.writeBytes(bytes);
    }
}
