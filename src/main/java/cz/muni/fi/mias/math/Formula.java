/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.mias.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Container class for the information about a single MathML formula
 * 
 * @author Martin Liska
 */
public class Formula {

    private float weight;
    private Node node;
    private String miasString;
    private static final int LENGTH_TRIM = 20000;

    public Formula() {
    }

    public Formula(Node node, float weight) {
        this.weight = weight;
        this.node = node;
    }
    
    public Formula(Formula f) {
        this.node = f.getNode().cloneNode(true);
        this.weight = f.getWeight();
    }

    public String getMiasString() {
        return miasString;
    }

    public void setMiasString(String miasString) {
        this.miasString = miasString;
    }
    
    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "Formula{" + "weight=" + weight + "node=" + nodeToString(node, false, new HashMap<String, String>(), new HashMap<String, String>(), new ArrayList<String>()) + '}';
    }
    
    /**
     * Creates M-term styled string representation of a MathML formula.
     * 
     * @param node MathML with the formula
     * @param withoutTextContent if true, resulting string will not contain text content of MathML nodes
     * @param eldict dictionary for substituting standard MathML element names for custom ones
     * @param attrdict dictionary for substituting standard MathML attribute names and their values for custom ones
     * @param ignoreNode a list of MathML nodes which the output should not contain
     * @return M-terms styles string representing the input MathML formula
     */
    public static String nodeToString(Node node, boolean withoutTextContent, Map<String, String> eldict, Map<String, String> attrdict, List<String> ignoreNode) {
        StringBuilder s = new StringBuilder();
        if (node instanceof Element) {
            String name = node.getLocalName();
            if (!ignoreNode.contains(name)) {
                if (eldict.get(name) == null || withoutTextContent) {
                    s.append(name);
                } else {
                    s.append(eldict.get(name));
                }
                if (!withoutTextContent) {
                    NamedNodeMap attrs = node.getAttributes();
                    for (int i = 0; i < attrs.getLength(); i++) {
                        String attrName = attrs.item(i).getNodeName();
                        if (attrdict.containsKey(attrName)) {
                            String dictAttrName = attrdict.get(attrName);
                            String attrValue = attrs.item(i).getNodeValue();
                            String dictValue = attrdict.get(attrValue);
                            s.append("[").append(dictAttrName).append("=").append(dictValue == null ? attrValue : dictValue).append("]");
                        }

                    }
                }
                NodeList nl = node.getChildNodes();
                int length = nl.getLength();
                if ((length > 1)) {
                    s.append("(");
                    for (int j = 0; j < length; j++) {
                        s.append(nodeToString(nl.item(j), withoutTextContent, eldict, attrdict, ignoreNode));
                    }
                    s.append(")");
                } else {
                    if (name.equals("mrow") || name.equals("math") || name.equals("mfenced")) {
                        if (length == 1) {
                            s.append(nodeToString(node.getFirstChild(), withoutTextContent, eldict, attrdict, ignoreNode));
                        } else {
                            s = new StringBuilder();
                        }
                    } else if (!withoutTextContent) {
                        s.append("(").append(node.getTextContent()).append(")");
                    }
                }
            }
        }
        if (s.length() > LENGTH_TRIM) {
            return s.substring(0, LENGTH_TRIM);
        }
        return s.toString();
    }
}
