package com.threelambda.minilisp.node;

import com.threelambda.minilisp.core.Visitor;

/**
 * Created by YM on 5/26/2017.
 */
public class NonSExprNode extends Node {

    public Node node;

    public String image;

    public NonSExprNodeType type;

    public NonSExprNode() {
        super("NonSExprNode");
    }

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visitNonSExprNode(this);
    }

    public enum NonSExprNodeType {
        COMMENT,EMPTY
    }

    @Override
    public String toString(String indent) {
        return " ";
    }
}
