package mmbn;

public class MysteryDataContents extends Item {
    protected int probability;
    
    public MysteryDataContents(byte[] base) {
        super(base);
        this.probability = 0;
    }
    
    public int getProbability() {
        return this.probability;
    }
    public void setProbability(int probability) {
        this.probability = probability;
    }
}
