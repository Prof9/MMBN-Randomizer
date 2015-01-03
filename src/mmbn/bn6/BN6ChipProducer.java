package mmbn.bn6;

import mmbn.types.BattleChip;
import mmbn.ChipLibrary;

public class BN6ChipProducer extends AbstractBN56ChipProducer {
	public BN6ChipProducer(ChipLibrary library) {
		super(library, new BattleChip.Element[] {
			BattleChip.Element.HEAT,
			BattleChip.Element.AQUA,
			BattleChip.Element.ELEC,
			BattleChip.Element.WOOD,
			BattleChip.Element.BONUS,
			BattleChip.Element.SWORD,
			BattleChip.Element.CURSOR,
			BattleChip.Element.SUMMON,
			BattleChip.Element.WIND,
			BattleChip.Element.BREAK,
			BattleChip.Element.NULL,
			BattleChip.Element.PA,
			BattleChip.Element.UNUSED
		}, new BattleChip.Library[] {
			BattleChip.Library.STANDARD,
			BattleChip.Library.MEGA,
			BattleChip.Library.GIGA,
			BattleChip.Library.NONE,
			BattleChip.Library.PA
		});
	}
}
