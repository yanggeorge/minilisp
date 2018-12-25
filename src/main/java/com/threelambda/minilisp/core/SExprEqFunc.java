package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;

public class SExprEqFunc extends FuncType {

    public SExprEqFunc() {
        super("SExprEqFunc");
    }

    @Override
    Type eval(Visitor visitor, CellNode params) {
        try {
            assert CellNodeUtil.length(params) == 2;

            SExprNode first = CellNodeUtil.getFirst(params);
            SExprNode second = CellNodeUtil.getFirst(CellNodeUtil.nextCell(params));

            ExprType firstResult = (ExprType) visitor.visit(first);
            ExprType secondResult = (ExprType) visitor.visit(second);
            if(firstResult.toString().equalsIgnoreCase(secondResult.toString())){
                return new BoolType(true);
            }else{
                return new BoolType(false);
            }


        } catch (Exception e) {
            throw new RuntimeException("Malformed SExprEqFunc.",e);
        }
    }
}
