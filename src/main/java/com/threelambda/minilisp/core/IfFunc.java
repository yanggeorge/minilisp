package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;

/**
 * @author yangming 2018/8/29
 */
public class IfFunc extends FuncType {
    public IfFunc() {
        super("IfFunc");
    }

    /**
     * 第一个cellNode的car是condition
     * 如果为true则返回第二个cellNode的car
     * 如果为false则返回最后一个cellNode的car
     * <p>
     * 注意与rui314的实现保持一致
     *
     * @param visitor
     * @param cellNode
     * @return
     */
    public Type eval(Visitor visitor, CellNode cellNode) {
        try {
            Integer length = CellNodeUtil.length(cellNode);
            if (length < 2) {
                throw new RuntimeException("list is too short");
            }

            SExprNode cond = CellNodeUtil.getFirst(cellNode);
            Type result = visitor.visit(cond);
            // to define true and false .
            Boolean condValue = null;
            if (result instanceof NullType) {
                condValue = false;
            } else if (result instanceof BoolType) {
                BoolType boolResult = (BoolType) result;
                condValue = boolResult.getValue();
            } else if (result instanceof NumType) {
                condValue = true;
            } else if (result instanceof ExprType) {
                ExprType exprType = (ExprType) result;
                condValue = true;
            }

            if (condValue == null) {
                throw new RuntimeException("cond cannot eval");
            }
            CellNode next = CellNodeUtil.nextCell(cellNode);
            if (condValue) {
                SExprNode firstExpr = CellNodeUtil.getFirst(next);
                return visitor.visit(firstExpr);
            }

            next = CellNodeUtil.nextCell(next);
            Type secondResult = null;
            while (next != null && !next.nil) {
                SExprNode tmp = CellNodeUtil.getFirst(next);
                secondResult = visitor.visit(tmp);
                next = CellNodeUtil.nextCell(next);
            }
            if (secondResult != null) {
                return secondResult;
            } else {
                return new NullType();
            }

        } catch (Exception e) {
            throw new RuntimeException("Malformed if func.", e);
        }

    }
}
