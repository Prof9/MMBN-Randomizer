package mmbn.prod;

import mmbn.ChipLibrary;
import mmbn.types.Item;
import mmbn.types.BoktaiTraderEntry;

public abstract class BoktaiTraderEntryProducer extends ItemProducer<BoktaiTraderEntry> {
	public BoktaiTraderEntryProducer(final ChipLibrary chipLibrary,
			final Item.Type[] itemTypes) {
		super(chipLibrary, itemTypes);
	}

	@Override
	public String getDataName() {
		return "Boktai Trader entry";
	}
}
