package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.Node;
import com.threelambda.minilisp.node.SExprNode;

public class CdrFunc extends FuncType {

    public CdrFunc() {
        super("CdrFunc");
    }

    @Override
    Type eval(Visitor visitor, CellNode params) {
        try {
            assert CellNodeUtil.length(params) == 1;

            Type result = visitor.visit(params.car);

            ExprType exprType = new ExprType();
            if (result instanceof ExprType) {
                ExprType e = (ExprType) result;
                CellNode cellNode = (CellNode) e.sExprNode.node;
                Node cdr = cellNode.cdr;
                if (cdr instanceof SExprNode) {
                    exprType.sExprNode = (SExprNode) cdr;
                } else if (cdr instanceof CellNode) {
                    exprType.sExprNode = new SExprNode();
                    exprType.sExprNode.node = cdr;
                }
            }
            return exprType;
        } catch (Exception e) {
            throw new RuntimeException("Malformed CdrFunc.", e);
        }
    }
}
