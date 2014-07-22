package mmbn;

public class ShopItem extends Item {
    protected int stock;
    protected int price;
    
    public ShopItem(byte[] base) {
        super(base);
        this.stock = 0;
        this.price = 0;
    }
    
    public int getStock() {
        return this.stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    public int getPrice() {
        return this.price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
}
