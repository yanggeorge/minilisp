package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;

public class WhileFunc extends FuncType {

    public WhileFunc() {
        super("WhileFunc");
    }

    @Override
    Type eval(Visitor visitor, CellNode params) {
        try {
            assert CellNodeUtil.length(params) >= 2;
            SExprNode cond = CellNodeUtil.getFirst(params);
            while(evalCond(visitor, cond)){
                LambdaFunc.evalParam(visitor, CellNodeUtil.nextCell(params));
            }

        } catch (Exception e) {
            throw new RuntimeException("Malformed WhileFunc.", e);
        }

        return null;
    }

    private boolean evalCond(Visitor visitor, SExprNode cond) {
        BoolType result = (BoolType) visitor.visit(cond);
        return result.getValue();
    }
}
