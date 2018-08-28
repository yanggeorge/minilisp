package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.Node;
import com.threelambda.minilisp.node.SExprNode;
import com.threelambda.minilisp.node.SymbolExprNode;
import com.threelambda.minilisp.node.SymbolNode;

/**
 *
 * Created by ym on 7/18/2017.
 */
public class ExprType extends Type {
    public CellNode cellNode;

    public ExprType() {
        super("ExprType");
        this.cellNode = null;
    }

    public ExprType(CellNode cellNode) {
        super("ExprType");
        this.cellNode = cellNode;
    }


    /**
     * convert ExprType to minilisp expression , output exmaple: "(set a 1)"
     * @return
     */
    @Override
    public String toString() {
        return cellNode.toString("");
    }

    private String convert(SExprNode sExprNode) {
        String s = "";
        String sep = " ";
        Node child = sExprNode.node;
        if( child instanceof SymbolExprNode) {
            SymbolExprNode symExpr = (SymbolExprNode) child;
            SymbolNode symbolNode = (SymbolNode) symExpr.node;
            s += symbolNode.image;
            return s;
        }
        return s;
    }


}
