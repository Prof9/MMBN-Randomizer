package rand;

import mmbn.bn4.BN4RandomizerContext;
import mmbn.bn5.BN5RandomizerContext;
import mmbn.bn6.BN6RandomizerContext;

public class ContextSelector {
	private static final RandomizerContext[] contexts = new RandomizerContext[]{
		//new BN4RandomizerContext(),
		new BN5RandomizerContext(),
		new BN6RandomizerContext(),
	};

	public static RandomizerContext getAppropriateContext(ByteStream rom) {
		String romId = Main.getRomId(rom);
		for (RandomizerContext context : ContextSelector.contexts) {
			String[] supportedIds = context.getSupportedRomIds();
			for (String id : supportedIds) {
				if (romId.equals(id)) {
					return context;
				}
			}
		}
		return null;
	}
}
