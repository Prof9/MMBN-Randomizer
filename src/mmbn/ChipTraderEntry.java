package mmbn;

public class ChipTraderEntry {
    private BattleChip chip;
    private final byte[] codes;
    
    public ChipTraderEntry(BattleChip chip, final byte[] codes) {
        this.chip = chip;
        this.codes = codes;
    }
    
    public BattleChip getChip() {
        return this.chip;
    }
    public void setChip(BattleChip chip) {
        this.chip = chip;
    }
    
    public byte[] getCodes() {
        byte[] result = new byte[codeCount()];
        System.arraycopy(this.codes, 0, result, 0, result.length);
        return result;
    }
    public void setCodes(byte[] codes) {
        for (int i = 0; i < this.codes.length; i++) {
            this.codes[i] = -1;
        }
        System.arraycopy(codes, 0, this.codes, 0, Math.min(this.codes.length, codes.length));
    }
    
    public int codeCount() {
        int r = 0;
        for (int i = 0; i < this.codes.length && this.codes[i] != -1; i++) {
            r++;
        }
        return r;
    }
    
    public int maxCodeCount() {
        return this.codes.length;
    }
}