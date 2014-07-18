package rand.prod;

import rand.ByteConverter;
import rand.ByteStream;
import rand.obj.BattleObject;

public class BN6BattleObjectProducer extends BattleObjectProducer {
    public BN6BattleObjectProducer() {
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
    public BattleObject readFromStream(ByteStream stream) {
        byte[] bytes = stream.readBytes(4);
        
        BattleObject obj = new BattleObject(objectTypeFromIndex(
                ByteConverter.readBits(bytes, 0, 4, 4)));
        obj.setSide(ByteConverter.readBits(bytes, 0, 0, 4));
        obj.setX(ByteConverter.readBits(bytes, 1, 0, 4));
        obj.setY(ByteConverter.readBits(bytes, 1, 4, 4));
        obj.setValue(ByteConverter.readUInt16(bytes, 2));
        
        return obj;
    }

    @Override
    public void writeToStream(ByteStream stream, BattleObject obj) {
        byte[] bytes = new byte[4];
        
        ByteConverter.writeBits(indexFromObjectType(obj.getType()), bytes, 0, 4,
                4);
        ByteConverter.writeBits(obj.getSide(), bytes, 0, 0, 4);
        ByteConverter.writeBits(obj.getX(), bytes, 1, 0, 4);
        ByteConverter.writeBits(obj.getY(), bytes, 1, 4, 4);
        ByteConverter.writeUInt16(obj.getValue(), bytes, 2);
        
        stream.writeBytes(bytes);
    }
}
