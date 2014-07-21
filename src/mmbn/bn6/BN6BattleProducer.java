package mmbn.bn6;

import mmbn.Battle;
import mmbn.BattleObject;
import mmbn.BattleProducer;
import rand.ByteStream;

public class BN6BattleProducer extends BattleProducer {
    public BN6BattleProducer() {
        super(new BN6BattleObjectProducer());
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
        stream.push();
        stream.setPosition(ptr);
        
        for (BattleObject obj : battle.getObjects()) {
            this.objectProducer.writeToStream(stream, obj);
        }
    }
}