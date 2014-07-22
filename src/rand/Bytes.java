package rand;

/**
 * Provides various methods for reading and writing from a little-endian byte
 * array.
 */
public final class Bytes {
    /**
     * Reads the given number of bytes from the given byte array into a signed
     * 64-bit integer.
     * 
     * @param bytes The byte array to read from.
     * @param offset The position in the byte array from which to begin reading.
     * @param amount The amount of bytes to read.
     * @return The value that was read.
     */
    private static long readValue(byte[] bytes, int offset, int amount) {
        long r = 0;
        for (int i = 0; i < amount; i++) {
            r += (long)(bytes[offset + i] & 0xFF) << (i * 8);
        }
        return r;
    }
    
    /**
     * Converts the given signed 64-bit integer to a byte array with the given
     * length.
     * 
     * @param value The value to convert.
     * @param amount The amount of bytes to convert.
     * @return The resulting byte array.
     */
    private static byte[] convertValue(long value, int amount) {
        byte[] bytes = new byte[amount];
        for (int i = 0; i < amount; i++) {
            bytes[i] = (byte)((value >> (i * 8)) & 0xFF);
        }
        return bytes;
    }
    
    /**
     * Reads an 8-bit signed integer from the given byte array.
     * 
     * @param bytes The byte array to read from.
     * @param offset The position in the byte array from which to begin reading.
     * @return The value that was read.
     */
    public static byte readInt8(byte[] bytes, int offset) {
        return bytes[offset];
    }
    
    /**
     * Reads an 8-bit unsigned integer from the given byte array.
     * 
     * @param bytes The byte array to read from.
     * @param offset The position in the byte array from which to begin reading.
     * @return The value that was read.
     */
    public static short readUInt8(byte[] bytes, int offset) {
        return (short)readValue(bytes, offset, 1);
    }
    
    /**
     * Reads a 16-bit signed integer from the given byte array.
     * 
     * @param bytes The byte array to read from.
     * @param offset The position in the byte array from which to begin reading.
     * @return The value that was read.
     */
    public static short readInt16(byte[] bytes, int offset) {
        return (short)readValue(bytes, offset, 2);
    }
    
    /**
     * Reads a 16-bit unsigned integer from the given byte array.
     * 
     * @param bytes The byte array to read from.
     * @param offset The position in the byte array from which to begin reading.
     * @return The value that was read.
     */
    public static int readUInt16(byte[] bytes, int offset) {
        return (int)readValue(bytes, offset, 2);
    }
    
    /**
     * Reads a 32-bit signed integer from the given byte array.
     * 
     * @param bytes The byte array to read from.
     * @param offset The position in the byte array from which to begin reading.
     * @return The value that was read.
     */
    public static int readInt32(byte[] bytes, int offset) {
        return (int)readValue(bytes, offset, 4);
    }
    
    /**
     * Reads a 32-bit unsigned integer from the given byte array.
     * 
     * @param bytes The byte array to read from.
     * @param offset The position in the byte array from which to begin reading.
     * @return The value that was read.
     */
    public static long readUInt32(byte[] bytes, int offset) {
        return readValue(bytes, offset, 4);
    }
    
    /**
     * Reads a 64-bit signed integer from the given byte array.
     * 
     * @param bytes The byte array to read from.
     * @param offset The position in the byte array from which to begin reading.
     * @return The value that was read.
     */
    public static long readInt64(byte[] bytes, int offset) {
        return readValue(bytes, offset, 8);
    }
    
    /**
     * Reads the given number of bits from the given byte array into a 32-bit
     * signed integer.
     * 
     * @param bytes The byte array to read from.
     * @param offset The position in the byte array from which to begin reading.
     * @param shift The amount of bits to shift the value to the right.
     * @param size The amount of bits to read.
     * @return The value that was read.
     */
    public static int readBits(byte[] bytes, int offset, int shift, int size) {
        offset += shift / 8;
        shift %= 8;
        long v = readValue(bytes, offset, (size + shift + 7) / 8);
        return (int)((v >> shift) & ((1L << size) - 1));
    }
    
    /**
     * Converts the given 8-bit signed integer to a byte array.
     * 
     * @param value The value to convert.
     * @return The resulting byte array.
     */
    public static byte[] convertInt8(byte value)  {
        return convertValue(value, 1);
    }
    
    /**
     * Converts the given 8-bit unsigned integer to a byte array.
     * 
     * @param value The value to convert.
     * @return The resulting byte array.
     */
    public static byte[] convertUInt8(short value) {
        return convertValue(value, 1);
    }
    
    /**
     * Converts the given 16-bit signed integer to a byte array.
     * 
     * @param value The value to convert.
     * @return The resulting byte array.
     */
    public static byte[] convertInt16(short value) {
        return convertValue(value, 2);
    }
    
