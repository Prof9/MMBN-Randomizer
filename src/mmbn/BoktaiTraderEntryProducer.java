package mmbn;

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
