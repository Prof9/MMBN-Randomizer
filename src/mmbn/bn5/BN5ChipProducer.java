package mmbn.bn5;

import mmbn.ChipLibrary;
import mmbn.bn6.AbstractBN456ChipProducer;
import mmbn.types.BattleChip;

public class BN5ChipProducer extends AbstractBN456ChipProducer {
	public BN5ChipProducer(ChipLibrary library) {
		super(library, new BattleChip.Element[]{
			BattleChip.Element.HEAT,
			BattleChip.Element.AQUA,
			BattleChip.Element.ELEC,
			BattleChip.Element.WOOD,
			BattleChip.Element.RECOVERY,
			BattleChip.Element.BONUS,
			BattleChip.Element.SWORD,
			BattleChip.Element.INVISIBLE,
			BattleChip.Element.CURSOR,
			BattleChip.Element.SUMMON,
			BattleChip.Element.WIND,
			BattleChip.Element.BREAK,
			BattleChip.Element.NULL,
			BattleChip.Element.PA,
			BattleChip.Element.UNUSED
		}, new BattleChip.Library[]{
			BattleChip.Library.STANDARD,
			BattleChip.Library.MEGA,
			BattleChip.Library.GIGA,
			BattleChip.Library.NONE,
			BattleChip.Library.PA
		});
	}
}
