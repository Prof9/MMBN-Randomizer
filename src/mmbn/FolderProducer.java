package mmbn;
import rand.ByteStream;
import rand.DataProducer;
import mmbn.ChipLibrary;
import mmbn.Folder;
import mmbn.Reward;

public class FolderProducer implements DataProducer<Folder> {
    protected final RewardProducer rewardProducer;
    
    public FolderProducer(RewardProducer rewardProducer) {
        this.rewardProducer = rewardProducer;
    }
    
    @Override
    public Folder readFromStream(ByteStream stream) {
        Folder folder = new Folder();
        
        while (folder.isFull()) {
            folder.addChip(this.rewardProducer.readFromStream(stream));
        }
        
        return folder;
    }

    @Override
    public void writeToStream(ByteStream stream, Folder folder) {
        for (Reward chip : folder.getChips()) {
            this.rewardProducer.writeToStream(stream, chip);
        }
    }
    
    public ChipLibrary library() {
        return this.rewardProducer.library();
    }
}
