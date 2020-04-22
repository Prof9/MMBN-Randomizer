package mmbn.bn5;

import java.util.Random;
import mmbn.prod.BattleProducer;
import mmbn.types.Battle;
import mmbn.types.BattleObject;
import rand.DataProvider;
import rand.RandomizerContext;

public class BN5BattleProvider extends DataProvider<Battle> {
	private static final byte[][] ENEMY_LEVELS = {
		{ 1, 2, 3, 4, 5, 6 }, // Mettaur
		{ 1, 2, 3, 4, 5, 6 }, // Powie
		{ 1, 2, 3, 4, 5, 6 }, // BugTank
		{ 1, 2, 3, 4, 5, 6 }, // Handi
		{ 1, 2, 3, 4, 5, 6 }, // Dominerd
		{ 1, 2, 3, 4, 5, 6 }, // MetFire
		{ 1, 2, 3, 4, 5, 6 }, // Shakey
		{ 1, 2, 3, 4, 5, 6 }, // Trumpy
		{ 1, 2, 3, 4, 5, 6 }, // NinJoy
		{ 1, 1, 3, 3, 5, 5 }, // WindBox
		{ 1, 2, 3, 4, 5, 6 }, // Lark
		{ 1, 2, 3, 4, 5, 6 }, // BomBoy
		{ 1, 2, 3, 4, 5, 6 }, // Zomon
		{ 1, 2, 3, 4, 5, 6 }, // CannonGuard
		{ 1, 2, 3, 4, 5, 6 }, // Catack
		{ 1, 2, 3, 4, 5, 6 }, // Drixol
		{ 1, 2, 3, 4, 5, 6 }, // Batty
		{ 1, 2, 3, 4, 5, 6 }, // Skarab
		{ 1, 2, 3, 4, 5, 6 }, // Champy
		{ 1, 2, 3, 4, 5, 6 }, // Draggin
		{ 1, 2, 3, 4, 5, 6 }, // Whirly
		{ 1, 2, 3, 4, 5, 6 }, // Marina
		{ 1, 2, 3, 4, 5, 6 }, // Flashy
		{ 1, 2, 3, 4, 5, 6 }, // Eleogre
		{ 1, 2, 3, 4, 5, 6 }, // Cactikil
		{ 1, 2, 3, 4, 5, 6 }, // WuNote
		{ 1, 2, 3, 4, 5, 6 }, // Appley
		{ 1, 2, 3, 4, 5, 6 }, // BigBrute
		{ 1, 2, 3, 4, 5, 6 }, // TinHawk
		{ 1, 2, 3, 4, 5, 6 }, // Bladia
	};
	
	public BN5BattleProvider(RandomizerContext context, BattleProducer producer) {
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
					enemyRank = Math.abs(ENEMY_LEVELS[enemyFamily][enemyRank]);
					
					// Choose a random family.
					enemyFamily = rng.nextInt(27);
					
					// Choose a random rank of the same difficulty.
					byte[] possibleRanks = new byte[6];
					int possibleCount = 0;
					do {
						for (byte j = 0; j < 6; j++) {
							byte possibleRank = ENEMY_LEVELS[enemyFamily][j];
							if (possibleRank == enemyRank) {
								possibleRanks[possibleCount++] = j;
							}
						}
						// Reduce enemy rank if not found.
					} while (possibleCount == 0 && --enemyRank >= 1);
					if (possibleCount == 0) {
						// Should never happen!!!
						continue;
					}
					
					// Choose enemy level.
					enemyRank = possibleRanks[rng.nextInt(possibleCount)];

					objValue = enemyFamily * 6 + enemyRank + 1;
				}
			}

			obj.setValue(objValue);
		}
	}
}
