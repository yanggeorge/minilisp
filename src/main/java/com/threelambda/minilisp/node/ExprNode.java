package com.threelambda.minilisp.node;

import com.threelambda.minilisp.core.Visitor;

/**
 * Created by YM on 5/26/2017.
 */
public class ExprNode extends Node {

    /**
     * 只有以下类型：<br/>
     *
     * @see SExprNode
     * @see NonSExprNode
     * @see SymbolExprNode
     */
    public Node node;

    public ExprNode() {
        super("ExprNode");
    }

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visitExprNode(this);
    }


    @Override
    public String toString(String indent) {
        return node.toString(indent);
    }
}
