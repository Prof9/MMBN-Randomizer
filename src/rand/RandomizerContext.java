package rand;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

public abstract class RandomizerContext {
	public static final String PROGRESS_PROPERTY_NAME = "progress";
	public static final String STATUS_PROPERTY_NAME = "status";
	
	private final Random rng;
	private int progress;
	private String lastMsg;
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public RandomizerContext() {
		this.rng = new Random();
		this.progress = 0;
		this.lastMsg = "";
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	public void setSeed(int seed) {
		this.rng.setSeed(seed);
	}

	protected int next(int bound) {
		return this.rng.nextInt(bound);
	}

	protected void runProvider(DataProvider provider, ByteStream rom) {
		provider.randomize(this.rng);
		provider.produce(rom);
	}

	public int getProgress() {
		return this.progress;
	}

	protected void setProgress(int progress) {
		if (this.progress != progress) {
			int prevProgress = this.progress;
			this.progress = progress;
			
			this.pcs.firePropertyChange(PROGRESS_PROPERTY_NAME,
					prevProgress, progress);
		}
	}

	protected void status(String message) {
		String prevMessage = this.lastMsg;
		this.lastMsg = message;
		
		this.pcs.firePropertyChange(STATUS_PROPERTY_NAME,
				prevMessage, message);
	}

	public void randomize(ByteStream rom) {
		String romId = Main.getRomId(rom);
		for (String id : getSupportedRomIds()) {
			if (romId.equals(id)) {
				randomize(romId, rom);
				return;
			}
		}
		throw new IllegalArgumentException("Unsupported ROM ID \"" + romId
				+ "\".");
	}

	protected abstract void randomize(String romId, ByteStream rom);

	public abstract String[] getSupportedRomIds();
}
