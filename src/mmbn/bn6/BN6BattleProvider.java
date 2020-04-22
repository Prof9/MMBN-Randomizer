package mmbn.bn6;

import java.util.Random;
import mmbn.prod.BattleProducer;
import mmbn.types.Battle;
import mmbn.types.BattleObject;
import rand.DataProvider;
import rand.RandomizerContext;

public class BN6BattleProvider extends DataProvider<Battle> {
	private static final byte[][] ENEMY_LEVELS = {
		{ 1, 2, 3, 4, 5, 6 }, // Mettaur
		{ 1, 2, 3, 4, 5, 6 }, // Piranha
		{ 1, 2, 3, 4, 5, 6 }, // Heady
		{ 1, 2, 3, 4, 5, 6 }, // Swordy
		{ 1, 2, 3, 4, 5, 6 }, // KillerEye
		{ 1, 2, 3, 4, 5, 6 }, // Quaker
		{ 1, 2, 3, 4, 5, 6 }, // Catack
		{ 1, 2, 3, 4, 5, 6 }, // Champy
		{ 1, 1, 2, 2, 5, 5 }, // WindBox
		{ 1, 2, 3, 4, 5, 6 }, // Trumpy
		{ 1, 2, 3, 4, 5, 6 }, // OldStove
		{ 1, 2, 3, 4, 5, 6 }, // HauntedCandle
		{-1, 1, 2, 3, 4, 5 }, // Kettle
		{ 1, 2, 3, 4, 5, 6 }, // Puffy
		{ 1, 2, 3, 4, 5, 6 }, // StarFish
		{ 1, 2, 3, 4, 5, 6 }, // EarthDragon
		{ 1, 2, 3, 4, 5, 6 }, // ScareCrow
		{ 1, 2, 3, 4, 5, 6 }, // PulseBulb
		{ 1, 2, 3, 4, 5, 6 }, // BigHat
		{ 1, 2, 3, 4, 5, 6 }, // BombCorn
		{ 1, 2, 3, 4, 5, 6 }, // Shrubby
		{ 1, 2, 3, 4, 5, 6 }, // HoneyBomber
		{ 1, 2, 3, 4, 5, 6 }, // Gunner
		{ 1, 2, 3, 4, 5, 6 }, // FighterPlane
		{ 1, 2, 3, 4, 5, 6 }, // DarkMech
		{ 1, 2, 3, 4, 5, 6 }, // SnakeArm
		{ 1, 2, 3, 4, 5, 6 }, // Armadill
		{ 1, 2, 3, 4, 5, 6 }, // Cragger
		{ 1, 2, 3, 4, 5, 6 }, // Nightmare
	};
	
	private boolean allowNightmares;
	
	public BN6BattleProvider(RandomizerContext context, BattleProducer producer) {
		super(context, producer);
		this.allowNightmares = true;
	}
	
	public boolean getAllowNightmares() {
		return this.allowNightmares;
	}
	
	public void setAllowNightmares(boolean allow) {
		this.allowNightmares = allow;
	}

	@Override
	protected void randomizeData(Random rng, Battle battle, int position) {
		for (int i = 0; i < battle.objectCount(); i++) {
			BattleObject obj = battle.getObject(i);
			int objValue = obj.getValue();

			// If object is an enemy, randomize it.
			if (obj.getType() == BattleObject.Type.ENEMY && obj.getSide() == 1) {
				// Only randomize regular viruses; not bosses.
				if (objValue >= 1 && objValue <= 0xAE) {
					int enemyFamily = (objValue - 1) / 6;
					int enemyRank = (objValue - 1) % 6;
					enemyRank = Math.abs(ENEMY_LEVELS[enemyFamily][enemyRank]);
					
					// Choose a random family.
					if (this.allowNightmares) {
						enemyFamily = rng.nextInt(29);
					} else {
						enemyFamily = rng.nextInt(28);
					}
					
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
