package mmbn.bn4;

import java.util.Random;
import mmbn.prod.BattleProducer;
import mmbn.types.Battle;
import mmbn.types.BattleObject;
import rand.DataProvider;
import rand.RandomizerContext;

public class BN4BattleProvider extends DataProvider<Battle>  {
    public BN4BattleProvider(RandomizerContext context, BattleProducer producer) {
		super(context, producer);
	}

	@Override
	protected void randomizeData(Random rng, Battle battle, int position) {
		for (int i = 0; i < battle.objectCount(); i++) {
			BattleObject obj = battle.getObject(i);
			int objValue = obj.getValue();

			// If object is an enemy, randomize it.
			if (obj.getType() == BattleObject.Type.ENEMY) {
				// Only randomize regular viruses; not (mini-)bosses.
				if (objValue >= 1 && objValue <= 0x7E) {
					int enemyFamily = (objValue - 1) / 6;
					int enemyRank = (objValue - 1) % 6;

					// Choose a random family but keep the same rank.
					enemyFamily = rng.nextInt(21);

					objValue = enemyFamily * 6 + enemyRank + 1;
				}
			}

			obj.setValue(objValue);
		}
	}
}
