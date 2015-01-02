package mmbn.multi;

import mmbn.types.BattleChip;
import mmbn.types.ProgramAdvance;
import rand.Library;

public class BN56ProgramAdvanceProducer extends AbstractBN456ProgramAdvanceProducer {
	public BN56ProgramAdvanceProducer(final Library<ProgramAdvance> paLibrary,
			final Library<BattleChip> chipLibrary) {
		super(paLibrary, chipLibrary, new ProgramAdvance.Type[]{
			ProgramAdvance.Type.CONSECUTIVE,
			null,
			null,
			null,
			ProgramAdvance.Type.COMBINATION
		});
	}
}