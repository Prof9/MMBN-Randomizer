package mmbn;

public class MysteryDataContents {
    public enum Type {
        BATTLECHIP,
        SUBCHIP,
        ZENNY,
        ITEM,
        BUGFRAG,
        BATTLECHIP_TRAP,
        ZENNY_TRAP,
        HP_MEMORY,
        NAVICUST_PROGRAM,
        REGUP,
        SUBMEMORY,
        EXPMEMORY
    }
    
    protected final byte[] base;
    
    protected MysteryDataContents.Type type;
    protected int probability;
    protected int subValue;
    protected int value;
    
    public MysteryDataContents(byte[] base) {
        this.base = base.clone();
        this.probability = 0;
        this.subValue = 0;
        this.value = 0;
    }
    
    public byte[] base() {
        return this.base.clone();
    }
    
    public MysteryDataContents.Type getType() {
        return this.type;
    }
    public void setType(MysteryDataContents.Type type) {
        this.type = type;
    }
    
    public int getProbability() {
        return this.probability;
    }
    public void setProbability(int probability) {
        this.probability = probability;
    }
    
    public int getValue() {
        return this.value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    
    public int getSubValue() {
        return this.subValue;
    }
    public void setSubValue(int subValue) {
        this.subValue = subValue;
    }
}
