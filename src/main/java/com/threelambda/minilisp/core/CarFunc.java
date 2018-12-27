package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;

public class CarFunc extends FuncType {

    public CarFunc() {
        super("CarFunc");
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
                exprType.sExprNode = (SExprNode) cellNode.car;
            }
            return exprType;
        } catch (Exception e) {
            throw new RuntimeException("Malformed CarFunc.", e);
        }
    }
}
