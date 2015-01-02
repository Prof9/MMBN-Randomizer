package mmbn.prod;

import mmbn.types.BattleObject;
import rand.DataProducer;

public abstract class BattleObjectProducer implements DataProducer<BattleObject> {
	private final BattleObject.Type[] objectTypes;

	public BattleObjectProducer(final BattleObject.Type[] objectTypes) {
		this.objectTypes = objectTypes;
	}

	@Override
	public String getDataName() {
		return "battle object";
	}

	public BattleObject.Type objectTypeFromIndex(int index) {
		return this.objectTypes[index];
	}

	public int indexFromObjectType(BattleObject.Type objectType) {
		for (int i = 0; i < this.objectTypes.length; i++) {
			if (this.objectTypes[i] == objectType) {
				return i;
			}
		}
		return -1;
	}
}
