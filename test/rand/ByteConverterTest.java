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
        byte[] bytes = new byte[] {
            (byte)0x00, (byte)0x01, (byte)0xFF, (byte)0x7F,
            (byte)0x80
        };
        assertEquals((byte)0x00, ByteConverter.readInt8(bytes, 0));
        assertEquals((byte)0x01, ByteConverter.readInt8(bytes, 1));
        assertEquals((byte)0xFF, ByteConverter.readInt8(bytes, 2));
        assertEquals((byte)0x7F, ByteConverter.readInt8(bytes, 3));
        assertEquals((byte)0x80, ByteConverter.readInt8(bytes, 4));
    }

    /**
     * Test of readUInt8 method, of class ByteConverter.
     */
    @Test
    public void testReadUInt8() {
        System.out.println("readUInt8");
        byte[] bytes = new byte[] {
            (byte)0x00, (byte)0x01, (byte)0xFF, (byte)0x7F,
            (byte)0x80
        };
        assertEquals((short)0x00, ByteConverter.readUInt8(bytes, 0));
        assertEquals((short)0x01, ByteConverter.readUInt8(bytes, 1));
        assertEquals((short)0xFF, ByteConverter.readUInt8(bytes, 2));
        assertEquals((short)0x7F, ByteConverter.readUInt8(bytes, 3));
        assertEquals((short)0x80, ByteConverter.readUInt8(bytes, 4));
    }

    /**
     * Test of readInt16 method, of class ByteConverter.
     */
    @Test
    public void testReadInt16() {
        System.out.println("readInt16");
        byte[] bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00,
            (byte)0xFF, (byte)0xFF, (byte)0x7F, (byte)0x00,
            (byte)0x80
        };
        assertEquals((short)0x0000, ByteConverter.readInt16(bytes, 0));
        assertEquals((short)0x0001, ByteConverter.readInt16(bytes, 2));
        assertEquals((short)0xFFFF, ByteConverter.readInt16(bytes, 4));
        assertEquals((short)0x7FFF, ByteConverter.readInt16(bytes, 5));
        assertEquals((short)0x8000, ByteConverter.readInt16(bytes, 7));
    }

    /**
     * Test of readUInt16 method, of class ByteConverter.
     */
    @Test
    public void testReadUInt16() {
        System.out.println("readUInt16");
        byte[] bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00,
            (byte)0xFF, (byte)0xFF, (byte)0x7F, (byte)0x00,
            (byte)0x80
        };
        assertEquals(0x0000, ByteConverter.readUInt16(bytes, 0));
        assertEquals(0x0001, ByteConverter.readUInt16(bytes, 2));
        assertEquals(0xFFFF, ByteConverter.readUInt16(bytes, 4));
        assertEquals(0x7FFF, ByteConverter.readUInt16(bytes, 5));
        assertEquals(0x8000, ByteConverter.readUInt16(bytes, 7));
    }

    /**
     * Test of readInt32 method, of class ByteConverter.
     */
    @Test
    public void testReadInt32() {
        System.out.println("readInt32");
        byte[] bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0x7F, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x80
        };
        assertEquals(0x00000000, ByteConverter.readInt32(bytes,  0));
        assertEquals(0x00000001, ByteConverter.readInt32(bytes,  4));
        assertEquals(0xFFFFFFFF, ByteConverter.readInt32(bytes,  8));
        assertEquals(0x7FFFFFFF, ByteConverter.readInt32(bytes,  9));
        assertEquals(0x80000000, ByteConverter.readInt32(bytes, 13));
    }

    /**
     * Test of readUInt32 method, of class ByteConverter.
     */
    @Test
    public void testReadUInt32() {
        System.out.println("readUInt32");
        byte[] bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0x7F, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x80
        };
        assertEquals(0x00000000L, ByteConverter.readUInt32(bytes,  0));
        assertEquals(0x00000001L, ByteConverter.readUInt32(bytes,  4));
        assertEquals(0xFFFFFFFFL, ByteConverter.readUInt32(bytes,  8));
        assertEquals(0x7FFFFFFFL, ByteConverter.readUInt32(bytes,  9));
        assertEquals(0x80000000L, ByteConverter.readUInt32(bytes, 13));
    }

    /**
     * Test of readInt64 method, of class ByteConverter.
     */
    @Test
    public void testReadInt64() {
        System.out.println("readInt64");
        byte[] bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0x7F, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x80
        };
        assertEquals(0x0000000000000000L, ByteConverter.readInt64(bytes,  0));
        assertEquals(0x0000000000000001L, ByteConverter.readInt64(bytes,  8));
        assertEquals(0xFFFFFFFFFFFFFFFFL, ByteConverter.readInt64(bytes, 16));
        assertEquals(0x7FFFFFFFFFFFFFFFL, ByteConverter.readInt64(bytes, 17));
        assertEquals(0x8000000000000000L, ByteConverter.readInt64(bytes, 25));
    }

    /**
     * Test of readBits method, of class ByteConverter.
     */
    @Test
    public void testReadBits() {
        System.out.println("readBits");
        byte[] bytes = new byte[] {
            (byte)0x00, (byte)0xE0, (byte)0xFF, (byte)0xE7,
            (byte)0x55, (byte)0xAA, (byte)0x55, (byte)0x1F
        };
        assertEquals((int)(0x0007FFE000 >> 13),
                ByteConverter.readBits(bytes, 0, 13, 14)); 
        assertEquals((int)(0x1F55AA55E0L >> 5),
                ByteConverter.readBits(bytes, 3,  5, 32));
    }

    /**
     * Test of writeInt8 method, of class ByteConverter.
     */
    @Test
    public void testWriteInt8() {
        System.out.println("writeInt8");
        byte[] bytes;
        bytes = new byte[] { (byte)0x00 };
        assertArrayEquals(bytes, ByteConverter.writeInt8((byte)0x00));
        bytes = new byte[] { (byte)0x01 };
        assertArrayEquals(bytes, ByteConverter.writeInt8((byte)0x01));
        bytes = new byte[] { (byte)0xFF };
        assertArrayEquals(bytes, ByteConverter.writeInt8((byte)0xFF));
        bytes = new byte[] { (byte)0x7F };
        assertArrayEquals(bytes, ByteConverter.writeInt8((byte)0x7F));
        bytes = new byte[] { (byte)0x80 };
        assertArrayEquals(bytes, ByteConverter.writeInt8((byte)0x80));
    }

    /**
     * Test of writeUInt8 method, of class ByteConverter.
     */
    @Test
    public void testWriteUInt8() {
        System.out.println("writeUInt8");
        byte[] bytes;
        bytes = new byte[] { (byte)0x00 };
        assertArrayEquals(bytes, ByteConverter.writeUInt8((short)0x00));
        bytes = new byte[] { (byte)0x01 };
        assertArrayEquals(bytes, ByteConverter.writeUInt8((short)0x01));
        bytes = new byte[] { (byte)0xFF };
        assertArrayEquals(bytes, ByteConverter.writeUInt8((short)0xFF));
        bytes = new byte[] { (byte)0x7F };
        assertArrayEquals(bytes, ByteConverter.writeUInt8((short)0x7F));
        bytes = new byte[] { (byte)0x80 };
        assertArrayEquals(bytes, ByteConverter.writeUInt8((short)0x80));
    }

    /**
     * Test of writeInt16 method, of class ByteConverter.
     */
    @Test
    public void testWriteInt16() {
        System.out.println("writeInt16");
        byte[] bytes;
        bytes = new byte[] { (byte)0x00, (byte)0x00 };
        assertArrayEquals(bytes, ByteConverter.writeInt16((short)0x0000));
        bytes = new byte[] { (byte)0x01, (byte)0x00 };
        assertArrayEquals(bytes, ByteConverter.writeInt16((short)0x0001));
        bytes = new byte[] { (byte)0xFF, (byte)0xFF };
        assertArrayEquals(bytes, ByteConverter.writeInt16((short)0xFFFF));
        bytes = new byte[] { (byte)0xFF, (byte)0x7F };
        assertArrayEquals(bytes, ByteConverter.writeInt16((short)0x7FFF));
        bytes = new byte[] { (byte)0x00, (byte)0x80 };
        assertArrayEquals(bytes, ByteConverter.writeInt16((short)0x8000));
    }

    /**
     * Test of writeUInt16 method, of class ByteConverter.
     */
    @Test
    public void testWriteUInt16() {
        System.out.println("writeUInt16");
        byte[] bytes;
        bytes = new byte[] { (byte)0x00, (byte)0x00 };
        assertArrayEquals(bytes, ByteConverter.writeUInt16(0x0000));
        bytes = new byte[] { (byte)0x01, (byte)0x00 };
        assertArrayEquals(bytes, ByteConverter.writeUInt16(0x0001));
        bytes = new byte[] { (byte)0xFF, (byte)0xFF };
        assertArrayEquals(bytes, ByteConverter.writeUInt16(0xFFFF));
        bytes = new byte[] { (byte)0xFF, (byte)0x7F };
        assertArrayEquals(bytes, ByteConverter.writeUInt16(0x7FFF));
        bytes = new byte[] { (byte)0x00, (byte)0x80 };
        assertArrayEquals(bytes, ByteConverter.writeUInt16(0x8000));
    }

    /**
     * Test of writeInt32 method, of class ByteConverter.
     */
    @Test
    public void testWriteInt32() {
        System.out.println("writeInt32");
        byte[] bytes;
        bytes = new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00 };
        assertArrayEquals(bytes, ByteConverter.writeInt32(0x00000000));
        bytes = new byte[] { (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00 };
        assertArrayEquals(bytes, ByteConverter.writeInt32(0x00000001));
        bytes = new byte[] { (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF };
        assertArrayEquals(bytes, ByteConverter.writeInt32(0xFFFFFFFF));
        bytes = new byte[] { (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x7F };
        assertArrayEquals(bytes, ByteConverter.writeInt32(0x7FFFFFFF));
        bytes = new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x80 };
        assertArrayEquals(bytes, ByteConverter.writeInt32(0x80000000));
    }

    /**
     * Test of writeUInt32 method, of class ByteConverter.
     */
    @Test
    public void testWriteUInt32() {
        System.out.println("writeUInt32");
        byte[] bytes;
        bytes = new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00 };
        assertArrayEquals(bytes, ByteConverter.writeUInt32(0x00000000L));
        bytes = new byte[] { (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00 };
        assertArrayEquals(bytes, ByteConverter.writeUInt32(0x00000001L));
        bytes = new byte[] { (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF };
        assertArrayEquals(bytes, ByteConverter.writeUInt32(0xFFFFFFFFL));
        bytes = new byte[] { (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x7F };
        assertArrayEquals(bytes, ByteConverter.writeUInt32(0x7FFFFFFFL));
        bytes = new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x80 };
        assertArrayEquals(bytes, ByteConverter.writeUInt32(0x80000000L));
    }

    /**
     * Test of writeInt64 method, of class ByteConverter.
     */
    @Test
    public void testWriteInt64() {
        System.out.println("writeInt64");
        byte[] bytes;
        bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
        };
        assertArrayEquals(bytes, ByteConverter.writeInt64(0x0000000000000000L));
        bytes = new byte[] {
            (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
        };
        assertArrayEquals(bytes, ByteConverter.writeInt64(0x0000000000000001L));
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF
        };
        assertArrayEquals(bytes, ByteConverter.writeInt64(0xFFFFFFFFFFFFFFFFL));
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x7F
        };
        assertArrayEquals(bytes, ByteConverter.writeInt64(0x7FFFFFFFFFFFFFFFL));
        bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x80
        };
        assertArrayEquals(bytes, ByteConverter.writeInt64(0x8000000000000000L));
    }

    /**
     * Test of writeBits method, of class ByteConverter.
     */
    @Test
    public void testWriteBits() {
        System.out.println("writeBits");
        byte[] bytes;
        
        bytes = new byte[] { (byte)0xE0, (byte)0xFF, (byte)0x07 };
        assertArrayEquals(bytes,
                ByteConverter.writeBits((int)(0x07FFE0 >> 5), 5, 14));
        
        bytes = new byte[] {
            (byte)0x00, (byte)0xF8, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0x03
        };
        assertArrayEquals(bytes,
                ByteConverter.writeBits((int)(0x03FFFFFFF800L >> 11), 11, 32));
        
        bytes = new byte[] {
            (byte)0x00, (byte)0xF8, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0x07
        };
        assertArrayEquals(bytes,
                ByteConverter.writeBits((int)(0x07FFFFFFF800L >> 11), 11, 32));
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
