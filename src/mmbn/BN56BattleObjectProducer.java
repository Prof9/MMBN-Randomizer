package mmbn;

import rand.Bytes;
import rand.ByteStream;
import mmbn.BattleObject;
import mmbn.BattleObjectProducer;

public class BN56BattleObjectProducer extends BattleObjectProducer {
    public BN56BattleObjectProducer() {
        super(new BattleObject.Type[] {
            BattleObject.Type.PLAYER,
            BattleObject.Type.ENEMY,
            BattleObject.Type.MYSTERY_DATA,
            BattleObject.Type.ROCK,
            BattleObject.Type.NOTHING,
            BattleObject.Type.NOTHING,
            BattleObject.Type.WIND,
            BattleObject.Type.FLAG,
            BattleObject.Type.DESTRUCTIBLE_CUBE,
            BattleObject.Type.GUARDIAN,
            BattleObject.Type.METALCUBE
        });
    }
    
    @Override
    public int getDataSize() {
        return 4;
    }
    
    @Override
    public BattleObject readFromStream(ByteStream stream) {
        byte[] bytes = stream.readBytes(4);
        
        BattleObject obj = new BattleObject(objectTypeFromIndex(
                Bytes.readBits(bytes, 0, 4, 4)));
        obj.setSide(Bytes.readBits(bytes, 0, 0, 4));
        obj.setX(Bytes.readBits(bytes, 1, 0, 4));
        obj.setY(Bytes.readBits(bytes, 1, 4, 4));
        obj.setValue(Bytes.readUInt16(bytes, 2));
        
        return obj;
    }

    @Override
    public void writeToStream(ByteStream stream, BattleObject obj) {
        byte[] bytes = new byte[4];
        
        Bytes.writeBits(indexFromObjectType(obj.getType()), bytes, 0, 4,
                4);
        Bytes.writeBits(obj.getSide(), bytes, 0, 0, 4);
        Bytes.writeBits(obj.getX(), bytes, 1, 0, 4);
        Bytes.writeBits(obj.getY(), bytes, 1, 4, 4);
        Bytes.writeUInt16(obj.getValue(), bytes, 2);
        
        stream.writeBytes(bytes);
    }
}
