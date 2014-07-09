package rand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Stack;

/**
 * A buffered, non-resizeable stream of bytes with various read/write methods.
 */
public class ByteStream {
    private final byte[] bytes;
    private final Stack<Integer> returnStack;
    private final int headerSize;
    private int position;
    
    /**
     * Constructs a byte stream loaded from the given path.
     * 
     * @param path The path to load the byte stream from.
     * @throws IOException
     */
    public ByteStream(final Path path) throws IOException {
        this(path, 0);
    }
    
    /**
     * Constructs a byte stream loaded from the given path with the given header
     * size.
     * 
     * @param path The path to load the byte stream from.
     * @param headerSize The header size of the byte stream.
     * @throws IOException
     */
    public ByteStream(final Path path, final int headerSize) throws IOException {
        this(Files.readAllBytes(path), headerSize);
    }
    
    /**
     * Constructs a byte stream from the given bytes.
     * 
     * @param bytes The bytes to construct a byte stream from.
     */
    public ByteStream(final byte[] bytes) {
        this(bytes, 0);
    }
    
    /**
     * Constructs a byte stream from the given bytes with the given header size.
     * 
     * @param bytes The bytes to construct a byte stream from.
     * @param headerSize The header size of the byte stream.
     */
    public ByteStream(final byte[] bytes, int headerSize) {
        this.bytes = bytes.clone();
        this.returnStack = new Stack<>();
        this.headerSize = headerSize;
        this.position = 0;
    }
    
    /**
     * Gets the size of the byte stream.
     * 
     * @return The size.
     */
    public int size() {
        return this.bytes.length;
    }
    
    /**
     * Checks whether this byte stream has bytes left.
     * 
     * @return true if there are bytes left; otherwise, false.
     */
    public boolean hasNext() {
        return this.position < this.bytes.length;
    }
    
    /**
     * Gets the header size of this byte stream.
     * 
     * @return The header size.
     */
    public int headerSize() {
        return this.headerSize;
    }
    
    /**
     * Gets the current read/write position offset by the header size.
     * 
     * @return The current position.
     */
    public int getPosition() {
        return this.position + this.headerSize;
    }
    
    /**
     * Gets the current absolute read/write position.
     * 
     * @return The current absolute position.
     */
    public int getRealPosition() {
        return this.position;
    }
    
    /**
     * Sets the current read/write position offset by the header size.
     * 
     * @param position The new position.
     */
    public void setPosition(int position) {
        setRealPosition(position - this.headerSize);
    }
    
    /**
     * Sets the current absolute read/write position.
     * @param position The new absolute position.
     */
    public void setRealPosition(int position) {
        if (position < 0 || position >= this.bytes.length)
            throw new IndexOutOfBoundsException("Position is out of bounds.");
        
        this.position = position;
    }
    
    /**
     * Converts the entire byte stream to a byte array.
     * 
     * @return The byte stream as a byte array.
     */
    public byte[] toBytes() {
        return this.bytes.clone();
    }
    
    /**
     * Reads the given number of bytes and advances the position.
     * 
     * @param amount The amount of bytes to read.
     * @return The array of bytes read.
     */
    public byte[] readBytes(int amount) {
        if (this.bytes.length - this.position < amount)
            throw new IllegalStateException("Not enough bytes left.");
        
        byte[] r = new byte[amount];
        System.arraycopy(this.bytes, this.position, r, 0, amount);
        this.position += amount;
        return r;
    }
    
    /**
     * Writes the given byte array to the byte stream and advances the position.
     * 
     * @param bytes The bytes to write.
     */
    public void writeBytes(byte[] bytes) {
        if (this.bytes.length - this.position < bytes.length)
            throw new IllegalStateException("Not enough bytes left.");
        
        System.arraycopy(bytes, 0, this.bytes, this.position, bytes.length);
        this.position += bytes.length;
    }
    
    /**
     * Pushes the current position onto the stack. 
     */
    public void push() {
        this.returnStack.push(this.position);
    }
    
    /**
     * Pops the current position from the stack.
     */
    public void pop() {
        setPosition(rewind(1));
    }
    
    /**
     * Rewinds the position stack by the given amount and returns the last
     * position popped offset by the header size.
     * 
     * @param amount The amount of pointers to remove.
     * @return The last position popped from the stack.
     */
    public int rewind(int amount) {
        if (amount > this.returnStack.size())
            throw new IllegalStateException("Not enough pointers on the "
                    + "position stack.");
        
        int r = 0;
        while (amount-- > 0) {
            r = this.returnStack.pop();
        }
        return r + headerSize;
    }
    
    /**
     * Advances the current read/write position by the given amount.
     * 
     * @param amount The amount of bytes to skip.
     */
    public void advance(int amount) {
        setRealPosition(this.position + amount);
    }
    
    /**
     * Reads the next 8-bit signed integer from the stream and advances the
     * position.
     * 
     * @return The value that was read.
     */
    public byte readInt8() {
        if (this.bytes.length - this.position < 1)
            throw new IllegalStateException("Not enough bytes left.");
        
        return this.bytes[this.position++];
    }
    
