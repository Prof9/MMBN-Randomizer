package rand;

import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

public class RandomizerWorker extends SwingWorker<Void, String> {
	private final RandomizerContext context;
	private final ByteStream rom;
	private Runnable done;
	private Throwable thrown;

	public RandomizerWorker(RandomizerContext context, ByteStream rom) {
		this.context = context;
		this.rom = rom;
		this.done = null;
		this.thrown = null;
	}

	public void setDone(Runnable done) {
		this.done = done;
	}

	public Throwable getThrown() {
		return this.thrown;
	}

	@Override
	protected Void doInBackground() {
		this.context.randomize(rom);
		return null;
	}

	@Override
	protected void done() {
		try {
			get();
		} catch (ExecutionException ex) {
			this.thrown = ex.getCause();
		} catch (InterruptedException ex) {
			this.thrown = ex;
		} finally {
			if (this.done != null) {
				this.done.run();
			}
		}
	}
}
