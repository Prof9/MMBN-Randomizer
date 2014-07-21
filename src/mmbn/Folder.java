package mmbn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Folder {
    private static final int FOLDER_SIZE = 30;
    
    private final List<Reward> chips;
    
    public Folder() {
        this.chips = new ArrayList<>(FOLDER_SIZE);
    }
    
    public int size() {
        return this.chips.size();
    }
    public int maxSize() {
        return FOLDER_SIZE;
    }
    
    public List<Reward> getChips() {
        return new ArrayList<>(this.chips);
    }
    public void setChips(List<Reward> chips) {
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
        this.chips.sort(new Comparator<Reward>() {
            @Override
            public int compare(Reward a, Reward b) {
                return Integer.compare(a.getChip().index(), b.getChip().index());
            }
        });
    }
    
    public void addChip(Reward chip) {
        if (chip.getType() != Reward.Type.BATTLECHIP) {
            throw new IllegalArgumentException("Reward is not a chip.");
        }
        if (isFull()) {
            throw new IllegalArgumentException("No more room for chips.");
        }
        this.chips.add(chip);
    }
    public boolean removeChip(Reward chip) {
        return this.chips.remove(chip);
    }
    
    public boolean isFull() {
        return this.chips.size() >= FOLDER_SIZE;
    }
}
