package mmbn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Represents a BattleChip. */
public class BattleChip {    
    public enum Element {
        HEAT,
        AQUA,
        ELEC,
        WOOD,
        PLUS,
        SWORD,
        CURSOR,
        OBJECT,
        WIND,
        BREAK,
        NULL,
        PA,
        UNUSED,
    }
    public enum Library {
        STANDARD,
        MEGA,
        GIGA,
        NONE,
        PA
    }
    
    private static final byte[] supportedCodes = new byte[] {
        0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
        0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10, 0x11,
        0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A
    };
    
    protected final int index;
    protected final byte[] bytes;    
    protected final byte[] codes;
    
    protected byte rarity;
    protected BattleChip.Element element;
    protected BattleChip.Library library;
    protected byte mb;
    protected boolean isInLibrary;
    
    /**
     * Converts the given code to its string equivalent.
     * 
     * @param c The code to convert.
     * @return The string equivalent.
     */
    public static String codeToString(byte c) {
        if (c >= 0 && c < 26) {
            return Character.toString((char)('A' + c));
        } else if (c == 26) {
            return "*";
        } else if (c == -1) {
            return "";
        } else {
            return String.format("0x%02X", c);
        }
    }
    
    /**
     * Constructs a new BattleChip with the given index, the given backing byte
     * array and the given maximum amount of chip codes.
     * 
     * @param index The index of the BattleChip.
     * @param base The base byte array to store.
     * @param maxCodeCount The maximum amount of chip codes.
     */
    public BattleChip(int index, byte[] base, int maxCodeCount) {
        this.index = index;
        this.bytes = base;
        this.codes = new byte[maxCodeCount];
        for (int i = 0; i < maxCodeCount; i++) {
            this.codes[i] = -1;
        }
        
        this.rarity = 1;
        this.element = BattleChip.Element.NULL;
        this.library = BattleChip.Library.STANDARD;
        this.mb = 0;
        this.isInLibrary = true;
    }
    
    /**
     * Gets a sorted array of all possible codes this chip can have.
     * 
     * @return The possible chip codes.
     */
    public byte[] supportedCodes() {
        return supportedCodes;
    }
    
    /**
     * Gets the maximum amount of chip codes this BattleChip can have.
     * 
     * @return The maximum amount of chip codes.
     */
    public int maxCodeCount() {
        return this.codes.length;
    }
    
