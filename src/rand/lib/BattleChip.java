package rand.lib;

import java.util.Arrays;
import rand.ByteConverter;
import rand.ByteStream;

/** Represents a battle chip. */
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
    }
    public enum Library {
        STANDARD,
        MEGA,
        GIGA,
        NONE,
        PA
    }
    
    private final byte[] bytes;
    
    private final byte[] codes;
    private final int rarity;
    private final int element;
    private final int library;
    private int mb;
    private final boolean isInLibrary;
    
    /**
     * Loads a battle chip from the given byte stream.
     * 
     * @param stream The byte stream to load from.
     */
    public BattleChip(ByteStream stream) {
        // Read the whole chip.
        this.bytes = stream.readBytes(44);
        
        // Load codes.
        this.codes = Arrays.copyOfRange(this.bytes, 0, 4);
        // Load rarity.
        this.rarity = ByteConverter.readUInt8(this.bytes, 5);
        // Load element.
        this.element = ByteConverter.readUInt8(this.bytes, 6);
        // Load library.
        this.library = ByteConverter.readUInt8(this.bytes, 7);
        // Load MB.
        this.mb = ByteConverter.readUInt8(this.bytes, 8);
        // Load effect flags.
        int effectFlags = ByteConverter.readUInt8(this.bytes, 9);
        this.isInLibrary = (effectFlags & 0x40) != 0;
    }
    
    /**
     * Converts this battle chip to a byte array.
     * 
     * @return The byte array.
     */
    public byte[] toBytes() {
        // Write current codes to the underlying data.
        int codeCount = getCodeCount();
        System.arraycopy(this.codes, 0, this.bytes, 0, codeCount);
        
        // Write rarity.
        this.bytes[5] = (byte)this.rarity;
        
        // Write element.
        this.bytes[6] = (byte)this.element;
        
        // Write library.
        this.bytes[7] = (byte)this.library;
        
        // Write MB
        this.bytes[8] = (byte)this.mb;
        
        return this.bytes;
    }
    /**
     * Gets the size of this chip in bytes.
     * 
     * @return The amount of bytes.
     */
    public int byteCount() {
        return this.bytes.length;
    }
    
    /**
     * Gets the maximum amount of codes this chip can have.
     * 
     * @return The maximum amount of codes.
     */
    public int getCodeCount() {
        return this.codes.length;
    }
    
    /**
     * Gets the effective codes of this battle chip.
     * 
     * @return The chip codes.
     */
    public byte[] getCodes() {
        // Get amount of effective codes.
        int amount = 0;
        while (amount < this.codes.length && this.codes[amount] != -1) {
            amount++;
        }
        
        // Copy the codes out.
        return Arrays.copyOfRange(this.codes, 0, amount);
    }
    /**
     * Sets the effective codes of this battle chip, erasing all other codes.
     * 
     * @param codes The new chip codes.
     */
    public void setCodes(byte[] codes) {
        int codeCount = getCodeCount();
        if (codes.length > codeCount) {
            throw new IllegalArgumentException("This battle chip can only have "
                    + codeCount + " codes.");
        }
        
        for (int i = 0; i < codeCount; i++) {
            if (i < codes.length) {
                this.codes[i] = codes[i];
            } else {
                this.codes[i] = -1;
            }
        }
    }
    
    /**
     * Gets the rarity of this battle chip.
     * 
     * @return The rarity.
     */
    public int rarity() {
        return this.rarity + 1;
    }
    
    /**
     * Gets the element of this battle chip, or null if the battle chip has an
     * invalid element.
     * 
     * @return The element.
     */
    public Element element() {
        Element[] values = Element.values();
        if (this.element < 0 || this.element >= values.length) {
            return null;
        } else {
            return values[this.element];
        }
    }
    
    /**
     * Gets the library of this battle chip, or null if the battle chip has an
     * invalid library.
     * 
     * @return The library.
     */
    public Library library() {
        Library[] values = Library.values();
        if (this.library < 0 || this.library >= values.length) {
            return null;
        } else {
            return values[this.library];
        }
    }
    
    /**
     * Gets the MB of this battle chip.
     * 
     * @return The MB.
     */
    public int getMB() {
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
        this.mb = mb;
    }
    
    /**
     * Checks whether this chip has the given code.
     * 
     * @param code The code.
     * @return The result.
     */
    public boolean hasCode(byte code) {
        for (byte c : getCodes()) {
            if (c == code) {
                return true;
            }
        }
        return false;
    }
    /**
     * Checks whether this chip is shown in the game library.
     * 
     * @return The result.
     */
    public boolean isInLibrary() {
        return this.isInLibrary;
    }
}
