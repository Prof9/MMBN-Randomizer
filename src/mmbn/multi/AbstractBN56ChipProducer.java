package mmbn.bn6;

import rand.Bytes;
import rand.ByteStream;
import mmbn.types.BattleChip;
import mmbn.ChipLibrary;
import mmbn.prod.ChipProducer;

public class AbstractBN56ChipProducer extends ChipProducer {
	public AbstractBN56ChipProducer(ChipLibrary library,
			BattleChip.Element[] elements, BattleChip.Library[] libraries) {
		super(library, elements, libraries);
	}

	@Override
	public int getDataSize() {
		return 44;
	}

	@Override
	protected BattleChip deferredReadFromStream(ByteStream stream, int chipIndex) {
		byte[] bytes = stream.readBytes(44);
		BattleChip chip = new BattleChip(chipIndex, bytes, 4);

		byte[] codes = new byte[4];
		System.arraycopy(bytes, 0, codes, 0, 4);
		chip.setAllCodes(codes);

		chip.setRarity(bytes[5] + 1);
		chip.setElement(elementFromIndex(bytes[6]));
		chip.setLibrary(libraryFromIndex(bytes[7]));
		chip.setMB(bytes[8]);
		chip.setIDPosition(Bytes.readUInt16(bytes, 28));

		chip.setIsInLibrary(Bytes.readBits(bytes, 9, 6, 1) != 0);

		return chip;
	}

	@Override
	public void writeToStream(ByteStream stream, BattleChip chip) {
		byte[] bytes = chip.base();

		System.arraycopy(chip.getAllCodes(), 0, bytes, 0, 4);

		bytes[5] = (byte) (chip.getRarity() - 1);
		bytes[6] = indexFromElement(chip.getElement());
		bytes[7] = indexFromLibrary(chip.getLibrary());
		bytes[8] = chip.getMB();
		Bytes.writeUInt16(chip.getIDPosition(), bytes, 28);

		Bytes.writeBits(chip.getIsInLibrary() ? 1 : 0, bytes, 9, 6, 1);

		stream.writeBytes(bytes);
	}
}
