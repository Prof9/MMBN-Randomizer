package mmbn.prod;

import mmbn.ChipLibrary;
import mmbn.types.Item;

public abstract class ItemPanelContentsProducer extends ItemProducer<Item> {
	public ItemPanelContentsProducer(ChipLibrary chipLibrary, Item.Type[] itemTypes) {
		super(chipLibrary, itemTypes);
	}

	@Override
	public String getDataName() {
		return "Item Panel";
	}
}
