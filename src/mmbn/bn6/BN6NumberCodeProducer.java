package mmbn.bn6;

import mmbn.NumberCode;
import mmbn.NumberCodeProducer;
import rand.ByteStream;

public class BN6NumberCodeProducer extends NumberCodeProducer {
    public BN6NumberCodeProducer(byte[] cipher) {
        super(new NumberCode.Type[] {
            NumberCode.Type.BATTLECHIP,
            NumberCode.Type.ITEM,
            NumberCode.Type.SUBCHIP,
            NumberCode.Type.NAVICUST_PROGRAM
        }, cipher);
    }
    
    public BN6NumberCodeProducer() {
        this(new byte[] {
            (byte)0x3E, (byte)0x45, (byte)0xCC, (byte)0x86, (byte)0x90,
            (byte)0x18, (byte)0x4F, (byte)0x09, (byte)0x61, (byte)0xE9
        });
    }

    @Override
    public NumberCode readFromStream(ByteStream stream) {
        int codeTypeIndex = stream.readUInt8();
        NumberCode code = new NumberCode(codeTypeFromIndex(codeTypeIndex));
        
        code.setSubValue(stream.readUInt8());
        code.setValue(stream.readUInt16());
        
        code.setCode(decode(stream.readBytes(8)));
        
        return code;
    }

    @Override
    public void writeToStream(ByteStream stream, NumberCode code) {
        stream.writeUInt8((short)indexFromCodeType(code.getType()));
        stream.writeUInt8((short)code.getSubValue());
        stream.writeUInt16(code.getValue());
        stream.writeBytes(encode(code.getCode()));
    }
}
