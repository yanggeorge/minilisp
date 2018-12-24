package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;

/**
 * Created by ym on 6/3/2017.
 */
public class MinusFunc extends FuncType {

    public MinusFunc() {
        super("MinusFunc");
    }

    public Type eval(Visitor visitor, CellNode cellNode) {
        Integer result = 0;
        boolean first = true;
        while (!cellNode.nil) {
            try {
                Type tmp = visitor.visit(cellNode.car);
                if (first) {
                    first = false;
                    if (tmp.kind.equals("NumType")) {
                        result += ((NumType) tmp).val;
                    } else if (tmp.kind.equals("ExprType")) {
                        ExprType e = (ExprType) tmp;
                        tmp = visitor.visit(e.sExprNode);
                        result += ((NumType) tmp).val;
                    }
                } else {
                    if (tmp.kind.equals("NumType")) {
                        result -= ((NumType) tmp).val;
                    } else if (tmp.kind.equals("ExprType")) {
                        ExprType e = (ExprType) tmp;
                        tmp = visitor.visit(e.sExprNode);
                        result -= ((NumType) tmp).val;
                    }
                }
                cellNode = (CellNode) cellNode.cdr;
            } catch (Exception e) {
                throw new RuntimeException("MinusFunc fail", e);
            }
        }

        return new NumType(result);
    }
}
