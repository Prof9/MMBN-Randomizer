package rand;

/**
 * Provides various methods for reading and writing from a byte array.
 */
public final class ByteConverter {
    /**
     * Reads the given number of bytes from the given byte array into a signed
     * 64-bit integer.
     * 
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
    
    private static byte[] writeValue(long value, int amount) {
        byte[] bytes = new byte[amount];
        for (int i = 0; i < amount; i++) {
            bytes[i] = (byte)((value >> (i * 8)) & 0xFF);
        }
        return bytes;
    }
    
    public static byte readInt8(byte[] bytes, int offset) {
        return bytes[offset];
    }
    
    public static short readUInt8(byte[] bytes, int offset) {
        return (short)readValue(bytes, offset, 1);
    }
    
    public static short readInt16(byte[] bytes, int offset) {
        return (short)readValue(bytes, offset, 2);
    }
    
    public static int readUInt16(byte[] bytes, int offset) {
        return (int)readValue(bytes, offset, 2);
    }
    
    public static int readInt32(byte[] bytes, int offset) {
        return (int)readValue(bytes, offset, 4);
    }
    
    public static long readUInt32(byte[] bytes, int offset) {
        return readValue(bytes, offset, 4);
    }
    
    public static long readInt64(byte[] bytes, int offset) {
        return readValue(bytes, offset, 8);
    }
    
    public static int readBits(byte[] bytes, int offset, int shift, int size) {
        offset += shift / 8;
        shift %= 8;
        long v = readValue(bytes, offset, (size + shift + 7) / 8);
        return (int)((v >> shift) & ((1L << size) - 1));
    }
    
    public static byte[] writeInt8(byte value)  {
        return writeValue(value, 1);
    }
    
    public static byte[] writeUInt8(short value) {
        return writeValue(value, 1);
    }
    
    public static byte[] writeInt16(short value) {
        return writeValue(value, 2);
    }
    
    public static byte[] writeUInt16(int value) {
        return writeValue(value, 2);
    }
    
    public static byte[] writeInt32(int value) {
        return writeValue(value, 4);
    }
    
    public static byte[] writeUInt32(long value) {
        return writeValue(value, 4);
    }
    
    public static byte[] writeInt64(long value) {
        return writeValue(value, 8);
    }
    
    public static byte[] writeBits(int value, int shift, int size) {
        byte[] r = new byte[(size + 7) / 8];
        writeBitsTo(value, r, 0, shift, size);
        return r;
    }
    
    public static void writeInt8To(byte value, byte[] dest, int offset) {
        System.arraycopy(writeInt8(value), 0, dest, offset, 1);
    }
    
    public static void writeUInt8To(short value, byte[] dest, int offset) {
        System.arraycopy(writeUInt8(value), 0, dest, offset, 1);
    }
    
    public static void writeInt16To(short value, byte[] dest, int offset) {
        System.arraycopy(writeInt16(value), 0, dest, offset, 2);
    }
    
    public static void writeUInt16To(int value, byte[] dest, int offset) {
        System.arraycopy(writeUInt16(value), 0, dest, offset, 2);
    }
    
    public static void writeInt32To(int value, byte[] dest, int offset) {
        System.arraycopy(writeInt32(value), 0, dest, offset, 4);
    }
    
    public static void writeUInt32To(long value, byte[] dest, int offset) {
        System.arraycopy(writeUInt32(value), 0, dest, offset, 4);
    }
    
    public static void writeInt64To(long value, byte[] dest, int offset) {
        System.arraycopy(writeInt64(value), 0, dest, offset, 8);
    }
    
    public static void writeBitsTo(int value, byte[] dest, int offset, int shift, int size) {
        while (size > 0) {
            offset += shift / 8;
            shift %= 8;
            
            int next = 8 - shift;
            next = size < next ? size : next;
            
            dest[offset] &= ~(((1 << next) - 1) << shift);
            dest[offset] |= ((value >> shift) & ((1 << next) - 1)) << shift;
            
            size -= next;
            shift += next;
        }
    }
}
