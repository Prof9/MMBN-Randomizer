package mmbn;

import mmbn.BattleChip;

public class Reward {
    public enum Type {
        CHIP,
        ZENNY,
        HP,
        BUGFRAG
    }

    protected Type type;
    protected BattleChip chip;
    protected byte code;
    protected int amount;

    public Reward(BattleChip chip, byte code) {
        this.type = Type.CHIP;
        this.chip = chip;
        this.code = code;
        this.amount = 1;
    }

    public Reward(Type type, int amount) {
        this.type = type;
        this.chip = null;
        this.code = -1;
        this.amount = amount;
    }

    public Type getType() {
        return this.type;
    }
    public void setType(Type type) {
        this.type = type;
    }

    public BattleChip getChip() {
        return this.chip;
    }
    public void setChip(BattleChip chip) {
        this.chip = chip;
    }

    public byte getCode() {
        return this.code;
    }
    public void setCode(byte code) {
        this.code = code;
    }
    
    public int getAmount() {
        return this.amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}