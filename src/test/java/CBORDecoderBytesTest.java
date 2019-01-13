import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CBORDecoderBytesTest extends CBORDecoderTest {

    @Test()
    public void BytesTest() throws CBORException {
        String input = "5818010203040102030401020304010203040102030401020304";
        byte[] bytes = fromHex(input);
        CBORDecoder decoder = new CBORDecoder();
        CBORObject result = decoder.decode(bytes);
        assertTrue(result.val instanceof byte[]);
        byte[] checkBytes = {0x01, 0x02, 0x03, 0x04, 0x01, 0x02, 0x03, 0x04, 0x01, 0x02, 0x03, 0x04, 0x01, 0x02, 0x03, 0x04, 0x01, 0x02, 0x03, 0x04, 0x01, 0x02, 0x03, 0x04};
        byte[] resultBytes = (byte[]) result.val;
        for (int i = 0; i < resultBytes.length; i++) {
            assertEquals(checkBytes[i], resultBytes[i]);
        }
    }
}
