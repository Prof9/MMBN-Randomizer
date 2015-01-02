package mmbn.bn5;

import rand.Bytes;
import rand.ByteStream;
import mmbn.ChipLibrary;
import mmbn.types.BattleChip;
import mmbn.prod.ChipProducer;

public class BN5ChipProducer extends ChipProducer {
	public BN5ChipProducer(ChipLibrary library) {
		super(library, new BattleChip.Element[]{
			BattleChip.Element.HEAT,
			BattleChip.Element.AQUA,
			BattleChip.Element.ELEC,
			BattleChip.Element.WOOD,
			BattleChip.Element.RECOVERY,
			BattleChip.Element.BONUS,
			BattleChip.Element.SWORD,
			BattleChip.Element.INVISIBLE,
			BattleChip.Element.CURSOR,
			BattleChip.Element.SUMMON,
			BattleChip.Element.WIND,
			BattleChip.Element.BREAK,
			BattleChip.Element.NULL,
			BattleChip.Element.PA,
			BattleChip.Element.UNUSED
		}, new BattleChip.Library[]{
			BattleChip.Library.STANDARD,
			BattleChip.Library.MEGA,
			BattleChip.Library.GIGA,
			BattleChip.Library.NONE,
			BattleChip.Library.PA
		});
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
