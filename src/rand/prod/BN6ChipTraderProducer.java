package rand.prod;

import rand.ByteStream;
import rand.DataProducer;
import rand.lib.Library;
import rand.obj.BattleChip;
import rand.obj.ChipTrader;
import rand.obj.ChipTraderEntry;

public class BN6ChipTraderProducer implements DataProducer<ChipTrader> {
    protected DataProducer<ChipTraderEntry> entryProducer;
    
    public BN6ChipTraderProducer(final Library<BattleChip> library) {
        this.entryProducer = new BN6ChipTraderEntryProducer(library);
    }

    @Override
    public ChipTrader readFromStream(ByteStream stream) {
        ChipTrader trader = new ChipTrader();
        
        stream.advance(4);
        
        int ptr = stream.readInt32();
        stream.push();
        stream.setPosition(ptr);
        
        while (stream.readInt16() != 0) {
            stream.advance(-2);
            trader.addEntry(this.entryProducer.readFromStream(stream));
        }
        stream.advance(4);
        
        stream.pop();
        stream.advance(4);
        
        return trader;
    }

    @Override
    public void writeToStream(ByteStream stream, ChipTrader trader) {
        stream.advance(4);
        
        int ptr = stream.readInt32();
        stream.push();
        stream.setPosition(ptr);
        
        for (ChipTraderEntry entry : trader.entries()) {
            this.entryProducer.writeToStream(stream, entry);
        }
        
        stream.pop();
        stream.advance(4);
    }
}
