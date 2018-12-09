package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;

/**
 * @author yangming 2018/9/27
 */
public class GtFunc extends FuncType {

    public GtFunc() {
        super("GtFunc");
    }

    public Type eval(Visitor visitor, CellNode cellNode) {
        try {
            Integer length = CellNodeUtil.length(cellNode);
            if (length != 2) {
                throw new RuntimeException("list size is not 2.");
            }

            SExprNode firstExpr = CellNodeUtil.getFirst(cellNode);
            SExprNode secondExpr = CellNodeUtil.getFirst(CellNodeUtil.nextCell(cellNode));

            Type firstResult = visitor.visit(firstExpr);
            if (!(firstResult instanceof NumType)) {
                throw new RuntimeException("must be num.");
            }
            Type secondResult = visitor.visit(secondExpr);
            if (!(secondResult instanceof NumType)) {
                throw new RuntimeException("must be num.");
            }

            NumType firstNum = (NumType) firstResult;
            NumType secondNum = (NumType) secondResult;
            if (firstNum.val > secondNum.val) {
                return new BoolType(true);
            } else {
                return new BoolType(false);
            }


        } catch (Exception e) {
            throw new RuntimeException("Malformed Eq func.", e);
        }
    }

}
