package rand;

import org.junit.Test;
import static org.junit.Assert.*;

public class ByteConverterTest {    
    /**
     * Test of readInt8 method, of class ByteConverter.
     */
    @Test
    public void testReadInt8() {
        System.out.println("readInt8");
        byte[] bytes = new byte[] { 0, 1, -1, 127, -128 };
        assertEquals(0, ByteConverter.readInt8(bytes, 0));
        assertEquals(1, ByteConverter.readInt8(bytes, 1));
        assertEquals(-1, ByteConverter.readInt8(bytes, 2));
        assertEquals(127, ByteConverter.readInt8(bytes, 3));
        assertEquals(-128, ByteConverter.readInt8(bytes, 4));
    }

    /**
     * Test of readUInt8 method, of class ByteConverter.
     */
    @Test
    public void testReadUInt8() {
        System.out.println("readUInt8");
        byte[] bytes = new byte[] { 0, 1, -1, 127, -128 };
        assertEquals(0, ByteConverter.readUInt8(bytes, 0));
        assertEquals(1, ByteConverter.readUInt8(bytes, 1));
        assertEquals(255, ByteConverter.readUInt8(bytes, 2));
        assertEquals(127, ByteConverter.readUInt8(bytes, 3));
        assertEquals(128, ByteConverter.readUInt8(bytes, 4));
    }

    /**
     * Test of readInt16 method, of class ByteConverter.
     */
    @Test
    public void testReadInt16() {
        System.out.println("readInt16");
        byte[] bytes = new byte[] { 0, 0, 1, 0, -1, -1, 127, 0, -128 };
        assertEquals(0, ByteConverter.readInt16(bytes, 0));
        assertEquals(1, ByteConverter.readInt16(bytes, 2));
        assertEquals(-1, ByteConverter.readInt16(bytes, 4));
        assertEquals(32767, ByteConverter.readInt16(bytes, 5));
        assertEquals(-32768, ByteConverter.readInt16(bytes, 7));
    }

    /**
     * Test of readUInt16 method, of class ByteConverter.
     */
    @Test
    public void testReadUInt16() {
        System.out.println("readUInt16");
        byte[] bytes = new byte[] { 0, 0, 1, 0, -1, -1, 127, 0, -128 };
        assertEquals(0, ByteConverter.readUInt16(bytes, 0));
        assertEquals(1, ByteConverter.readUInt16(bytes, 2));
        assertEquals(65535, ByteConverter.readUInt16(bytes, 4));
        assertEquals(32767, ByteConverter.readUInt16(bytes, 5));
        assertEquals(32768, ByteConverter.readUInt16(bytes, 7));
    }

    /**
     * Test of readInt32 method, of class ByteConverter.
     */
    @Test
    public void testReadInt32() {
        System.out.println("readInt32");
        byte[] bytes = new byte[] {0, 0, 0, 0, 1, 0, 0, 0, -1, -1, -1, -1, 127,
            0, 0, 0, -128 };
        assertEquals(0, ByteConverter.readInt32(bytes, 0));
        assertEquals(1, ByteConverter.readInt32(bytes, 4));
        assertEquals(-1, ByteConverter.readInt32(bytes, 8));
        assertEquals(2147483647, ByteConverter.readInt32(bytes, 9));
        assertEquals(-2147483648, ByteConverter.readInt32(bytes, 13));
    }

    /**
     * Test of readUInt32 method, of class ByteConverter.
     */
    @Test
    public void testReadUInt32() {
        System.out.println("readUInt32");
        byte[] bytes = new byte[] { 0, 0, 0, 0, 1, 0, 0, 0, -1, -1, -1, -1, 127,
            0, 0, 0, -128 };
        assertEquals(0L, ByteConverter.readUInt32(bytes, 0));
        assertEquals(1L, ByteConverter.readUInt32(bytes, 4));
        assertEquals(4294967295L, ByteConverter.readUInt32(bytes, 8));
        assertEquals(2147483647L, ByteConverter.readUInt32(bytes, 9));
        assertEquals(2147483648L, ByteConverter.readUInt32(bytes, 13));
    }

