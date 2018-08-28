package com.threelambda.minilisp.node;

import com.threelambda.minilisp.core.Visitor;

/**
 * Created by YM on 5/26/2017.
 */
public class ExprNode extends Node {

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
        String s = "";
        if(node instanceof SExprNode){
            SExprNode sExprNode = (SExprNode) node;
            s = sExprNode.toString(indent) ;
        } else if (node instanceof NonSExprNode) {
            NonSExprNode nonSExprNode = (NonSExprNode) node;
            s = nonSExprNode.toString(indent);
        } else if( node instanceof SymbolExprNode){
            SymbolExprNode symbolExprNode = (SymbolExprNode) node;
            s = symbolExprNode.toString(indent);
        } else{
            throw new RuntimeException("Not support node.");
        }
        return s;
    }
}
