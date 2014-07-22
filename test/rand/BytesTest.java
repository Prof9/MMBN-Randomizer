package rand;

import org.junit.Test;
import static org.junit.Assert.*;

public class BytesTest {    
    /**
     * Test of readInt8 method, of class Bytes.
     */
    @Test
    public void testReadInt8() {
        System.out.println("readInt8");
        byte[] bytes = new byte[] {
            (byte)0x00, (byte)0x01, (byte)0xFF, (byte)0x7F,
            (byte)0x80
        };
        assertEquals((byte)0x00, Bytes.readInt8(bytes, 0));
        assertEquals((byte)0x01, Bytes.readInt8(bytes, 1));
        assertEquals((byte)0xFF, Bytes.readInt8(bytes, 2));
        assertEquals((byte)0x7F, Bytes.readInt8(bytes, 3));
        assertEquals((byte)0x80, Bytes.readInt8(bytes, 4));
    }

    /**
     * Test of readUInt8 method, of class Bytes.
     */
    @Test
    public void testReadUInt8() {
        System.out.println("readUInt8");
        byte[] bytes = new byte[] {
            (byte)0x00, (byte)0x01, (byte)0xFF, (byte)0x7F,
            (byte)0x80
        };
        assertEquals((short)0x00, Bytes.readUInt8(bytes, 0));
        assertEquals((short)0x01, Bytes.readUInt8(bytes, 1));
        assertEquals((short)0xFF, Bytes.readUInt8(bytes, 2));
        assertEquals((short)0x7F, Bytes.readUInt8(bytes, 3));
        assertEquals((short)0x80, Bytes.readUInt8(bytes, 4));
    }

