package cz.muni.fi.mias;

import cz.muni.fi.mias.math.PayloadHelper;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.util.BytesRef;

/**
 * PayloadSimilarity class extending default similarity. Alters the way how
 * lucene scores hit documents in order to consider payloads located at formuale
 * in index.
 */
public class PayloadSimilarity extends DefaultSimilarity {

    @Override
    public float scorePayload(int docId, int start, int end, BytesRef byteRef) {
        float score = 1.0F;
        if (byteRef.bytes != BytesRef.EMPTY_BYTES) {
            score = PayloadHelper.decodeFloatFromShortBytes(byteRef.bytes);
        }
        return score;
    }
}
