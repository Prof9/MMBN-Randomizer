package rand;

import java.util.Observable;
import java.util.Random;

public abstract class RandomizerContext extends Observable {
	private final Random rng;
	private int progress;
	private final String lastMsg;

	public RandomizerContext() {
		this.rng = new Random();
		this.progress = 0;
		this.lastMsg = "";
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
			this.progress = progress;
			setChanged();
		}
	}

	protected void status(String message) {
		if (this.lastMsg != message) {
			setChanged();
			notifyObservers(message);
		}
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
