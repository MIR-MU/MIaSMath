package cz.muni.fi.mias.math;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.muni.fi.mias.MIaSError;

/**
 * Helper class containing static MathML configuration
 *
 * @author Martin Liska
 */
public class MathMLConf {

    private static final Logger log = Logger.getLogger(MathTokenizer.class.getName());

    private static final List<String> ignoreNode = Arrays.asList("semantics", "annotation-xml");
    private static final List<String> ignoreAll = Arrays.asList("annotation");

    private static final List<String> presentationElements = Arrays.asList("mi", "mn", "mo", "mtext", "mspace", "ms", "mglyph", "mrow", "mfrac", "msqrt", "mroot", "mstyle", "merror",
            "mpadded", "mphantom", "mfenced", "menclose", "msub", "msup", "msubsup", "munder", "mover", "munderover", "mmultiscripts", "mtable", "mlabeledtr", "mtr", "mtd");

    private static List<String> contentElements = Arrays.asList("ci", "cn", "csymbol", "apply", "cs", "bind", "bvar", "share", "cerror", "cbytes", "set", "domainofapplication",
            "interval", "condition", "lowlimit", "uplimit", "degree", "momentabout", "logbase", "union", "piecewise", "piece", "otherwise", "reln", "fn", "declare", "ident",
            "domain", "codomain", "image", "ln", "log", "moment", "lambda", "compose", "quotient", "divide", "minues", "power", "rem", "root", "factorial", "abs", "conjugate",
            "arg", "real", "imaginary", "floor", "ceiling", "exp", "max", "min", "plus", "times", "gcd", "lcm", "and", "or", "xor", "not", "implies", "equivalent", "forall",
            "exists", "eq", "gt", "lt", "geq", "leq", "neq", "approx", "factorof", "tendsto", "int", "diff", "partialdiff", "divergence", "grad", "curl", "laplacian", "set",
            "\\list", "union", "intersect", "cartesianproduct", "in", "notin", "notsubset", "notprsubset", "setdiff", "subset", "prsubset", "card", "sum", "product",
            "limit", "sin", "cos", "tan", "sec", "csc", "cot", "sinh", "cosh", "tanh", "sech", "csch", "coth", "arcsin", "arccos", "arctan", "arccosh", "arccot", "arccoth",
            "arccsc", "arccsch", "arcsec", "arccsch", "arcsec", "arcsinh", "arctanh", "mean", "sdev", "variance", "median", "mode", "vector", "matrix", "matrixrow",
            "determinant", "transpose", "selector", "vectorproduct", "scalarproduct", "outerproduct", "integers", "reals", "rationals", "naturalnumbers", "complexes",
            "primes", "emptyset", "exponentiale", "imaginaryi", "notanumber", "true", "false", "pi", "eulergamma", "infinity");

    private static final List<String> operatorElements = (Arrays.asList("mo", "times", "plus", "minus", "power", "log", "max", "min", "divide", "ln", "log", "lambda", "and", "or", "xor", "implies", "equivalen", "forall", "exists", "int"));

    public static final List<String> additiveOperators = (Arrays.asList("-", "\u2212", "\u2213", "\u2214", "\u2238", "\u2295", "\u2296", "\u229d", "\u229e", "\u229f", "plus", "minus"));

    public static final String timesOperators = "\u2062 \u00d7 \u00b7";

    public static final String MATHML_NAMESPACE_URI = "http://www.w3.org/1998/Math/MathML";

    /**
     * Threshold for adding unified version of a formula to the index. Formulae
     * with weight coefficient under this threshold will be discarded from
     * further processing.
     */
    public static final float unifiedNodeWeightCoefThreshold = 0.1f;

    public static List<String> getPresentationElements() {
        return presentationElements;
    }

    public static List<String> getContentElements() {
        return contentElements;
    }

    /**
     * @return List of MathML nodes which should be ignored during the
     * processing
     */
    public static List<String> getIgnoreNode() {
        return ignoreNode;
    }

    /**
     * @return List of MathML nodes which should be ignored together with their
     * children during the processing
     */
    public static List<String> getIgnoreAll() {
        return ignoreAll;
    }

    /**
     * @return default dictionary for substituting standard MathML element names
     * for custom ones
     */
    public static Map<String, String> getElementDictionary() {
        Map<String, String> result = new HashMap<String, String>();
        BufferedReader br = null;
        String resource = "element-dictionary";
        try {
            br = new BufferedReader(new InputStreamReader(MathMLConf.class.getResourceAsStream(resource)));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] entry = line.split("=");
                result.put(entry[0], entry[1]);
            }
        } catch (Exception e) {
            handleError("Cannot load element dictionary file", e);
        } finally {
            closeReader(br, resource);
        }
        return result;
    }

    /**
     * @return default dictionary for substituting standard MathML attribute
     * names and their values for custom ones
     */
    public static Map<String, String> getAttrDictionary() {
        Map<String, String> result = new HashMap<String, String>();
        BufferedReader br = null;
        String resource = "attr-dictionary";
        try {
            br = new BufferedReader(new InputStreamReader(MathMLConf.class.getResourceAsStream(resource)));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] entry = line.split("=");
                result.put(entry[0], entry[1]);
            }
        } catch (Exception e) {
            handleError("Cannot load attribute dictionary file", e);
        } finally {
            closeReader(br, resource);
        }
        return result;
    }

    /**
     * @return Map of commutative operators and their priorities. The key is a
     * commutative operator and the value is a list of operators that have a
     * higher priority than the key
     */
    public static Map<String, List<String>> getOperators() {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        BufferedReader br = null;
        String resource = "operators";
        try {
            br = new BufferedReader(new InputStreamReader(MathMLConf.class.getResourceAsStream(resource)));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] op = line.substring(0, line.indexOf(";")).split(",");
                String[] ops = (line.substring(line.indexOf(";") + 1)).split(",");
                List<String> opsList = Arrays.asList(ops);
                for (int i = 0; i < op.length; i++) {
                    result.put(op[i], opsList);
                }
            }
        } catch (Exception e) {
            handleError("Cannot load operators file", e);
        } finally {
            closeReader(br, resource);
        }
        return result;
    }

    public static boolean isIndexableContentElement(String s) {
        return getContentElements().contains(s) && !isOperatorElement(s);
    }

    public static boolean isIndexablePresentationElement(String s) {
        return getPresentationElements().contains(s) && !isOperatorElement(s);
    }

    public static boolean isIndexableElement(String s) {
        return isIndexableContentElement(s) || isIndexablePresentationElement(s);
    }

    public static boolean isOperatorElement(String s) {
        return operatorElements.contains(s);
    }

    public static boolean ignoreNode(String s) {
        return getIgnoreNode().contains(s);
    }

    public static boolean ignoreNodeAndChildren(String s) {
        return getIgnoreAll().contains(s);
    }

    private static void closeReader(Reader reader, String fileName) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            handleError("Cannot close " + fileName + " file", e);
        }
    }

    private static void handleError(String msg, Exception e) throws MIaSError {
        log.log(Level.SEVERE, msg, e);
        throw new MIaSError(msg, e);
    }

}
