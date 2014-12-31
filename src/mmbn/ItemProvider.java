package mmbn;

import java.util.List;
import java.util.Random;
import rand.DataProvider;
import rand.RandomizerContext;

public class ItemProvider extends DataProvider<Item> {
	private final ChipLibrary chipLibrary;

	private boolean codeOnly;

	public ItemProvider(RandomizerContext context, ItemProducer producer) {
		super(context, producer);
		this.chipLibrary = producer.chipLibrary();
		this.codeOnly = false;
	}

	public void setCodeOnly(boolean codeOnly) {
		this.codeOnly = codeOnly;
	}

	@Override
	protected void randomizeData(Random rng, Item item, int position) {
		if (item.isChip()) {
			BattleChip chip = item.getChip();
			int r;

			if (!codeOnly) {
				// Choose a random new battle chip with the same library and rarity.
				List<BattleChip> possibleChips = this.chipLibrary.query(
						(byte) -1,
						chip.getRarity(), chip.getRarity(),
						null,
						chip.getLibrary(),
						0, 99
				);
				if (possibleChips.size() <= 0) {
					possibleChips.add(chip);
				}

				r = rng.nextInt(possibleChips.size());
				chip = possibleChips.get(r);
			}

			// Choose a random code.
			byte[] possibleCodes = chip.getCodes();
			r = rng.nextInt(possibleCodes.length);
			byte newCode = possibleCodes[r];

			if (item.type == Item.Type.BATTLECHIP_TRAP) {
				item.setChipCodeTrap(chip, newCode);
			} else {
				item.setChipCode(chip, newCode);
			}
		}
	}
}
