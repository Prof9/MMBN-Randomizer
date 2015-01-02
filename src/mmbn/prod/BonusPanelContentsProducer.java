package mmbn.prod;

import mmbn.ChipLibrary;
import mmbn.types.Item;
import mmbn.types.MysteryDataContents;

public abstract class BonusPanelContentsProducer extends ItemProducer<MysteryDataContents> {
	public BonusPanelContentsProducer(ChipLibrary chipLibrary, Item.Type[] itemTypes) {
		super(chipLibrary, itemTypes);
	}

	@Override
	public String getDataName() {
		return "Bonus Panel";
	}
}
