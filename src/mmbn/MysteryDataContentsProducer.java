package mmbn;

public abstract class MysteryDataContentsProducer extends ItemProducer<MysteryDataContents> {
    public MysteryDataContentsProducer(ChipLibrary chipLibrary, Item.Type[] itemTypes) {
        super(chipLibrary, itemTypes);
    }
    
    @Override
    public String getDataName() {
        return "Mystery Data";
    }
}
