package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;

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
            result.cellNode = cellNode;
        } catch (Exception e) {
            throw new RuntimeException("MalFormed QuoteFunc.");
        }

        return result;
    }
}
