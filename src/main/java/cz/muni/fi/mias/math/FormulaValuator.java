/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.mias.math;

import org.w3c.dom.Node;

/**
 * Interface for computing formula's complexity.
 * 
 * @author Martin Liska
 */
public interface FormulaValuator {

    /**
     * 
     * @param node MathML node denoting a formula
     * @param mmlType Type of the MathML which needs to be considered in the valuation
     * @return formula's complexity
     */
    float count(Node node, MathTokenizer.MathMLType mmlType);

}
