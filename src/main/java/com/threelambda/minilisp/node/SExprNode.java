package com.threelambda.minilisp.node;

import com.threelambda.minilisp.core.Visitor;

/**
 * Created by YM on 5/26/2017.
 */
public class SExprNode extends Node {

    public Node node;

    public SExprNode() {
        super("SExprNode");
    }

    public SExprNode(Node node) {
        super("SExprNode");
        this.node = node ;
    }

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visitSExprNode(this);
    }

    @Override
    public String toString(String indent) {
        String s = "";
        if (node instanceof SymbolExprNode) {
            SymbolExprNode symbolExprNode = (SymbolExprNode) node;
            s = indent + symbolExprNode.toString("");
        } else if (node instanceof CellNode) {
            CellNode cellNode = (CellNode) node;
            s = indent + "(" +  cellNode.toString("") + ")";
        } else if(node instanceof SQuoteExprNode) {
            SQuoteExprNode sQuoteExprNode = (SQuoteExprNode) node;
            s = indent + sQuoteExprNode.toString("");
        }else{
            throw new RuntimeException("Not support node.");
        }
        return s;
    }
}
