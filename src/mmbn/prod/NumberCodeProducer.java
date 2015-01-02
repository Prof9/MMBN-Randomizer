package mmbn.prod;

import mmbn.ChipLibrary;
import mmbn.types.Item;
import mmbn.types.NumberCode;

public abstract class NumberCodeProducer extends ItemProducer<NumberCode> {
	protected final byte[] cipher;

	public NumberCodeProducer(final ChipLibrary library,
			final Item.Type[] itemTypes, final byte[] cipher) {
		super(library, itemTypes);

		if (cipher.length != 10) {
			throw new IllegalArgumentException("Cipher must have 10 bytes.");
		}
		this.cipher = cipher.clone();
	}

	@Override
	public String getDataName() {
		return "Number Trader code";
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
}
