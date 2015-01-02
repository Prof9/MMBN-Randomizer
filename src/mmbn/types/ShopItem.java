package mmbn.types;

public class ShopItem extends Item {
	protected int stock;
	protected int price;

	public ShopItem(byte[] base) {
		super(base);
		this.stock = 0;
		this.price = 0;
	}

	public ShopItem(ShopItem shopItem) {
		super(shopItem);
		this.stock = shopItem.stock;
		this.price = shopItem.price;
	}

	public void setItem(Item item) {
		this.type = item.type;
		this.value = item.value;
		this.subValue = item.subValue;
		this.chip = item.chip;
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