    /**
     * Reads the next 8-bit unsigned integer from the stream and advances the
     * position.
     * 
     * @return The value that was read.
     */
    public short readUInt8() {
        if (this.bytes.length - this.position < 1)
            throw new IllegalStateException("Not enough bytes left.");
        
        return (short)(this.bytes[this.position++] & 0xFF);
    }
    
    /**
     * Reads the next 16-bit signed integer from the stream and advances the
     * position.
     * 
     * @return The value that was read.
     */
    public short readInt16() {
        if (this.bytes.length - this.position < 2)
            throw new IllegalStateException("Not enough bytes left.");
        
        short r = ByteConverter.readInt16(this.bytes, this.position);
        this.position += 2;
        return r;
    }
    
    /**
     * Reads the next 16-bit unsigned integer from the stream and advances the
     * position.
     * 
     * @return The value that was read.
     */
    public int readUInt16() {
        if (this.bytes.length - this.position < 2)
            throw new IllegalStateException("Not enough bytes left.");
        
        int r = ByteConverter.readUInt16(this.bytes, this.position);
        this.position += 2;
        return r;
    }
    
    /**
     * Reads the next 32-bit signed integer from the stream and advances the
     * position.
     * 
     * @return The value that was read.
     */
    public int readInt32() {
        if (this.bytes.length - this.position < 4)
            throw new IllegalStateException("Not enough bytes left.");
        
        int r = ByteConverter.readInt32(this.bytes, this.position);
        this.position += 4;
        return r;
    }
    
    /**
     * Reads the next 32-bit signed integer from the stream and advances the
     * position.
     * 
     * @return The value that was read.
     */
    public long readUInt32() {
        if (this.bytes.length - this.position < 4)
            throw new IllegalStateException("Not enough bytes left.");
        
        long r = ByteConverter.readUInt32(this.bytes, this.position);
        this.position += 4;
        return r;
    }
    
    /**
     * Reads the next 64-bit signed integer from the stream and advances the
     * position.
     * 
     * @return The value that was read.
     */
    public long readInt64() {
        if (this.bytes.length - this.position < 8)
            throw new IllegalStateException("Not enough bytes left.");
        
        int r = ByteConverter.readInt32(this.bytes, this.position);
        this.position += 8;
        return r;
    }
    
    /**
     * Writes the given 8-bit signed integer to the stream and advances the
     * position.
     * 
     * @param value The value to write.
     */
    public void writeInt8(byte value) {
        if (this.bytes.length - this.position < 1)
            throw new IllegalStateException("Not enough bytes left.");
        
        this.bytes[this.position++] = value;
    }
    
    /**
     * Writes the given 8-bit unsigned integer to the stream and advances the
     * position.
     * 
     * @param value The value to write.
     */
    public void writeUInt8(short value) {
        if (this.bytes.length - this.position < 1)
            throw new IllegalStateException("Not enough bytes left.");
        
        this.bytes[this.position++] = (byte)(value & 0xFF);
    }
    
    /**
     * Writes the given 16-bit signed integer to the stream and advances the
     * position.
     * 
     * @param value The value to write.
     */
    public void writeInt16(short value) {
        if (this.bytes.length - this.position < 2)
            throw new IllegalStateException("Not enough bytes left.");
        
        ByteConverter.writeInt16(value, this.bytes, this.position);
        this.position += 2;
    }
    
    /**
     * Writes the given 16-bit unsigned integer to the stream and advances the
     * position.
     * 
     * @param value The value to write.
     */
    public void writeUInt16(int value) {
        if (this.bytes.length - this.position < 2)
            throw new IllegalStateException("Not enough bytes left.");
        
        ByteConverter.writeUInt16(value, this.bytes, this.position);
        this.position += 2;
    }
    
    /**
     * Writes the given 32-bit signed integer to the stream and advances the
     * position.
     * 
     * @param value The value to write.
     */
    public void writeInt32(int value) {
        if (this.bytes.length - this.position < 4)
            throw new IllegalStateException("Not enough bytes left.");
        
        ByteConverter.writeInt32(value, this.bytes, this.position);
        this.position += 4;
    }
    
    /**
     * Writes the given 32-bit unsigned integer to the stream and advances the
     * position.
     * 
     * @param value The value to write.
     */
    public void writeUInt32(long value) {
        if (this.bytes.length - this.position < 4)
            throw new IllegalStateException("Not enough bytes left.");
        
        ByteConverter.writeUInt32(value, this.bytes, this.position);
        this.position += 4;
    }
    
    /**
     * Writes the given 64-bit signed integer to the stream and advances the
     * position.
     * 
     * @param value The value to write.
     */
    public void writeInt64(long value) {
        if (this.bytes.length - this.position < 8)
            throw new IllegalStateException("Not enough bytes left.");
        
        ByteConverter.writeInt64(value, this.bytes, this.position);
        this.position += 8;
    }
}
