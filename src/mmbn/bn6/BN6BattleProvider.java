package mmbn.bn6;

import java.util.Random;
import mmbn.Battle;
import mmbn.BattleObject;
import rand.DataProvider;
import rand.RandomizerContext;

public class BN6BattleProvider extends DataProvider<Battle> {
    public BN6BattleProvider(RandomizerContext context, BN6BattleProducer producer) {
        super(context, producer);
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

                    // Do not randomize Rare viruses.
                    if (enemyRank <= 3) {
                        // Choose a random family but keep the same rank.
                        enemyFamily = rng.nextInt(29);
                    }

                    objValue = enemyFamily * 6 + enemyRank + 1;
                }
            }
            
            obj.setValue(objValue);
        }
    }
}
