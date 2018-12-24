package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.SExprNode;

/**
 * Created by ym on 7/18/2017.
 */
public class ExprType extends Type {
    public SExprNode sExprNode;

    public ExprType() {
        super("ExprType");
        this.sExprNode = null;
    }

    public ExprType(SExprNode sExprNode) {
        super("ExprType");
        this.sExprNode = sExprNode;
    }


    /**
     * convert ExprType to minilisp expression , output exmaple: "(set a 1)"
     *
     * @return
     */
    @Override
    public String toString() {
        return sExprNode.toString("");
    }

}
