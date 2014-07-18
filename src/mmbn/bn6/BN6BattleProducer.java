package mmbn.bn6;

import mmbn.bn6.BN6BattleObjectProducer;
import rand.ByteStream;
import rand.DataProducer;
import mmbn.Battle;

public class BN6BattleProducer implements DataProducer<Battle> {
    protected BN6BattleObjectProducer objectProducer;
    
    public BN6BattleProducer() {
        this.objectProducer = new BN6BattleObjectProducer();
    }
    
    @Override
    public Battle readFromStream(ByteStream stream) {
        byte[] base = stream.readBytes(12);
        Battle battle = new Battle(base);
        
        int ptr = stream.readInt32();
        stream.push();
        stream.setPosition(ptr);
        
        while ((stream.readUInt8() & 0xF0) != 0xF0) {
            stream.advance(-1);
            battle.addObject(this.objectProducer.readFromStream(stream));
        }
        battle.lockObjectListMax();
        
        stream.pop();
        return battle;
    }

    @Override
    public void writeToStream(ByteStream stream, Battle battle) {
        stream.writeBytes(battle.base());
        
        int ptr = stream.readInt32();
    }
}