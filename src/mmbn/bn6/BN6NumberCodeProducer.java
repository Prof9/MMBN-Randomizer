package mmbn.bn6;

import mmbn.ChipLibrary;
import mmbn.Item;
import mmbn.NumberCode;
import mmbn.NumberCodeProducer;
import rand.ByteConverter;
import rand.ByteStream;

public class BN6NumberCodeProducer extends NumberCodeProducer {
    public BN6NumberCodeProducer(final ChipLibrary chipLibrary,
            final byte[] cipher) {
        super(chipLibrary, new Item.Type[] {
            Item.Type.BATTLECHIP,
            Item.Type.ITEM,
            Item.Type.SUBCHIP,
            Item.Type.NAVICUST_PROGRAM
        }, cipher);
    }
    
    public BN6NumberCodeProducer(final ChipLibrary chipLibrary) {
        this(chipLibrary, new byte[] {
            (byte)0x3E, (byte)0x45, (byte)0xCC, (byte)0x86, (byte)0x90,
            (byte)0x18, (byte)0x4F, (byte)0x09, (byte)0x61, (byte)0xE9
        });
    }

    @Override
    public NumberCode readFromStream(ByteStream stream) {
        byte[] bytes = stream.readBytes(12);
        NumberCode code = new NumberCode(bytes);
        
        Item.Type type = getItemType(ByteConverter.readUInt8(bytes, 0));
        int subValue = ByteConverter.readUInt8(bytes, 1);
        int value = ByteConverter.readUInt16(bytes, 2);
        
        setItem(code, type, value, subValue);
        byte[] numberCode = new byte[8];
        System.arraycopy(bytes, 4, numberCode, 0, numberCode.length);
        code.setNumberCode(decode(numberCode));
        
        return code;
    }

    @Override
    public void writeToStream(ByteStream stream, NumberCode code) {
        byte[] bytes = code.base();
        
        ByteConverter.writeUInt8((short)getItemTypeIndex(code.type()), bytes, 0);
        ByteConverter.writeUInt8((short)code.subValue(), bytes, 1);
        ByteConverter.writeUInt16(code.value(), bytes, 2);
        System.arraycopy(encode(code.getNumberCode()), 0, bytes, 4, 8);
        
        stream.writeBytes(bytes);
    }
}
