package mmbn.prod;

import mmbn.types.ChipTrader;
import rand.DataProducer;

public abstract class ChipTraderProducer implements DataProducer<ChipTrader> {
	@Override
	public String getDataName() {
		return "Chip Trader";
	}
}
