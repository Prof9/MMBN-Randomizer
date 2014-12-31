package mmbn;

public class NumberCode extends Item {
    protected int code;

    public NumberCode(byte[] base) {
        super(base);
		this.code = 0;
    }
	public NumberCode(Item item) {
		super(item);
		this.code = 0;
	}
    
    public int getNumberCode() {
        return this.code;
    }
    public void setNumberCode(int code) {
        if (code < 0 || code > 99999999) {
            throw new IllegalArgumentException("Number codes must be at least "
                    + "00000000 and at most 99999999.");
        }
        this.code = code;
    }
}
