import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CBORDecoderIntegerTest extends CBORDecoderTest {

    @Test()
    public void IntegerTest() throws CBORException {
        String input = "1b000000e8d4a51000";
        byte[] bytes = fromHex(input);
        CBORDecoder decoder = new CBORDecoder();
        CBORObject result = decoder.decode(bytes);
        assertTrue(result.val instanceof Long);
        assertEquals(1000000000000L, result.val);
    }

    @Test()
    public void IntegerTest2() throws CBORException {
        String input = "1a000f4240";
        byte[] bytes = fromHex(input);
        CBORDecoder decoder = new CBORDecoder();
        CBORObject result = decoder.decode(bytes);
        assertTrue(result.val instanceof Long);
        assertEquals(1000000L, result.val);
    }

    @Test()
    public void IntegerTest3() throws CBORException {
        String input = "1903e8";
        byte[] bytes = fromHex(input);
        CBORDecoder decoder = new CBORDecoder();
        CBORObject result = decoder.decode(bytes);
        assertTrue(result.val instanceof Long);
        assertEquals(1000L, result.val);
    }

    @Test()
    public void IntegerTest4() throws CBORException {
        String input = "1818";
        byte[] bytes = fromHex(input);
        CBORDecoder decoder = new CBORDecoder();
        CBORObject result = decoder.decode(bytes);
        assertTrue(result.val instanceof Long);
        assertEquals(24L, result.val);
    }

    @Test()
    public void IntegerTest5() throws CBORException {
        String input = "01";
        byte[] bytes = fromHex(input);
        CBORDecoder decoder = new CBORDecoder();
        CBORObject result = decoder.decode(bytes);
        assertTrue(result.val instanceof Long);
        assertEquals(1L, result.val);
    }

    @Test()
    public void IntegerTest6() throws CBORException {
        String input = "00";
        byte[] bytes = fromHex(input);
        CBORDecoder decoder = new CBORDecoder();
        CBORObject result = decoder.decode(bytes);
        assertTrue(result.val instanceof Long);
        assertEquals(0L, result.val);
    }
}
