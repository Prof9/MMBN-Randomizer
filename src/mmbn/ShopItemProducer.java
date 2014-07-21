package mmbn;

import rand.DataProducer;

public abstract class ShopItemProducer implements DataProducer<ShopItem> {
    protected final ShopItem.Type[] itemTypes;
    
    public ShopItemProducer(final ShopItem.Type[] itemTypes) {
        this.itemTypes = itemTypes;
    }
    
    protected ShopItem.Type itemTypeFromIndex(int index) {
        return this.itemTypes[index - 1];
    }
    protected int indexFromItemType(ShopItem.Type itemType) {
        for (int i = 0; i < this.itemTypes.length; i++) {
            if (this.itemTypes[i] == itemType) {
                return i + 1;
            }
        }
        return -1;
    }
}
