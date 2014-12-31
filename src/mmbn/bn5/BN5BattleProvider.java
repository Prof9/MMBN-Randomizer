package mmbn.bn5;

import mmbn.BN56BattleProducer;
import java.util.Random;
import mmbn.Battle;
import mmbn.BattleObject;
import rand.DataProvider;
import rand.RandomizerContext;

public class BN5BattleProvider extends DataProvider<Battle> {
	public BN5BattleProvider(RandomizerContext context, BN56BattleProducer producer) {
		super(context, producer);
	}

	@Override
	protected void randomizeData(Random rng, Battle battle, int position) {
		for (int i = 0; i < battle.objectCount(); i++) {
			BattleObject obj = battle.getObject(i);
			int objValue = obj.getValue();

			// If object is an enemy, randomize it.
			if (obj.getType() == BattleObject.Type.ENEMY && obj.getSide() == 1) {
				// Only randomize regular viruses; not (mini-)bosses.
				if (objValue >= 1 && objValue <= 0xA2) {
					int enemyFamily = (objValue - 1) / 6;
					int enemyRank = (objValue - 1) % 6;

					// Choose a random family but keep the same rank.
					enemyFamily = rng.nextInt(27);

					objValue = enemyFamily * 6 + enemyRank + 1;
				}
			}

			obj.setValue(objValue);
		}
	}
}
