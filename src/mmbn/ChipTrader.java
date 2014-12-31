package mmbn;

import java.util.ArrayList;
import java.util.List;

public class ChipTrader {
	protected final List<ChipTraderEntry> entries;

	public ChipTrader() {
		this.entries = new ArrayList<>();
	}

	public void addEntry(ChipTraderEntry entry) {
		this.entries.add(entry);
	}

	public int size() {
		return this.entries.size();
	}

	public List<ChipTraderEntry> entries() {
		return new ArrayList<>(this.entries);
	}

	public ChipTraderEntry getEntry(int i) {
		return this.entries.get(i);
	}

	public void setEntry(int i, ChipTraderEntry entry) {
		this.entries.set(i, entry);
	}

	public void clear() {
		this.entries.clear();
	}
}
