# CBOR Java library

The Concise Binary Object Representation (CBOR) is a data format whose design goals include the possibility of extremely small code size, fairly small message size, and extensibility without the need for version negotiation

Read more in the [documentation on ReadTheDocs](http://cbor.io/). 

# Examples
## decode integer 
```java
    String input = "1b000000e8d4a51000";
    byte[] bytes = fromHex(input);
    CBORDecoder decoder = new CBORDecoder();
    CBORObject result = decoder.decode(bytes);
    assertTrue(result.val instanceof Long);
    assertEquals(1000000000000L, result.val);
```
## decode bytes 
```java
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
```
## decode array 
```java
    String input = "83010203";
    byte[] bytes = fromHex(input);
    CBORDecoder decoder = new CBORDecoder();
    CBORObject result = decoder.decode(bytes);
    assertTrue(result.val instanceof ArrayList);
    ArrayList items = (ArrayList) result.val;
    assertEquals(1L, items.get(0));
    assertEquals(2L, items.get(1));
    assertEquals(3L, items.get(2));
```

## Getting help

Please contact sfa.alptekin@gmail.com
