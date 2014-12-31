package mmbn;

import rand.DataProducer;

public abstract class ItemProducer<T extends Item> implements DataProducer<T> {
	private final ChipLibrary chipLibrary;
	private final Item.Type[] itemTypes;

	public ItemProducer(ChipLibrary chipLibrary, Item.Type[] itemTypes) {
		this.chipLibrary = chipLibrary;
		this.itemTypes = itemTypes;
	}

	public ChipLibrary chipLibrary() {
		return this.chipLibrary;
	}

	protected BattleChip getChip(int index) {
		return this.chipLibrary.getElement(index);
	}

	protected Item.Type getItemType(int index) {
		return this.itemTypes[index];
	}

	protected int getItemTypeIndex(Item.Type itemType) {
		for (int i = 0; i < this.itemTypes.length; i++) {
			if (this.itemTypes[i] == itemType) {
				return i;
			}
		}
		return -1;
	}

	protected void setItem(Item item, Item.Type type, int value, int subValue) {
		switch (type) {
			case BATTLECHIP:
				item.setChipCode(getChip(value), subValue);
				break;
			case BATTLECHIP_TRAP:
				item.setChipCodeTrap(getChip(value), subValue);
				break;
			case BUGFRAG:
				item.setBugFrags(value);
				break;
			case EXPMEMORY:
				item.setExpMemory(value);
				break;
			case HP:
				item.setHP(value);
				break;
			case HP_MEMORY:
				item.setHPMemory(value);
				break;
			case ITEM:
				item.setItem(value);
				break;
			case NAVICUST_PROGRAM:
				item.setProgramColor(value, subValue);
				break;
			case REGUP:
				item.setRegUP(value);
				break;
			case SUBCHIP:
				item.setSubChip(value);
				break;
			case SUBMEMORY:
				item.setSubMemory(value);
				break;
			case ZENNY:
				item.setZenny(value);
				break;
			case ZENNY_TRAP:
				item.setZennyTrap(value);
			default:
				throw new IllegalArgumentException("Unknown item type " + type);
		}
	}
}
