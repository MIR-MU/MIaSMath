/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.mias.math;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Formula valuator which values formula's complexity on the number of its nodes
 * 
 * @author Martin Liska
 */
public class CountNodesFormulaValuator implements FormulaValuator {

    @Override
    public float count(Node n, MathTokenizer.MathMLType mmlType) {
        if (mmlType == MathTokenizer.MathMLType.BOTH) {
            mmlType = MathTokenizer.MathMLType.PRESENTATION;
        }
        float result = 0;
        if (n instanceof Element) {
            String name = n.getLocalName();
            if (!MathMLConf.ignoreNodeAndChildren(name)) {
                boolean count = false;
                if (((mmlType == MathTokenizer.MathMLType.BOTH && MathMLConf.isIndexableElement(name))
                        || (mmlType == MathTokenizer.MathMLType.PRESENTATION && MathMLConf.isPresentationElement(name))
                        || (mmlType == MathTokenizer.MathMLType.CONTENT && MathMLConf.isContentElement(name)))) {
                    count = true;
                }
                NodeList nl = n.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    result += count(nl.item(i), mmlType);
                }
                if (count) {
                    result += 1.0;
                }
            }
        }
        return result;
    }

}