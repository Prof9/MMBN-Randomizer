package rand;

import mmbn.bn6.BN6RandomizerContext;

public class ContextSelector {
    private static final RandomizerContext[] contexts = new RandomizerContext[] {
        new BN6RandomizerContext()
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
