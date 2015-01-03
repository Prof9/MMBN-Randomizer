package mmbn.bn4;

import mmbn.multi.AbstractBN456ProgramAdvanceProducer;
import mmbn.types.BattleChip;
import mmbn.types.ProgramAdvance;
import rand.Library;

public class BN4ProgramAdvanceProducer extends AbstractBN456ProgramAdvanceProducer {
	public BN4ProgramAdvanceProducer(final Library<ProgramAdvance> paLibrary,
			final Library<BattleChip> chipLibrary) {
		super(paLibrary, chipLibrary, new ProgramAdvance.Type[]{
			ProgramAdvance.Type.CONSECUTIVE,
			ProgramAdvance.Type.COMBINATION
		});
	}
}