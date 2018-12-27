package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;

/**
 * Created by ym on 7/20/2017.
 */
public class QuoteFunc extends FuncType {


    public QuoteFunc() {
        super("QuoteFunc");
    }

    public Type eval(Visitor visitor, CellNode cellNode) {
        ExprType result = new ExprType();

        try {
            result.sExprNode = (SExprNode) cellNode.car;
        } catch (Exception e) {
            throw new RuntimeException("MalFormed QuoteFunc.", e);
        }

        return result;
    }
}
