package mmbn;

public abstract class BonusPanelContentsProducer extends ItemProducer<MysteryDataContents> {
	public BonusPanelContentsProducer(ChipLibrary chipLibrary, Item.Type[] itemTypes) {
		super(chipLibrary, itemTypes);
	}

	@Override
	public String getDataName() {
		return "Bonus Panel";
	}
}
