
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CBORDecoder {

    private final static int _CBOR_UINT8_FOLLOWS = 0x18;
    private final static int _CBOR_UINT16_FOLLOWS = (0x19);
    private final static int _CBOR_UINT32_FOLLOWS = (0x1a);
    private final static int _CBOR_UINT64_FOLLOWS = (0x1b);
    private final static int _CBOR_VAR_FOLLOWS = (0x1f);

    private final static int _CBOR_BREAK = (0x1f);
    private final static int _CBOR_RAW_TAG = (0x18);

    private final static int _CBOR_TYPE_MASK = (0xE0);
    private final static int _CBOR_INFO_BITS = (0x1F);

    private final static int _CBOR_UNSIGNED_INT = (0x00);
    private final static int _CBOR_BYTE_STRING = (0x40);
    private final static int _CBOR_ARRAY = (0x80);
    private final static int _CBOR_MAP = (0xA0);
    private final static int _CBOR_TAG = (0xC0);
    private final static int _CBOR_PRIMITIVE = (0xE0);

    private long convertByte(byte value) {
        long retval = value;
        if (retval < 0) {
            retval = retval + 256;
        }
        return retval;
    }

    private CBORObject read_length(byte[] cbor, long aux) throws CBORException {
        if (aux == _CBOR_UINT8_FOLLOWS) {
            byte[] retVal = new byte[cbor.length - 1];
            System.arraycopy(cbor, 1, retVal, 0, cbor.length - 1);
            return new CBORObject(convertByte(cbor[0]), retVal);
        } else if (aux == _CBOR_UINT16_FOLLOWS) {
            long res = convertByte(cbor[1]);
            res += convertByte(cbor[0]) << 8;
            byte[] retVal = new byte[cbor.length - 2];
            System.arraycopy(cbor, 2, retVal, 0, cbor.length - 2);
            return new CBORObject(res, retVal);
        } else if (aux == _CBOR_UINT32_FOLLOWS) {
            long res = convertByte(cbor[3]);
            res += convertByte(cbor[2]) << 8;
            res += convertByte(cbor[1]) << 16;
            res += convertByte(cbor[0]) << 24;
            byte[] retVal = new byte[cbor.length - 4];
            System.arraycopy(cbor, 4, retVal, 0, cbor.length - 4);
            return new CBORObject(res, retVal);
        } else if (aux == _CBOR_UINT64_FOLLOWS) {
            long res = convertByte(cbor[7]);
            res += convertByte(cbor[6]) << 8;
            res += convertByte(cbor[5]) << 16;
            res += convertByte(cbor[4]) << 24;
            res += convertByte(cbor[3]) << 32;
            res += convertByte(cbor[2]) << 40;
            res += convertByte(cbor[1]) << 48;
            res += convertByte(cbor[0]) << 56;
            byte[] retVal = new byte[cbor.length - 8];
            System.arraycopy(cbor, 8, retVal, 0, cbor.length - 8);
            return new CBORObject(res, retVal);
        }
        throw new CBORException("Length " + aux + " not suppported");
    }

    public CBORObject decode(byte[] cbor) throws CBORException {
        long fb = (long) cbor[0];
        long fb_type = fb & _CBOR_TYPE_MASK;
        long fb_aux = fb & _CBOR_INFO_BITS;
        if (fb_type == _CBOR_UNSIGNED_INT) {
            if (fb_aux < 0x18) {
                byte[] retVal = new byte[cbor.length - 1];
                System.arraycopy(cbor, 1, retVal, 0, cbor.length - 1);
                return new CBORObject(fb_aux, retVal);
            } else {
                byte[] input = new byte[cbor.length - 1];
                System.arraycopy(cbor, 1, input, 0, cbor.length - 1);
                CBORObject retVal = read_length(input, fb_aux);
                return new CBORObject(retVal.val, retVal.data);
            }
        } else if (fb_type == _CBOR_BYTE_STRING) {
            byte[] input = new byte[cbor.length - 1];
            System.arraycopy(cbor, 1, input, 0, cbor.length - 1);
            CBORObject retVal = read_length(input, fb_aux);
            int point;
            if (retVal.val instanceof Long) {
                point = ((Long) retVal.val).intValue();
                if (point < 0) {
                    point = point + 256;
                }
            } else {
                point = (Integer) retVal.val;
            }
            byte[] ln = new byte[point];
            byte[] data = new byte[retVal.data.length - point];
            System.arraycopy(retVal.data, 0, ln, 0, point);
            System.arraycopy(retVal.data, point, data, 0, retVal.data.length - point);
            return new CBORObject(ln, data);
        } else if (fb_type == _CBOR_ARRAY) {
            if (fb_aux == _CBOR_VAR_FOLLOWS) {
                List res = new ArrayList();
                byte[] data = new byte[cbor.length - 1];
                System.arraycopy(cbor, 1, data, 0, cbor.length - 1);
                while (true) {
                    CBORObject retVal = decode(data);
                    data = retVal.data;
                    try {
                        if (retVal.val instanceof Byte) {
                            int item = ((Byte) retVal.val).intValue();
                            if (item < 0) {
                                item = item + 256;
                            }
                            if (item == _CBOR_PRIMITIVE + _CBOR_BREAK) {
                                break;
                            }
                        }
                    } catch (ClassCastException e) {
                        //nothing
                    }
                    res.add(retVal.val);
                }
                return new CBORObject(res, data);
            } else {
                long ln = 0;
                byte[] data = null;
                if (fb_aux < _CBOR_UINT8_FOLLOWS) {
                    ln = fb_aux;
                    data = new byte[cbor.length - 1];
                    System.arraycopy(cbor, 1, data, 0, cbor.length - 1);
                } else {
                    byte[] inputData = new byte[cbor.length - 1];
                    System.arraycopy(cbor, 1, inputData, 0, cbor.length - 1);
                    CBORObject outputData = read_length(inputData, fb_aux);
                    ln = (Long) outputData.val;
                    data = outputData.data;
                }
                List res = new ArrayList();
                for (int i = 0; i < ln; i++) {
                    CBORObject loopObj = decode(data);
                    res.add(loopObj.val);
                    data = loopObj.data;
                }
                return new CBORObject(res, data);
            }
        } else if (fb_type == _CBOR_MAP) {
            byte[] data = new byte[cbor.length - 1];
            System.arraycopy(cbor, 1, data, 0, cbor.length - 1);
            return new CBORObject(new HashMap(), data);
        } else if (fb_type == _CBOR_TAG) {
            if (cbor[1] == _CBOR_RAW_TAG) {
                byte[] data = new byte[cbor.length - 2];
                System.arraycopy(cbor, 2, data, 0, cbor.length - 2);
                return decode(data);
            }
            throw new CBORException("Not Implemented");
        } else if (fb_type == _CBOR_PRIMITIVE) {
            byte[] data = new byte[cbor.length - 1];
            System.arraycopy(cbor, 1, data, 0, cbor.length - 1);
            return new CBORObject(cbor[0], data);
        }
        throw new CBORException("Not Implemented");
    }
}
