import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CBORDecoderArrayTest extends CBORDecoderTest {

    @Test()
    public void ArrayTest() throws CBORException {
        String input = "83010203";
        byte[] bytes = fromHex(input);
        CBORDecoder decoder = new CBORDecoder();
        CBORObject result = decoder.decode(bytes);
        assertTrue(result.val instanceof ArrayList);
        ArrayList items = (ArrayList) result.val;
        assertEquals(1L, items.get(0));
        assertEquals(2L, items.get(1));
        assertEquals(3L, items.get(2));
    }
}
