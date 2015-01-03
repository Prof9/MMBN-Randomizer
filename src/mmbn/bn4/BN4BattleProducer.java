package mmbn.bn4;

import mmbn.prod.BattleProducer;
import mmbn.types.Battle;
import mmbn.types.BattleObject;
import rand.ByteStream;

public class BN4BattleProducer extends BattleProducer {
	public BN4BattleProducer() {
		super(new BN4BattleObjectProducer());
	}

	@Override
	public int getDataSize() {
		return 12;
	}

	@Override
	public Battle readFromStream(ByteStream stream) {
		byte[] base = stream.readBytes(8);
		Battle battle = new Battle(base);

		int ptr = stream.readInt32();
		stream.push();
		stream.setPosition(ptr);

		while (stream.readUInt8() != 0xFF) {
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
		
		stream.pop();
	}
    
}