    /**
     * Test of readInt64 method, of class ByteConverter.
     */
    @Test
    public void testReadInt64() {
        System.out.println("readInt64");
        byte[] bytes = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
                0, -1, -1, -1, -1, -1, -1, -1, -1, 127, 0, 0, 0, 0, 0, 0, 0,
                -128, };
        assertEquals(0L, ByteConverter.readInt64(bytes, 0));
        assertEquals(1L, ByteConverter.readInt64(bytes, 8));
        assertEquals(-1L, ByteConverter.readInt64(bytes, 16));
        assertEquals(9223372036854775807L, ByteConverter.readInt64(bytes, 17));
        assertEquals(-9223372036854775808L, ByteConverter.readInt64(bytes, 25));
    }

    /**
     * Test of readBits method, of class ByteConverter.
     */
    @Test
    public void testReadBits() {
        System.out.println("readBits");
        byte[] bytes = new byte[] { 0, -32, -1, -25, 85, -86, 85, 31 };
        assertEquals(16383, ByteConverter.readBits(bytes, 0, 13, 14)); 
        assertEquals(-89304401, ByteConverter.readBits(bytes, 3, 5, 32));
    }

    /**
     * Test of writeInt8 method, of class ByteConverter.
     */
    @Test
    public void testWriteInt8() {
        System.out.println("writeInt8");
        byte[] bytes;
        bytes = new byte[] { 0 };
        assertArrayEquals(bytes, ByteConverter.writeInt8((byte)0));
        bytes = new byte[] { 1 };
        assertArrayEquals(bytes, ByteConverter.writeInt8((byte)1));
        bytes = new byte[] { -1 };
        assertArrayEquals(bytes, ByteConverter.writeInt8((byte)-1));
        bytes = new byte[] { 127 };
        assertArrayEquals(bytes, ByteConverter.writeInt8((byte)127));
        bytes = new byte[] { -128 };
        assertArrayEquals(bytes, ByteConverter.writeInt8((byte)-128));
    }

    /**
     * Test of writeUInt8 method, of class ByteConverter.
     */
    @Test
    public void testWriteUInt8() {
        System.out.println("writeUInt8");
        byte[] bytes;
        bytes = new byte[] { 0 };
        assertArrayEquals(bytes, ByteConverter.writeUInt8((short)0));
        bytes = new byte[] { 1 };
        assertArrayEquals(bytes, ByteConverter.writeUInt8((short)1));
        bytes = new byte[] { -1 };
        assertArrayEquals(bytes, ByteConverter.writeUInt8((short)255));
        bytes = new byte[] { 127 };
        assertArrayEquals(bytes, ByteConverter.writeUInt8((short)127));
        bytes = new byte[] { -128 };
        assertArrayEquals(bytes, ByteConverter.writeUInt8((short)128));
    }

    /**
     * Test of writeInt16 method, of class ByteConverter.
     */
    @Test
    public void testWriteInt16() {
        System.out.println("writeInt16");
        byte[] bytes;
        bytes = new byte[] { 0, 0 };
        assertArrayEquals(bytes, ByteConverter.writeInt16((short)0));
        bytes = new byte[] { 1, 0 };
        assertArrayEquals(bytes, ByteConverter.writeInt16((short)1));
        bytes = new byte[] { -1, -1 };
        assertArrayEquals(bytes, ByteConverter.writeInt16((short)-1));
        bytes = new byte[] { -1, 127 };
        assertArrayEquals(bytes, ByteConverter.writeInt16((short)32767));
        bytes = new byte[] { 0, -128 };
        assertArrayEquals(bytes, ByteConverter.writeInt16((short)-32768));
    }

    /**
     * Test of writeUInt16 method, of class ByteConverter.
     */
    @Test
    public void testWriteUInt16() {
        System.out.println("writeUInt16");
        byte[] bytes;
        bytes = new byte[] { 0, 0 };
        assertArrayEquals(bytes, ByteConverter.writeUInt16(0));
        bytes = new byte[] { 1, 0 };
        assertArrayEquals(bytes, ByteConverter.writeUInt16(1));
        bytes = new byte[] { -1, -1 };
        assertArrayEquals(bytes, ByteConverter.writeUInt16(65535));
        bytes = new byte[] { -1, 127 };
        assertArrayEquals(bytes, ByteConverter.writeUInt16(32767));
        bytes = new byte[] { 0, -128 };
        assertArrayEquals(bytes, ByteConverter.writeUInt16(32768));
    }

    /**
     * Test of writeInt32 method, of class ByteConverter.
     */
    @Test
    public void testWriteInt32() {
        System.out.println("writeInt32");
        byte[] bytes;
        bytes = new byte[] { 0, 0, 0, 0 };
        assertArrayEquals(bytes, ByteConverter.writeInt32(0));
        bytes = new byte[] { 1, 0, 0, 0 };
        assertArrayEquals(bytes, ByteConverter.writeInt32(1));
        bytes = new byte[] { -1, -1, -1, -1 };
        assertArrayEquals(bytes, ByteConverter.writeInt32(-1));
        bytes = new byte[] { -1, -1, -1, 127 };
        assertArrayEquals(bytes, ByteConverter.writeInt32(2147483647));
        bytes = new byte[] { 0, 0, 0, -128 };
        assertArrayEquals(bytes, ByteConverter.writeInt32(-2147483648));
    }

    /**
     * Test of writeUInt32 method, of class ByteConverter.
     */
    @Test
    public void testWriteUInt32() {
        System.out.println("writeUInt32");
        byte[] bytes;
        bytes = new byte[] { 0, 0, 0, 0 };
        assertArrayEquals(bytes, ByteConverter.writeUInt32(0L));
        bytes = new byte[] { 1, 0, 0, 0 };
        assertArrayEquals(bytes, ByteConverter.writeUInt32(1L));
        bytes = new byte[] { -1, -1, -1, -1 };
        assertArrayEquals(bytes, ByteConverter.writeUInt32(4294967295L));
        bytes = new byte[] { -1, -1, -1, 127 };
        assertArrayEquals(bytes, ByteConverter.writeUInt32(2147483647L));
        bytes = new byte[] { 0, 0, 0, -128 };
        assertArrayEquals(bytes, ByteConverter.writeUInt32(2147483648L));
    }

    /**
     * Test of writeInt64 method, of class ByteConverter.
     */
    @Test
    public void testWriteInt64() {
        System.out.println("writeInt64");
        byte[] bytes;
        bytes = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        assertArrayEquals(bytes, ByteConverter.writeInt64(0L));
        bytes = new byte[] { 1, 0, 0, 0, 0, 0, 0, 0 };
        assertArrayEquals(bytes, ByteConverter.writeInt64(1L));
        bytes = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1 };
        assertArrayEquals(bytes, ByteConverter.writeInt64(-1L));
        bytes = new byte[] { -1, -1, -1, -1, -1, -1, -1, 127 };
        assertArrayEquals(bytes, ByteConverter.writeInt64(9223372036854775807L));
        bytes = new byte[] { 0, 0, 0, 0, 0, 0, 0, -128 };
        assertArrayEquals(bytes, ByteConverter.writeInt64(-9223372036854775808L));
    }

    /**
     * Test of writeBits method, of class ByteConverter.
     */
    @Test
    public void testWriteBits() {
        System.out.println("writeBits");
        byte[] bytes = new byte[] { -32, -1, 7 };
        assertArrayEquals(bytes, ByteConverter.writeBits(16383, 5, 14));
    }

    /**
     * Test of writeInt8To method, of class ByteConverter.
     */
    @Test
    public void testWriteInt8To() {
        System.out.println("writeInt8To");
        byte value = 0;
        byte[] dest = null;
        int offset = 0;
        ByteConverter.writeInt8To(value, dest, offset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeUInt8To method, of class ByteConverter.
     */
    @Test
    public void testWriteUInt8To() {
        System.out.println("writeUInt8To");
        short value = 0;
        byte[] dest = null;
        int offset = 0;
        ByteConverter.writeUInt8To(value, dest, offset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeInt16To method, of class ByteConverter.
     */
    @Test
    public void testWriteInt16To() {
        System.out.println("writeInt16To");
        short value = 0;
        byte[] dest = null;
        int offset = 0;
        ByteConverter.writeInt16To(value, dest, offset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeUInt16To method, of class ByteConverter.
     */
    @Test
    public void testWriteUInt16To() {
        System.out.println("writeUInt16To");
        int value = 0;
        byte[] dest = null;
        int offset = 0;
        ByteConverter.writeUInt16To(value, dest, offset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeInt32To method, of class ByteConverter.
     */
    @Test
    public void testWriteInt32To() {
        System.out.println("writeInt32To");
        int value = 0;
        byte[] dest = null;
        int offset = 0;
        ByteConverter.writeInt32To(value, dest, offset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeUInt32To method, of class ByteConverter.
     */
    @Test
    public void testWriteUInt32To() {
        System.out.println("writeUInt32To");
        long value = 0L;
        byte[] dest = null;
        int offset = 0;
        ByteConverter.writeUInt32To(value, dest, offset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeInt64To method, of class ByteConverter.
     */
    @Test
    public void testWriteInt64To() {
        System.out.println("writeInt64To");
        long value = 0L;
        byte[] dest = null;
        int offset = 0;
        ByteConverter.writeInt64To(value, dest, offset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeBitsTo method, of class ByteConverter.
     */
    @Test
    public void testWriteBitsTo() {
        System.out.println("writeBitsTo");
        int value = 0;
        byte[] dest = null;
        int offset = 0;
        int shift = 0;
        int size = 0;
        ByteConverter.writeBitsTo(value, dest, offset, shift, size);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
