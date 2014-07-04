package rand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Stack;

/**
 * A buffered ROM with various read/write methods.
 */
public class Rom {
    private final byte[] byteArray;
    private final ArrayList<Byte> byteList;
    private final boolean resizeable;
    private final Stack<Integer> returnStack;
    private int position;
    
    /**
     * Construct a ROM loaded from the given path.
     * 
     * @param path The path to load the ROM from.
     * @param resizeable Whether the ROM should be resizeable. A resizeable ROM
     *      takes up considerably more memory space.
     * @throws IOException
     */
    public Rom(final Path path, boolean resizeable) throws IOException {
        this(Files.readAllBytes(path), resizeable);
    }
    
    /**
     * Construct a ROM from the given bytes.
     * 
     * @param bytes The bytes to construct a ROM from.
     * @param resizeable Whether the ROM should be resizeable. A resizeable ROM
     *      takes up considerably more memory space.
     */
    public Rom(final byte[] bytes, boolean resizeable) {
        this.resizeable = resizeable;
        this.byteList = new ArrayList<>();
        this.returnStack = new Stack<>();
        
        if (resizeable) {
            for (byte b : bytes) {
                this.byteList.add(b);
            }
            this.byteArray = null;
        } else {
            this.byteArray = bytes.clone();
        }
        
        this.position = 0;
    }
    
    /**
     * Converts the entire ROM to a byte array.
     * 
     * @return The ROM as a byte array.
     */
    public byte[] toBytes() {
        byte[] r = new byte[size()];
        
        byte b;
        for (int i = 0; i < r.length; i++) {
            if (resizeable) {
                b = this.byteList.get(i);
            } else {
                b = this.byteArray[i];
            }
            r[i] = b;
        }
        
        return r;
    }
    
    /**
     * Reads the given number of bytes from the ROM and advances the position.
     * 
     * @param amount The amount of bytes to read.
     * @return The array of bytes read from the ROM.
     */
    public byte[] readBytes(int amount) {
        if (size() - getPosition() < amount)
            throw new IllegalStateException("Not enough bytes left.");
        
        byte[] r = new byte[amount];
        
        byte b;
        for (int i = 0; i < amount; i++) {
            if (resizeable) {
                b = this.byteList.get(this.position);
            } else {
                b = this.byteArray[this.position];
            }
            this.position++;
            r[i] = b;
        }
        return r;
    }
    
    /**
     * Writes the given byte array to the ROM and advances the position.
     * 
     * @param bytes The bytes to write to the ROM.
     */
    public void writeBytes(byte[] bytes) {
        if (!resizeable && size() - getPosition() < bytes.length)
            throw new IllegalStateException("Not enough bytes left and ROM is "
                    + "not resizeable.");
        
        byte b;
        for (int i = 0; i < bytes.length; i++) {
            b = bytes[i];
            if (resizeable) {
                if (hasNext()) {
                    this.byteList.set(this.position, b);
                } else {
                    this.byteList.add(b);
                }
            } else {
                this.byteArray[this.position] = b;
            }
            this.position++;
        }
    }
    
    /**
     * Reads the given number of bytes from the ROM into a signed 64-bit
     * integer.
     * 
     * @param amount The amount of bytes to read.
     * @return A value read from the ROM.
     */
    protected long readValue(int amount) {
        byte[] bytes = readBytes(amount);
        long r = 0;
        
        long b;
        for (int i = 0; i < amount; i++) {
            b = bytes[i] & 0xFF;
            r += b << (i * 8);
        }
        
        return r;
    }
    
    /**
     * Writes the given number of first bytes of the given signed 64-bit integer
     * to the ROM.
     * 
     * @param value The value to write to the ROM.
     * @param amount The amount of bytes to write.
     */
    protected void writeValue(long value, int amount) {
        byte[] bytes = new byte[amount];
        for (int i = 0; i < amount; i++) {
            bytes[i] = (byte)(value & 0xFF);
            value >>= 8;
        }
        
        writeBytes(bytes);
    }
    
    /**
     * Gets the current read/write position.
     * 
     * @return The current position.
     */
    public int getPosition() {
        return this.position;
    }
    
    /**
     * Sets the current read/write position.
     * 
     * @param position The new position.
     */
    public void setPosition(int position) {
        position = getRealPtr(position);
        if (position < 0 || position >= size())
            throw new IndexOutOfBoundsException("Position is out of bounds.");
        
        this.position = position;
    }
    
