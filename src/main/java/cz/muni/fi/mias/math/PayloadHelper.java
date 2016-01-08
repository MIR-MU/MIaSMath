package cz.muni.fi.mias.math;

/**
 * Helper class for converting different number types for payloads
 *
 * @author Martin Liska
 */
public class PayloadHelper extends org.apache.lucene.analysis.payloads.PayloadHelper {

    /**
     * Converts short number to byte array
     */
    public static byte[] encodeShort(short payload) {
        byte[] data = new byte[2];
        data[0] = (byte) (payload >> 8);
        data[1] = (byte) payload;
        return data;
    }

    /**
     * Converts byte array to short number
     */
    public static short decodeShort(byte[] bytes) {
        return (short) (((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xFF));
    }

    /**
     * Converts float number to byte array
     */
    public static byte[] encodeFloatToShort(float f) {
        return encodeShort((short) (Math.round(Math.log10(f) * 3276) + 32768));
    }

    /**
     * Converts byte array to float number
     */
    public static float decodeFloatFromShortBytes(byte[] bytes) {
        float result = (float) decodeShort(bytes);
        return result / 10000;
    }

}