    /**
     * Converts the given 16-bit unsigned integer to a byte array.
     * 
     * @param value The value to convert.
     * @return The resulting byte array.
     */
    public static byte[] convertUInt16(int value) {
        return convertValue(value, 2);
    }
    
    /**
     * Converts the given 32-bit signed integer to a byte array.
     * 
     * @param value The value to convert.
     * @return The resulting byte array.
     */
    public static byte[] convertInt32(int value) {
        return convertValue(value, 4);
    }
    
    /**
     * Converts the given 32-bit unsigned integer to a byte array.
     * 
     * @param value The value to convert.
     * @return The resulting byte array.
     */
    public static byte[] convertUInt32(long value) {
        return convertValue(value, 4);
    }
    
    /**
     * Converts the given 64-bit signed integer to a byte array.
     * 
     * @param value The value to convert.
     * @return The resulting byte array.
     */
    public static byte[] convertInt64(long value) {
        return convertValue(value, 8);
    }
    
    /**
     * Converts the given number of bits from the given 32-bit signed integer to
     * a byte array with optional shifting.
     * 
     * @param value The value to convert.
     * @param shift The amount of bits to shift the value to the left.
     * @param size The amount of bits to convert.
     * @return The resulting byte array.
     */
    public static byte[] convertBits(int value, int shift, int size) {
        byte[] r = new byte[(shift + size + 7) / 8];
        writeBits(value, r, 0, shift, size);
        return r;
    }
    
    /**
     * Writes the given 8-bit signed integer to the given byte array at the
     * given position.
     * 
     * @param value The value to write.
     * @param dest The byte array to write to.
     * @param offset The position in the byte array from which to begin writing.
     */
    public static void writeInt8(byte value, byte[] dest, int offset) {
        System.arraycopy(convertInt8(value), 0, dest, offset, 1);
    }
    
    /**
     * Writes the given 8-bit unsigned integer to the given byte array at the
     * given position.
     * 
     * @param value The value to write.
     * @param dest The byte array to write to.
     * @param offset The position in the byte array from which to begin writing.
     */
    public static void writeUInt8(short value, byte[] dest, int offset) {
        System.arraycopy(convertUInt8(value), 0, dest, offset, 1);
    }
    
    /**
     * Writes the given 16-bit signed integer to the given byte array at the
     * given position.
     * 
     * @param value The value to write.
     * @param dest The byte array to write to.
     * @param offset The position in the byte array from which to begin writing.
     */
    public static void writeInt16(short value, byte[] dest, int offset) {
        System.arraycopy(convertInt16(value), 0, dest, offset, 2);
    }
    
    /**
     * Writes the given 16-bit unsigned integer to the given byte array at the
     * given position.
     * 
     * @param value The value to write.
     * @param dest The byte array to write to.
     * @param offset The position in the byte array from which to begin writing.
     */
    public static void writeUInt16(int value, byte[] dest, int offset) {
        System.arraycopy(convertUInt16(value), 0, dest, offset, 2);
    }
    
    /**
     * Writes the given 32-bit signed integer to the given byte array at the
     * given position.
     * 
     * @param value The value to write.
     * @param dest The byte array to write to.
     * @param offset The position in the byte array from which to begin writing.
     */
    public static void writeInt32(int value, byte[] dest, int offset) {
        System.arraycopy(convertInt32(value), 0, dest, offset, 4);
    }
    
    /**
     * Writes the given 32-bit unsigned integer to the given byte array at the
     * given position.
     * 
     * @param value The value to write.
     * @param dest The byte array to write to.
     * @param offset The position in the byte array from which to begin writing.
     */
    public static void writeUInt32(long value, byte[] dest, int offset) {
        System.arraycopy(convertUInt32(value), 0, dest, offset, 4);
    }
    
    /**
     * Writes the given 64-bit signed integer to the given byte array at the
     * given position.
     * 
     * @param value The value to write.
     * @param dest The byte array to write to.
     * @param offset The position in the byte array from which to begin writing.
     */
    public static void writeInt64(long value, byte[] dest, int offset) {
        System.arraycopy(convertInt64(value), 0, dest, offset, 8);
    }
    
    /**
     * Writes the given number of bits from the given 32-bit signed integer to
     * the given byte array with optional shifting.
     * 
     * @param value The value to write.
     * @param dest The byte array to write to.
     * @param offset The position in the byte array from which to begin writing.
     * @param shift The amount of bits to shift the value to the left.
     * @param size The amount of bits to write.
     */
    public static void writeBits(int value, byte[] dest, int offset, int shift, int size) {
        while (size > 0) {
            offset += shift / 8;
            shift %= 8;
            
            int next = 8 - (shift & 7);
            next = size < next ? size : next;
            
            dest[offset] &= ~(((1 << next) - 1) << shift);
            dest[offset] |= (value & ((1 << next) - 1)) << shift;
            value >>= next;
            
            size -= next;
            shift += next;
        }
    }
}
