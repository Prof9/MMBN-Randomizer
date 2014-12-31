package mmbn;

public abstract class ShopItemProducer extends ItemProducer<ShopItem> {
	public ShopItemProducer(ChipLibrary chipLibrary, Item.Type[] itemTypes) {
		super(chipLibrary, itemTypes);
	}

	@Override
	public String getDataName() {
		return "shop item";
	}
}