    /**
     * Pushes the current position onto the stack. 
     */
    public void pushPosition() {
        this.returnStack.push(getPosition());
    }
    
    /**
     * Pops the current position from the stack.
     */
    public void popPosition() {
        setPosition(rewindStack(1));
    }
    
    /**
     * Rewinds the position stack by the given amount and returns the last
     * position popped.
     * 
     * @param amount The amount of pointers to remove.
     * @return The last position popped from the stack.
     */
    public int rewindStack(int amount) {
        if (amount > this.returnStack.size())
            throw new IllegalStateException("Not enough pointers on the "
                    + "position stack.");
        
        int r = 0;
        while (amount-- > 0) {
            r = this.returnStack.pop();
        }
        return r;
    }
    
    /**
     * Advances the current read/write position by the given amount.
     * 
     * @param amount The amount of bytes to skip.
     */
    public void advance(int amount) {
        setPosition(getPosition() + amount);
    }
    
    /**
     * Converts the given ROM pointer to an absolute pointer.
     * 
     * @param romPointer The ROM pointer to convert.
     * @return The resulting absolute pointer.
     */
    public static int getRealPtr(int romPointer) {
        return romPointer & 0x7FFFFFF;
    }
    
    /**
     * Gets the current ROM size.
     * 
     * @return The current ROM size.
     */
    public int size() {
        if (resizeable()) {
            return this.byteList.size();
        } else {
            return this.byteArray.length;
        }
    }
    
    /**
     * Checks whether the ROM has bytes left.
     * 
     * @return true if there are bytes left; otherwise, false.
     */
    public boolean hasNext() {
        if (resizeable) {
            return this.position < this.byteList.size();
        } else {
            return this.position < this.byteArray.length;
        }
    }
    
    /**
     * Returns whether the ROM is resizeable.
     * 
     * @return true if the ROM is resizeable; otherwise, false.
     */
    public boolean resizeable() {
        return this.resizeable;
    }
    
    /**
     * Reads the next signed byte from the ROM and advances the position.
     * 
     * @return The byte read from the ROM.
     */
    public byte readInt8() {
        return readBytes(1)[0];
    }
    
    /**
     * Reads the next unsigned byte from the ROM and advances the position.
     * 
     * @return The unsigned byte read from the ROM.
     */
    public short readUInt8() {
        return (short)readValue(1);
    }
    
    /**
     * Reads the next signed 16-bit integer from the ROM and advances the
     * position.
     * 
     * @return The signed 16-bit integer read from the ROM.
     */
    public short readInt16() {
        return (short)readValue(2);
    }
    
    /**
     * Reads the next unsigned 16-bit integer from the ROM and advances the
     * position.
     * 
     * @return The unsigned 16-bit integer read from the ROM.
     */
    public int readUInt16() {
        return (int)readValue(2);
    }
    
    /**
     * Reads the next signed 32-bit integer from the ROM and advances the
     * position.
     * 
     * @return The signed 32-bit integer read from the ROM.
     */
    public int readInt32() {
        return (int)readValue(4);
    }
    
    /**
     * Reads the next ROM pointer from the ROM and returns it as an absolute
     * pointer.
     * 
     * @return An absolute pointer read from the ROM.
     */
    public int readPtr() {
        return getRealPtr(readInt32());
    }
    
    /**
     * Reads the next unsigned 32-bit integer from the ROM and advances the
     * position.
     * 
     * @return The unsigned 32-bit integer read from the ROM.
     */
    public long readUInt32() {
        return readValue(4);
    }
    
    /**
     * Writes the given byte to the ROM and advances the position.
     * 
     * @param b The byte to be written to the ROM.
     */
    public void writeByte(byte b) {
        writeBytes(new byte[] { b });
    }
    
    /**
     * Writes the given 16-bit integer to the ROM and advances the position.
     * 
     * @param value The 16-bit integer to write to the ROM.
     */
    public void writeInt16(short value) {
        writeValue(value, 2);
    }
    
    /**
     * Writes the given 32-bit integer to the ROM and advances the position.
     * 
     * @param value The 32-bit integer to write to the ROM.
     */
    public void writeInt32(int value) {
        writeValue(value, 4);
    }
}