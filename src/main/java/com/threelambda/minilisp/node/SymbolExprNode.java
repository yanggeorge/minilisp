package com.threelambda.minilisp.node;

import com.threelambda.minilisp.core.Visitor;

/**
 * Created by YM on 5/26/2017.
 */
public class SymbolExprNode extends Node {

    public Node node;

    public SymbolExprNode() {
        super("SymbolExprNode");
    }

    public SymbolExprNode(Node node) {
        super("SymbolExprNode");
        this.node = node;
    }

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visitSymbolExprNode(this);
    }

    @Override
    public String toString(String indent) {
        String s = "";
        if (node instanceof SymbolNode) {
            SymbolNode symbolNode = (SymbolNode) node;
            s = symbolNode.toString("");
        } else {
            throw new RuntimeException("Not support node");
        }
        return s;
    }
}
