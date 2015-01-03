package mmbn.types;

public class BattleObject {
	public enum Type {
		PLAYER,
		ENEMY,
		MYSTERY_DATA,
		ROCK,
		DS_NAVI,
		METALGEAR,
		WIND,
		FLAG,
		ALLY,
		DESTRUCTIBLE_CUBE,
		GUARDIAN,
		METALCUBE,
		NOTHING
	}

	protected BattleObject.Type type;
	protected byte side;
	protected byte x;
	protected byte y;
	protected int value;

	public BattleObject(BattleObject.Type type) {
		this.type = type;
		this.side = 0;
		this.x = 1;
		this.y = 1;
		this.value = 0;
	}

	public BattleObject.Type getType() {
		return this.type;
	}

	public void setType(BattleObject.Type type) {
		this.type = type;
	}

	public byte getSide() {
		return this.side;
	}

	public void setSide(int side) {
		if (side < 0 || side > 3) {
			throw new IllegalArgumentException("side must be at least 0 and at "
					+ "most 3.");
		}
		this.side = (byte) side;
	}

	public byte getX() {
		return this.x;
	}

	public void setX(int x) {
		if (x < 0 || x > 6) {
			throw new IllegalArgumentException("x must be at least 0 and at "
					+ "most 6.");
		}
		this.x = (byte) x;
	}

	public byte getY() {
		return this.y;
	}

	public void setY(int y) {
		if (y < 0 || y > 3) {
			throw new IllegalArgumentException("y must be at least 0 and at "
					+ "most 3.");
		}
		this.y = (byte) y;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
