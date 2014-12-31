package mmbn;

public class BoktaiTraderEntry extends Item {
	protected int ratio;

	public BoktaiTraderEntry(byte[] base) {
		super(base);
		this.ratio = 0;
	}

	public BoktaiTraderEntry(Item item) {
		super(item);
		this.ratio = 0;
	}

	public void setChip(BattleChip chip) {
		this.type = Item.Type.BATTLECHIP;
		this.chip = chip;
		this.value = chip.index();
		this.subValue = -1;
	}

	public int getRatio() {
		return this.ratio;
	}

	public void setRatio(int ratio) {
		if (ratio < 0 || ratio > 16) {
			throw new IllegalArgumentException("Ratio must be at least 0 and at"
					+ " most 16.");
		}
		this.ratio = ratio;
	}
}
