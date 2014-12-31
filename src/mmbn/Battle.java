package mmbn;

import java.util.ArrayList;
import java.util.List;

public class Battle {
	protected final List<BattleObject> objects;

	protected final byte[] base;
	protected int originalObjectCount;

	public Battle(final byte[] base) {
		this.base = base;
		this.objects = new ArrayList<>();
		this.originalObjectCount = Integer.MAX_VALUE;
	}

	public byte[] base() {
		return this.base;
	}

	public int objectCount() {
		return this.objects.size();
	}

	public int originalObjectCount() {
		return this.originalObjectCount;
	}

	public List<BattleObject> getObjects() {
		return new ArrayList<>(this.objects);
	}

	public void setObjects(List<BattleObject> objects) {
		if (objects.size() > this.originalObjectCount) {
			throw new IllegalStateException("This battle can only contain "
					+ this.originalObjectCount + " objects at most.");
		}
		this.objects.clear();
		this.objects.addAll(objects);
	}

	public BattleObject getObject(int index) {
		return this.objects.get(index);
	}

	public void setObject(int index, BattleObject object) {
		this.objects.set(index, object);
	}

	public void addObject(BattleObject object) {
		if (this.objects.size() >= this.originalObjectCount) {
			throw new IllegalStateException("This battle already contains the "
					+ "maximum amount of objects.");
		}
		this.objects.add(object);
	}

	public void lockObjectListMax() {
		if (this.originalObjectCount < Integer.MAX_VALUE) {
			throw new IllegalStateException("This battle's object list has "
					+ "already been locked.");
		}
		this.originalObjectCount = this.objects.size();
	}
}
