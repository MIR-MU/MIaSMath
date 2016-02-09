package cz.muni.fi.mias.math.maple;

import com.maplesoft.externalcall.MapleException;
import com.maplesoft.openmaple.Algebraic;
import com.maplesoft.openmaple.Engine;
import com.maplesoft.openmaple.EngineCallBacksDefault;
import cz.muni.fi.mir.mathmlcanonicalization.MathMLCanonicalizer;
import cz.muni.fi.mir.mathmlcanonicalization.modules.ModuleException;
import org.jdom2.JDOMException;
import org.jdom2.output.DOMOutputter;
import org.w3c.dom.Node;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by 255768<at>main.muni.cz on 9.2.2016.
 */
public class MapleUnifier {

    private static Engine engine;
    private static boolean mapleCannotInit = false;
    private static Transformer transformer = null;

    // utilities
    private static final MathMLCanonicalizer canonicalizer = MathMLCanonicalizer.getDefaultCanonicalizer();
    private static final DOMOutputter outputter = new DOMOutputter();

    public static String mapleUnifyMathML(String mathml) {
        Engine engine = getEngine();
        if (engine != null) {
            try {
                Algebraic evaluate = engine.evaluate("MathML['Import'](\"" + mathml + "\");");
                engine.evaluate("MathML['Export'](\"" + evaluate.toString() + "\");");
            } catch (MapleException e) {
                e.printStackTrace();
            }
        }
        return mathml;
    }

    public static Node mapleUnifyMathML(Node mathNode) {
        if (getEngine() != null && getTransformer() != null) {
            StringWriter buffer = new StringWriter();
            try {
                getTransformer().transform(new DOMSource(mathNode), new StreamResult(buffer));
                String unifiedMathMLString = mapleUnifyMathML(buffer.toString());
                org.jdom2.Document jdom2Doc = canonicalizer.canonicalize(new ByteArrayInputStream(unifiedMathMLString.getBytes()));
                return outputter.output(jdom2Doc);
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            } catch (ModuleException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mathNode;
    }

    private static Transformer getTransformer() {
        if (transformer == null) {
            try {
                TransformerFactory transFactory = TransformerFactory.newInstance();
                transformer = transFactory.newTransformer();

                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            }
        }
        return transformer;
    }

    private static Engine getEngine() {
        if (engine == null && !mapleCannotInit) {
            try {
                engine = new Engine(new String[]{"java"}, new EngineCallBacksDefault(), null, null);
            } catch (MapleException e) {
                e.printStackTrace();
                mapleCannotInit = true;
            }
        }
        return engine;
    }
}
