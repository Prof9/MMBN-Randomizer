package mmbn.prod;

import mmbn.ChipLibrary;
import mmbn.types.Item;
import mmbn.types.MysteryDataContents;

public abstract class MysteryDataContentsProducer extends ItemProducer<MysteryDataContents> {
	public MysteryDataContentsProducer(ChipLibrary chipLibrary, Item.Type[] itemTypes) {
		super(chipLibrary, itemTypes);
	}

	@Override
	public String getDataName() {
		return "Mystery Data";
	}
}
