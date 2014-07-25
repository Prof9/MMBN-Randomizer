package rand;

import java.util.Observable;
import java.util.Observer;
import javax.swing.SwingWorker;

public class RandomizerWorker extends SwingWorker<Void, String> {
    private final RandomizerContext context;
    private final ByteStream rom;
    private final Runnable done;

    public RandomizerWorker(RandomizerContext context, ByteStream rom, Runnable done) {
        this.context = context;
        this.rom = rom;
        this.done = done;
    }

    @Override
    protected Void doInBackground() throws Exception {
        this.context.randomize(rom);
        return null;
    }

    @Override
    protected void done() {
        this.done.run();
    }
}