public class CBORObject {
    Object val;
    byte[] data;

    CBORObject(Object val, byte[] data) {
        this.val = val;
        this.data = data;
    }
}
