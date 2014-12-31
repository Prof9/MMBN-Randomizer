package mmbn;

import rand.DataProducer;

public abstract class ChipTraderEntryProducer implements DataProducer<ChipTraderEntry> {
	@Override
	public String getDataName() {
		return "Chip Trader entry";
	}
}
