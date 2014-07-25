package mmbn;
import rand.ByteStream;
import rand.DataProducer;

public class FolderProducer implements DataProducer<Folder> {
    protected final ItemProducer<Item> itemProducer;
    
    public FolderProducer(ItemProducer<Item> itemProducer) {
        this.itemProducer = itemProducer;
    }
    
    @Override
    public String getDataName() {
        return "Folder";
    }
    
    @Override
    public int getDataSize() {
        return 30 * this.itemProducer.getDataSize();
    }
    
    @Override
    public Folder readFromStream(ByteStream stream) {
        Folder folder = new Folder();
        
        while (!folder.isFull()) {
            folder.addChip(this.itemProducer.readFromStream(stream));
        }
        
        return folder;
    }

    @Override
    public void writeToStream(ByteStream stream, Folder folder) {
        for (Item chip : folder.getChips()) {
            this.itemProducer.writeToStream(stream, chip);
        }
    }
    
    public ChipLibrary chipLibrary() {
        return this.itemProducer.chipLibrary();
    }
}
