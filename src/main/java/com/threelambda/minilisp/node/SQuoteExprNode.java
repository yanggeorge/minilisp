package com.threelambda.minilisp.node;

import com.threelambda.minilisp.core.Visitor;

/**
 * Created by YM on 5/26/2017.
 */
public class SQuoteExprNode extends Node {

    public Node node ;

    public SQuoteExprNode() {
        super("SQuoteExprNode");
    }

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visitSQuoteExprNode(this);
    }

    @Override
    public String toString(String indent) {
        return "'" + node.toString(indent);
    }
}
