package mmbn;

public class NumberCode {
    public enum Type {
        BATTLECHIP,
        ITEM,
        SUBCHIP,
        NAVICUST_PROGRAM
    }
    
    protected NumberCode.Type type;
    protected int value;
    protected int subValue;
    protected int code;
    
    public NumberCode(NumberCode.Type type) {
        this.type = type;
        this.value = 0;
        this.subValue = 0;
        this.code = 0;
    }
    
    public NumberCode.Type getType() {
        return this.type;
    }
    public void setType(NumberCode.Type type) {
        this.type = type;
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
    
    public int getCode() {
        return this.code;
    }
    public void setCode(int code) {
        if (code < 0 || code > 99999999) {
            throw new IllegalArgumentException("Number codes must be at least "
                    + "00000000 and at most 99999999.");
        }
        this.code = code;
    }
}
