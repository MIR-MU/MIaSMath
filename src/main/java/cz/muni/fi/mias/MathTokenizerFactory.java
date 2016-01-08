package cz.muni.fi.mias;

import cz.muni.fi.mias.math.MathTokenizer;
import java.io.Reader;
import java.util.Map;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

/**
 * Factory used for calling MathTokenizer from SOLR environment. The following
 * attributes must be specified in the schema.xml file for tokenizer
 * MathTokenizer:
 * <ul>
 * <li>subformulae - true for analyzer type index, false for analyzer type
 * query</li>
 * </ul>
 *
 * Complete example:
 * <pre>
 * &lt;fieldType name=&quot;math&quot; class=&quot;solr.TextField&quot;&gt;
 *   &lt;analyzer type=&quot;index&quot;&gt;
 *     &lt;tokenizer class=&quot;cz.muni.fi.mias.MathTokenizerFactory&quot; subformulae=&quot;true&quot;  /&gt;
 *   &lt;/analyzer&gt;
 *   &lt;analyzer type=&quot;query&quot;&gt;
 *     &lt;tokenizer class=&quot;cz.muni.fi.mias.MathTokenizerFactory&quot; subformulae=&quot;false&quot; /&gt;
 *   &lt;/analyzer&gt;
 * &lt;/fieldType&gt;
 * </pre>
 *
 * @author Martin Liska
 */
public class MathTokenizerFactory extends TokenizerFactory {

    private boolean subformulae;

    public MathTokenizerFactory(Map<String, String> args) {
        super(args);
        String subforms = args.get("subformulae");
        subformulae = Boolean.parseBoolean(subforms);
    }

    @Override
    public Tokenizer create(AttributeFactory af, Reader reader) {
        return new MathTokenizer(reader, subformulae, MathTokenizer.MathMLType.BOTH);
    }

}
