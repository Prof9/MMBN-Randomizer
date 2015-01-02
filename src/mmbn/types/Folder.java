package mmbn.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Folder {
	private static final int FOLDER_SIZE = 30;

	private final List<Item> chips;

	public Folder() {
		this.chips = new ArrayList<>(FOLDER_SIZE);
	}

	public int size() {
		return this.chips.size();
	}

	public int maxSize() {
		return FOLDER_SIZE;
	}

	public List<Item> getChips() {
		return new ArrayList<>(this.chips);
	}

	public void setChips(List<Item> chips) {
		if (chips.size() > FOLDER_SIZE) {
			throw new IllegalArgumentException("A folder can only hold "
					+ FOLDER_SIZE + " chips at most.");
		}
		this.chips.clear();
		this.chips.addAll(chips);
	}

	public void clear() {
		this.chips.clear();
	}

	public void sort() {
		Collections.sort(this.chips, new Comparator<Item>() {
			@Override
			public int compare(Item a, Item b) {
				int result = Integer.compare(a.getChip().getIDPosition(),
						b.getChip().getIDPosition());
				if (result == 0) {
					result = Integer.compare(a.getCode(), b.getCode());
				}
				return result;
			}
		});
	}

	public void addChip(Item chip) {
		if (chip.type() != Item.Type.BATTLECHIP) {
			throw new IllegalArgumentException("Item is not a chip.");
		}
		if (isFull()) {
			throw new IllegalArgumentException("No more room for chips.");
		}
		this.chips.add(chip);
	}

	public boolean removeChip(Item chip) {
		return this.chips.remove(chip);
	}

	public boolean isFull() {
		return this.chips.size() >= FOLDER_SIZE;
	}
}
