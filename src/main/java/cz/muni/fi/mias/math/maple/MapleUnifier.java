package cz.muni.fi.mias.math.maple;

import com.maplesoft.externalcall.MapleException;
import com.maplesoft.openmaple.Algebraic;
import com.maplesoft.openmaple.Engine;
import com.maplesoft.openmaple.EngineCallBacksDefault;
import cz.muni.fi.mias.MIaSMathUtils;
import cz.muni.fi.mir.mathmlcanonicalization.MathMLCanonicalizer;
import org.jdom2.output.DOMOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by 255768<at>main.muni.cz on 9.2.2016.
 */
public class MapleUnifier {

    private static final Logger LOG = Logger.getLogger(MapleUnifier.class.getName());

    private static Engine engine;
    private static boolean mapleCannotInit = false;
    private static Transformer transformer = null;
    private static final int MATHML_MAX_LENGTH = 100000;

    // utilities
    private static final MathMLCanonicalizer canonicalizer = MathMLCanonicalizer.getDefaultCanonicalizer();
    private static final DOMOutputter outputter = new DOMOutputter();

    public static String mapleUnifyMathML(String mathml) {
        if (mathml.length() > MATHML_MAX_LENGTH) {
            return null;
        }
        Engine engine = getEngine();
        if (engine != null) {
            try {
                mathml = mathml.replaceAll("\"", "'");
                Algebraic importExpression = engine.evaluate("MathML['Import'](\"" + mathml + "\"):");
                Algebraic exportExpression = engine.evaluate("MathML['Export']('" + importExpression.toString() + "'):");
                String export = exportExpression.toString();
                return export.substring(1, export.length() - 1).replaceAll("'", "\"").replaceAll("ApplyFunction", "#8289");
            } catch (MapleException e) {
                LOG.log(Level.SEVERE, "Cannot unify expression " + e.getMessage() + ", " + mathml);
            }
        }
        return null;
    }

    public static Node mapleUnifyMathML(Node mathNode) {
        if (getEngine() != null && getTransformer() != null) {
            StringWriter buffer = new StringWriter();
            try {
                getTransformer().transform(new DOMSource(mathNode), new StreamResult(buffer));
                String unifiedMathMLString = mapleUnifyMathML(buffer.toString());
                if (unifiedMathMLString == null) {
                    return null;
                }
                DocumentBuilder documentBuilder = MIaSMathUtils.prepareDocumentBuilder();
                unifiedMathMLString = "<?xml version='1.0' encoding='UTF-8'?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1 plus MathML 2.0//EN\" \"http://www.w3.org/TR/MathML2/dtd/xhtml-math11-f.dtd\"><html>" + unifiedMathMLString + "</html>";
                Document parse = documentBuilder.parse(new ByteArrayInputStream(unifiedMathMLString.getBytes()));
                return parse.getElementsByTagName("math").item(0);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "", e);
            }
        }
        return null;
    }

    private static Transformer getTransformer() {
        if (transformer == null) {
            try {
                TransformerFactory transFactory = TransformerFactory.newInstance();
                transformer = transFactory.newTransformer();

                transformer.setOutputProperty(OutputKeys.INDENT, "no");
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            } catch (TransformerConfigurationException e) {
                LOG.log(Level.SEVERE, "", e);
            }
        }
        return transformer;
    }

    private static Engine getEngine() {
        if (engine == null && !mapleCannotInit) {
            try {
                engine = new Engine(new String[]{"java"}, new EngineCallBacksDefault(), null, null);
            } catch (MapleException e) {
                LOG.log(Level.SEVERE, "Cannot initialize Maple engine", e);
                mapleCannotInit = true;
            }
        }
        return engine;
    }

}
