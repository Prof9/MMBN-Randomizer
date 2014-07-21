package mmbn;

public class ShopItem {
    public enum Type {
        ITEM,
        BATTLECHIP,
        NAVICUST_PROGRAM
    }
    
    protected final byte[] base;
    
    protected ShopItem.Type type;
    protected int amount;
    protected int value;
    protected int subValue;
    protected int price;
    
    public ShopItem(ShopItem.Type type, byte[] base) {
        this.base = base.clone();
        this.type = type;
        this.amount = 0;
        this.value = 0;
        this.subValue = 0;
        this.price = 0;
    }
    
    public byte[] base() {
        return this.base.clone();
    }
    
    public ShopItem.Type getType() {
        return this.type;
    }
    public void setType(ShopItem.Type type) {
        this.type = type;
    }
    
    public int getAmount() {
        return this.amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public int getValue() {
        return this.value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    
    public int getSubValue() {
        return this.subValue;
    }
    public void setSubValue(int subValue) {
        this.subValue = subValue;
    }
    
    public int getPrice() {
        return this.price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
}