    /**
     * Checks whether this chip has the given code.
     * 
     * @param code The code.
     * @return The result.
     */
    public boolean hasCode(byte code) {
        for (byte c : getCodes()) {
            if (c == -1) {
                return false;
            } else if (c == code) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Generates a given amount of random chip codes.
     * 
     * @param rng The random number generator to use.
     * @param amount The amount of chip codes to generate.
     * @return A byte array of sorted random chip codes with the given size.
     */
    public byte[] generateRandomCodes(Random rng, int amount) {
        return generateRandomCodes(rng, amount, null);
    }
    /**
     * Generates a given amount of random chip codes, excluding the given codes.
     * 
     * @param rng The random number generator to use.
     * @param amount The amount of chip codes to generate.
     * @param excluded The chip codes that must be excluded. Can be null.
     * @return A byte array of sorted random chip codes with the given size.
     */
    public byte[] generateRandomCodes(Random rng, int amount, Iterable<Byte> excluded) {
        byte[] supported = supportedCodes();
        
        if (amount < 0 || amount > supported.length) {
            throw new IndexOutOfBoundsException("Code amount out of bounds.");
        }
            
        // Create list of possible codes left.
        List<Byte> possible
                = new ArrayList<>(supported.length);
        for (byte c : supported) {
            possible.add(c);
        }
        // Remove excluded codes.
        if (excluded != null) {
            for (byte c : excluded) {
                possible.remove((Byte)c);
            }
        }
        
        // Pull out random codes.
        // Could use Collections.shuffle(), but that does not allow a Random
        // parameter.
        byte[] result = new byte[amount];
        for (int i = 0; i < amount; i++) {
            int codeIndex = rng.nextInt(possible.size());
            result[i] = possible.get(codeIndex);
            possible.remove(codeIndex);
        }
        
        // Sort and return.
        Arrays.sort(result);
        return result;
    }
    
    /**
     * Gets the base byte array of this BattleChip.
     * 
     * @return The byte array.
     */
    public byte[] base() {
        return this.bytes.clone();
    }
    /**
     * Gets the index of this BattleChip.
     * 
     * @return The index.
     */
    public int index() {
        return this.index;
    }
    
    /**
     * Gets the current effective chip codes of this BattleChip.
     * 
     * @return The current chip codes.
     */
    public byte[] getCodes() {
        // Get amount of effective codes.
        int amount = 0;
        while (amount < codes.length && codes[amount] != -1) {
            amount++;
        }
        
        // Copy the codes out.
        return Arrays.copyOfRange(codes, 0, amount);
    }
    /**
     * Sets the effective codes of this BattleChip to the given codes, erasing
     * all other codes.
     * 
     * @param codes The new chip codes.
     */
    public void setCodes(byte[] codes) {
        byte[] possible = supportedCodes();
        
        List<Byte> newCodesList = new ArrayList<>(maxCodeCount());
        int newCount = 0;
        for (int i = 0; i < codes.length; i++) {
            byte c = codes[i];
            if (Arrays.binarySearch(possible, c) < 0) {
                throw new IllegalArgumentException("Code " + codeToString(c)
                + " is not valid for this chip.");
            } else if (newCodesList.size() == maxCodeCount()) {
                throw new IllegalArgumentException("This chip can only have "
                + maxCodeCount() + " different chip codes at most.");
            } else if (!newCodesList.contains(c)) {
                newCodesList.add(c);
                newCount++;
            }
        }
        
        Collections.sort(newCodesList);
        for (int i = 0; i < maxCodeCount(); i++) {
            if (i < newCount) {
                this.codes[i] = newCodesList.get(i);
            } else {
                this.codes[i] = -1;
            }
        }
    }
    
    /**
     * Gets all codes of this BattleChip.
     * 
     * @return The codes.
     */
    public byte[] getAllCodes() {
        return this.codes;
    }
    
    /**
     * Overwrites all codes of this BattleChip with the given codes.
     * 
     * @param codes The new chip codes.
     */
    public void setAllCodes(byte[] codes) {
        if (codes.length != this.codes.length) {
            throw new IllegalArgumentException("This chip must have " +
                    this.codes.length + " codes.");
        }
        
        System.arraycopy(codes, 0, this.codes, 0, this.codes.length);
    }
    
    /**
     * Gets the rarity of this battle chip.
     * 
     * @return The rarity.
     */
    public byte getRarity() {
        return this.rarity;
    }
    /** Sets the rarity of this battle chips.
     * 
     * @param rarity The new rarity.
     */
    public void setRarity(int rarity) {
        if (rarity < 1 || rarity > 5) {
            throw new IllegalArgumentException("The new rarity may be at least"
            + " 1 and at most 5.");
        }
        this.rarity = (byte)rarity;
    }
    
    /**
     * Gets the element of this BattleChip, or null if the BattleChip has an
     * invalid element.
     * 
     * @return The element.
     */
    public Element getElement() {
        return this.element;
    }
    /**
     * Sets the element of this BattleChip.
     * 
     * @param element The element.
     */
    public void setElement(Element element) {
        this.element = element;
    }
    
    /**
     * Gets the library of this BattleChip, or null if the BattleChip has an
     * invalid library.
     * 
     * @return The library.
     */
    public Library getLibrary() {
        return this.library;
    }
    /**
     * Sets the library of this BattleChip.
     * 
     * @param library The new library.
     */
    public void setLibrary(Library library) {
        this.library = library;
    }
    
    /**
     * Gets the MB of this battle chip.
     * 
     * @return The MB.
     */
    public byte getMB() {
        return this.mb;
    }
    /**
     * Sets the MB of this battle chip.
     * 
     * @param mb The new MB.
     */
    public void setMB(int mb) {
        if (mb < 0 || mb > 99) {
            throw new IllegalArgumentException("The new MB must be at least 0 "
                    + "and at most 99.");
        }
        
        this.mb = (byte)mb;
    }
    
    /**
     * Gets a boolean that indicates whether this BattleChip is listed in the
     * game's library.
     * 
     * @return true if this chip is in the library; otherwise, false;
     */
    public boolean getIsInLibrary() {
        return this.isInLibrary;
    }
    /**
     * Sets a boolean that indicates whether this BattleChip is listed in the
     * game's library.
     * 
     * @param isInLibrary Whether this chip is in the library.
     */
    public void setIsInLibrary(boolean isInLibrary) {
        this.isInLibrary = isInLibrary;
    }
}