    /**
     * Test of readInt16 method, of class Bytes.
     */
    @Test
    public void testReadInt16() {
        System.out.println("readInt16");
        byte[] bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00,
            (byte)0xFF, (byte)0xFF, (byte)0x7F, (byte)0x00,
            (byte)0x80
        };
        assertEquals((short)0x0000, Bytes.readInt16(bytes, 0));
        assertEquals((short)0x0001, Bytes.readInt16(bytes, 2));
        assertEquals((short)0xFFFF, Bytes.readInt16(bytes, 4));
        assertEquals((short)0x7FFF, Bytes.readInt16(bytes, 5));
        assertEquals((short)0x8000, Bytes.readInt16(bytes, 7));
    }

    /**
     * Test of readUInt16 method, of class Bytes.
     */
    @Test
    public void testReadUInt16() {
        System.out.println("readUInt16");
        byte[] bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00,
            (byte)0xFF, (byte)0xFF, (byte)0x7F, (byte)0x00,
            (byte)0x80
        };
        assertEquals(0x0000, Bytes.readUInt16(bytes, 0));
        assertEquals(0x0001, Bytes.readUInt16(bytes, 2));
        assertEquals(0xFFFF, Bytes.readUInt16(bytes, 4));
        assertEquals(0x7FFF, Bytes.readUInt16(bytes, 5));
        assertEquals(0x8000, Bytes.readUInt16(bytes, 7));
    }

    /**
     * Test of readInt32 method, of class Bytes.
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
        assertEquals(0x00000000, Bytes.readInt32(bytes,  0));
        assertEquals(0x00000001, Bytes.readInt32(bytes,  4));
        assertEquals(0xFFFFFFFF, Bytes.readInt32(bytes,  8));
        assertEquals(0x7FFFFFFF, Bytes.readInt32(bytes,  9));
        assertEquals(0x80000000, Bytes.readInt32(bytes, 13));
    }

    /**
     * Test of readUInt32 method, of class Bytes.
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
        assertEquals(0x00000000L, Bytes.readUInt32(bytes,  0));
        assertEquals(0x00000001L, Bytes.readUInt32(bytes,  4));
        assertEquals(0xFFFFFFFFL, Bytes.readUInt32(bytes,  8));
        assertEquals(0x7FFFFFFFL, Bytes.readUInt32(bytes,  9));
        assertEquals(0x80000000L, Bytes.readUInt32(bytes, 13));
    }

    /**
     * Test of readInt64 method, of class Bytes.
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
        assertEquals(0x0000000000000000L, Bytes.readInt64(bytes,  0));
        assertEquals(0x0000000000000001L, Bytes.readInt64(bytes,  8));
        assertEquals(0xFFFFFFFFFFFFFFFFL, Bytes.readInt64(bytes, 16));
        assertEquals(0x7FFFFFFFFFFFFFFFL, Bytes.readInt64(bytes, 17));
        assertEquals(0x8000000000000000L, Bytes.readInt64(bytes, 25));
    }

    /**
     * Test of readBits method, of class Bytes.
     */
    @Test
    public void testReadBits() {
        System.out.println("readBits");
        byte[] bytes = new byte[] {
            (byte)0x00, (byte)0xE0, (byte)0xFF, (byte)0xE7,
            (byte)0x55, (byte)0xAA, (byte)0x55, (byte)0x1F
        };
        assertEquals((int)(0x0007FFE000 >> 13),
                Bytes.readBits(bytes, 0, 13, 14)); 
        assertEquals((int)(0x1F55AA55E0L >> 5),
                Bytes.readBits(bytes, 3,  5, 32));
    }

    /**
     * Test of convertInt8 method, of class Bytes.
     */
    @Test
    public void testWriteInt8() {
        System.out.println("writeInt8");
        byte[] bytes;
        bytes = new byte[] { (byte)0x00 };
        assertArrayEquals(bytes, Bytes.convertInt8((byte)0x00));
        bytes = new byte[] { (byte)0x01 };
        assertArrayEquals(bytes, Bytes.convertInt8((byte)0x01));
        bytes = new byte[] { (byte)0xFF };
        assertArrayEquals(bytes, Bytes.convertInt8((byte)0xFF));
        bytes = new byte[] { (byte)0x7F };
        assertArrayEquals(bytes, Bytes.convertInt8((byte)0x7F));
        bytes = new byte[] { (byte)0x80 };
        assertArrayEquals(bytes, Bytes.convertInt8((byte)0x80));
    }

    /**
     * Test of convertUInt8 method, of class Bytes.
     */
    @Test
    public void testWriteUInt8() {
        System.out.println("writeUInt8");
        byte[] bytes;
        bytes = new byte[] { (byte)0x00 };
        assertArrayEquals(bytes, Bytes.convertUInt8((short)0x00));
        bytes = new byte[] { (byte)0x01 };
        assertArrayEquals(bytes, Bytes.convertUInt8((short)0x01));
        bytes = new byte[] { (byte)0xFF };
        assertArrayEquals(bytes, Bytes.convertUInt8((short)0xFF));
        bytes = new byte[] { (byte)0x7F };
        assertArrayEquals(bytes, Bytes.convertUInt8((short)0x7F));
        bytes = new byte[] { (byte)0x80 };
        assertArrayEquals(bytes, Bytes.convertUInt8((short)0x80));
    }

    /**
     * Test of convertInt16 method, of class Bytes.
     */
    @Test
    public void testWriteInt16() {
        System.out.println("writeInt16");
        byte[] bytes;
        bytes = new byte[] { (byte)0x00, (byte)0x00 };
        assertArrayEquals(bytes, Bytes.convertInt16((short)0x0000));
        bytes = new byte[] { (byte)0x01, (byte)0x00 };
        assertArrayEquals(bytes, Bytes.convertInt16((short)0x0001));
        bytes = new byte[] { (byte)0xFF, (byte)0xFF };
        assertArrayEquals(bytes, Bytes.convertInt16((short)0xFFFF));
        bytes = new byte[] { (byte)0xFF, (byte)0x7F };
        assertArrayEquals(bytes, Bytes.convertInt16((short)0x7FFF));
        bytes = new byte[] { (byte)0x00, (byte)0x80 };
        assertArrayEquals(bytes, Bytes.convertInt16((short)0x8000));
    }

    /**
     * Test of convertUInt16 method, of class Bytes.
     */
    @Test
    public void testWriteUInt16() {
        System.out.println("writeUInt16");
        byte[] bytes;
        bytes = new byte[] { (byte)0x00, (byte)0x00 };
        assertArrayEquals(bytes, Bytes.convertUInt16(0x0000));
        bytes = new byte[] { (byte)0x01, (byte)0x00 };
        assertArrayEquals(bytes, Bytes.convertUInt16(0x0001));
        bytes = new byte[] { (byte)0xFF, (byte)0xFF };
        assertArrayEquals(bytes, Bytes.convertUInt16(0xFFFF));
        bytes = new byte[] { (byte)0xFF, (byte)0x7F };
        assertArrayEquals(bytes, Bytes.convertUInt16(0x7FFF));
        bytes = new byte[] { (byte)0x00, (byte)0x80 };
        assertArrayEquals(bytes, Bytes.convertUInt16(0x8000));
    }

    /**
     * Test of convertInt32 method, of class Bytes.
     */
    @Test
    public void testWriteInt32() {
        System.out.println("writeInt32");
        byte[] bytes;
        bytes = new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00 };
        assertArrayEquals(bytes, Bytes.convertInt32(0x00000000));
        bytes = new byte[] { (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00 };
        assertArrayEquals(bytes, Bytes.convertInt32(0x00000001));
        bytes = new byte[] { (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF };
        assertArrayEquals(bytes, Bytes.convertInt32(0xFFFFFFFF));
        bytes = new byte[] { (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x7F };
        assertArrayEquals(bytes, Bytes.convertInt32(0x7FFFFFFF));
        bytes = new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x80 };
        assertArrayEquals(bytes, Bytes.convertInt32(0x80000000));
    }

    /**
     * Test of convertUInt32 method, of class Bytes.
     */
    @Test
    public void testWriteUInt32() {
        System.out.println("writeUInt32");
        byte[] bytes;
        bytes = new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00 };
        assertArrayEquals(bytes, Bytes.convertUInt32(0x00000000L));
        bytes = new byte[] { (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00 };
        assertArrayEquals(bytes, Bytes.convertUInt32(0x00000001L));
        bytes = new byte[] { (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF };
        assertArrayEquals(bytes, Bytes.convertUInt32(0xFFFFFFFFL));
        bytes = new byte[] { (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x7F };
        assertArrayEquals(bytes, Bytes.convertUInt32(0x7FFFFFFFL));
        bytes = new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x80 };
        assertArrayEquals(bytes, Bytes.convertUInt32(0x80000000L));
    }

    /**
     * Test of convertInt64 method, of class Bytes.
     */
    @Test
    public void testWriteInt64() {
        System.out.println("writeInt64");
        byte[] bytes;
        bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
        };
        assertArrayEquals(bytes, Bytes.convertInt64(0x0000000000000000L));
        bytes = new byte[] {
            (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
        };
        assertArrayEquals(bytes, Bytes.convertInt64(0x0000000000000001L));
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF
        };
        assertArrayEquals(bytes, Bytes.convertInt64(0xFFFFFFFFFFFFFFFFL));
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x7F
        };
        assertArrayEquals(bytes, Bytes.convertInt64(0x7FFFFFFFFFFFFFFFL));
        bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x80
        };
        assertArrayEquals(bytes, Bytes.convertInt64(0x8000000000000000L));
    }

    /**
     * Test of convertBits method, of class Bytes.
     */
    @Test
    public void testWriteBits() {
        System.out.println("writeBits");
        byte[] bytes;
        bytes = new byte[] { (byte)0xE0, (byte)0xFF, (byte)0x07 };
        assertArrayEquals(bytes,
                Bytes.convertBits((int)(0x07FFE0 >> 5), 5, 14));
        bytes = new byte[] {
            (byte)0x00, (byte)0xF8, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0x03
        };
        assertArrayEquals(bytes,
                Bytes.convertBits((int)(0x03FFFFFFF800L >> 11), 11, 32));
        bytes = new byte[] {
            (byte)0x00, (byte)0xF8, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0x07
        };
        assertArrayEquals(bytes,
                Bytes.convertBits((int)(0x07FFFFFFF800L >> 11), 11, 32));
    }

    /**
     * Test of writeInt8 method, of class Bytes.
     */
    @Test
    public void testWriteInt8To() {
        System.out.println("writeInt8To");
        byte[] bytes;
        byte[] result = new byte[] { (byte)0xFF, (byte)0xFF };
        bytes = new byte[] { (byte)0x00, (byte)0xFF };
        Bytes.writeInt8((byte)0x00, result, 0);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0x00, (byte)0x01 };
        Bytes.writeInt8((byte)0x01, result, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0xFF, (byte)0x01 };
        Bytes.writeInt8((byte)0xFF, result, 0);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0xFF, (byte)0x7F };
        Bytes.writeInt8((byte)0x7F, result, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0x80, (byte)0x7F };
        Bytes.writeInt8((byte)0x80, result, 0);
        assertArrayEquals(bytes, result);
    }

    /**
     * Test of writeUInt8 method, of class Bytes.
     */
    @Test
    public void testWriteUInt8To() {
        System.out.println("writeUInt8To");
        byte[] bytes;
        byte[] result = new byte[] { (byte)0xFF, (byte)0xFF };
        bytes = new byte[] { (byte)0x00, (byte)0xFF };
        Bytes.writeUInt8((byte)0x00, result, 0);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0x00, (byte)0x01 };
        Bytes.writeUInt8((byte)0x01, result, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0xFF, (byte)0x01 };
        Bytes.writeUInt8((byte)0xFF, result, 0);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0xFF, (byte)0x7F };
        Bytes.writeUInt8((byte)0x7F, result, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0x80, (byte)0x7F };
        Bytes.writeUInt8((byte)0x80, result, 0);
        assertArrayEquals(bytes, result);
    }

    /**
     * Test of writeInt16 method, of class Bytes.
     */
    @Test
    public void testWriteInt16To() {
        System.out.println("writeInt16To");
        byte[] bytes;
        byte[] result = new byte[] { (byte)0xFF, (byte)0xFF, (byte)0xFF };
        bytes = new byte[] { (byte)0x00, (byte)0x00, (byte)0xFF };
        Bytes.writeInt16((short)0x0000, result, 0);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0x00, (byte)0x01, (byte)0x00 };
        Bytes.writeInt16((short)0x0001, result, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0xFF, (byte)0xFF, (byte)0x00 };
        Bytes.writeInt16((short)0xFFFF, result, 0);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0xFF, (byte)0xFF, (byte)0x7F };
        Bytes.writeInt16((short)0x7FFF, result, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0x00, (byte)0x80, (byte)0x7F };
        Bytes.writeInt16((short)0x8000, result, 0);
        assertArrayEquals(bytes, result);
    }

    /**
     * Test of writeUInt16 method, of class Bytes.
     */
    @Test
    public void testWriteUInt16To() {
        System.out.println("writeUInt16To");
        byte[] bytes;
        byte[] result = new byte[] { (byte)0xFF, (byte)0xFF, (byte)0xFF };
        bytes = new byte[] { (byte)0x00, (byte)0x00, (byte)0xFF };
        Bytes.writeUInt16(0x0000, result, 0);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0x00, (byte)0x01, (byte)0x00 };
        Bytes.writeUInt16(0x0001, result, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0xFF, (byte)0xFF, (byte)0x00 };
        Bytes.writeUInt16(0xFFFF, result, 0);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0xFF, (byte)0xFF, (byte)0x7F };
        Bytes.writeUInt16(0x7FFF, result, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] { (byte)0x00, (byte)0x80, (byte)0x7F };
        Bytes.writeUInt16(0x8000, result, 0);
        assertArrayEquals(bytes, result);
    }

    /**
     * Test of writeInt32 method, of class Bytes.
     */
    @Test
    public void testWriteInt32To() {
        System.out.println("writeInt32To");
        byte[] bytes;
        byte[] result = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF
        };
        bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0xFF
        };
        Bytes.writeInt32(0x00000000, result, 0);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00,
            (byte)0x00
        };
        Bytes.writeInt32(0x00000001, result, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0x00
        };
        Bytes.writeInt32(0xFFFFFFFF, result, 0);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0x7F
        };
        Bytes.writeInt32(0x7FFFFFFF, result, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x80,
            (byte)0x7F
        };
        Bytes.writeInt32(0x80000000, result, 0);
        assertArrayEquals(bytes, result);
    }

    /**
     * Test of writeUInt32 method, of class Bytes.
     */
    @Test
    public void testWriteUInt32To() {
        System.out.println("writeUInt32To");
        byte[] bytes;
        byte[] result = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF
        };
        bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0xFF
        };
        Bytes.writeUInt32(0x00000000L, result, 0);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00,
            (byte)0x00
        };
        Bytes.writeUInt32(0x00000001L, result, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0x00
        };
        Bytes.writeUInt32(0xFFFFFFFFL, result, 0);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0x7F
        };
        Bytes.writeUInt32(0x7FFFFFFFL, result, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x80,
            (byte)0x7F
        };
        Bytes.writeUInt32(0x80000000L, result, 0);
        assertArrayEquals(bytes, result);
    }

    /**
     * Test of writeInt64 method, of class Bytes.
     */
    @Test
    public void testWriteInt64To() {
        System.out.println("writeInt64To");
        byte[] bytes;
        byte[] result = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF
        };
        bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0xFF
        };
        Bytes.writeInt64(0x0000000000000000L, result, 0);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00
        };
        Bytes.writeInt64(0x0000000000000001L, result, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0x00
        };
        Bytes.writeInt64(0xFFFFFFFFFFFFFFFFL, result, 0);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0x7F
        };
        Bytes.writeInt64(0x7FFFFFFFFFFFFFFFL, result, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x80,
            (byte)0x7F
        };
        Bytes.writeInt64(0x8000000000000000L, result, 0);
        assertArrayEquals(bytes, result);
    }

    /**
     * Test of writeBits method, of class Bytes.
     */
    @Test
    public void testWriteBitsTo() {
        System.out.println("writeBitsTo");
        byte[] bytes;
        byte[] result = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF
        };
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0x07, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0xF8, (byte)0xFF
        };
        Bytes.writeBits(0x00000000, result, 1, 11, 32);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0x07, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0xF8, (byte)0xF7
        };
        Bytes.writeBits(0x00000000, result, 7, 3, 1);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0xFF, (byte)0x5F, (byte)0xB5, (byte)0x4A,
            (byte)0xB5, (byte)0x0A, (byte)0xF8, (byte)0xF7
        };
        Bytes.writeBits((int)(0x0AB54AB540L >> 5), result, 1, 5, 32);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xB7, (byte)0x4A,
            (byte)0xB5, (byte)0x0A, (byte)0xF8, (byte)0xF7
        };
        Bytes.writeBits((int)(0x07FFE0 >> 5), result, 0, 5, 14);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0x0B, (byte)0xF8, (byte)0xF7
        };
        Bytes.writeBits((int)(0x03FFFFFFF800L >> 11), result, 0, 11, 32);
        assertArrayEquals(bytes, result);
        bytes = new byte[] {
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0x0F, (byte)0xF8, (byte)0xF7
        };
        Bytes.writeBits((int)(0x07FFFFFFF800L >> 11), result, 0, 11, 32);
        assertArrayEquals(bytes, result);
    }
}
