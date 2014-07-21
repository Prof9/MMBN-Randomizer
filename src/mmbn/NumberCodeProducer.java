package mmbn;

import rand.DataProducer;

public abstract class NumberCodeProducer implements DataProducer<NumberCode> {
    protected final NumberCode.Type[] codeTypes;
    protected final byte[] cipher;
    
    public NumberCodeProducer(final NumberCode.Type[] codeTypes, final byte[] cipher) {
        if (cipher.length != 10) {
            throw new IllegalArgumentException("Cipher must have 10 bytes.");
        }
        this.codeTypes = codeTypes;
        this.cipher = cipher.clone();
    }
    
    protected int decode(byte[] bytes) {
        if (bytes.length != 8) {
            throw new IllegalArgumentException("Array must be 8 bytes long.");
        }
        int result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result *= 10;
            result += decodeDigit(bytes[i]);
        }
        return result;
    }
    protected byte[] encode(int code) {
        if (code < 0 || code > 99999999) {
            throw new IllegalArgumentException("Number codes must be at least "
                    + "00000000 and at most 99999999.");
        }
        byte[] result = new byte[8];
        for (int i = 0; i < result.length; i++) {
            result[result.length - 1 - i] = encodeDigit(code % 10);
            code /= 10;
        }
        return result;
    }
    
    protected byte encodeDigit(int i) {
        if (i < 0 || i > 9) {
            throw new IllegalArgumentException("Number must be a single digit.");
        }
        return this.cipher[i];
    }
    protected int decodeDigit(byte b) {
        for (int i = 0; i < this.cipher.length; i++) {
            if (this.cipher[i] == b) {
                return i;
            }
        }
        return -1;
    }
    
    protected NumberCode.Type codeTypeFromIndex(int index) {
        return this.codeTypes[index];
    }
    protected int indexFromCodeType(NumberCode.Type codeType) {
        for (int i = 0; i < this.codeTypes.length; i++) {
            if (this.codeTypes[i] == codeType) {
                return i;
            }
        }
        return -1;
    }
}
