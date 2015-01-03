package mmbn.bn4;

import mmbn.prod.BattleObjectProducer;
import mmbn.types.BattleObject;
import rand.ByteStream;
import rand.Bytes;

public class BN4BattleObjectProducer extends BattleObjectProducer {
    public BN4BattleObjectProducer() {
		super(new BattleObject.Type[]{
			BattleObject.Type.PLAYER,
			BattleObject.Type.ENEMY,
			BattleObject.Type.MYSTERY_DATA,
			BattleObject.Type.ROCK,
			BattleObject.Type.DS_NAVI,
			BattleObject.Type.METALGEAR,
			BattleObject.Type.WIND,
			BattleObject.Type.FLAG,
			BattleObject.Type.ALLY
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
				Bytes.readUInt8(bytes, 3)));
		obj.setX(Bytes.readUInt8(bytes, 1));
		obj.setY(Bytes.readUInt8(bytes, 2));
		obj.setValue(Bytes.readUInt8(bytes, 0));

		return obj;
	}

	@Override
	public void writeToStream(ByteStream stream, BattleObject obj) {
		byte[] bytes = new byte[4];

		Bytes.writeUInt8((short)indexFromObjectType(obj.getType()), bytes, 3);
		Bytes.writeUInt8(obj.getX(), bytes, 1);
		Bytes.writeUInt8(obj.getY(), bytes, 2);
		Bytes.writeUInt8((short)obj.getValue(), bytes, 0);

		stream.writeBytes(bytes);
	}
}
