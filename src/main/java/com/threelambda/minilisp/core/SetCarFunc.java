package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;

public class SetCarFunc extends FuncType {

    public SetCarFunc() {
        super("SetCarFunc");
    }

    @Override
    Type eval(Visitor visitor, CellNode params) {
        try {
            assert CellNodeUtil.length(params) == 2;

            SExprNode first = CellNodeUtil.getFirst(params);
            SExprNode second = CellNodeUtil.getFirst(CellNodeUtil.nextCell(params));

            ExprType firstResult = (ExprType) visitor.visit(first);
            ExprType secondResult = (ExprType) visitor.visit(second);

            CellNode cellNode = (CellNode) firstResult.sExprNode.node;
            cellNode.car = secondResult.sExprNode;
            return firstResult;
        } catch (Exception e) {
            throw new RuntimeException("Malformed SetCarFunc.", e);
        }
    }
}
