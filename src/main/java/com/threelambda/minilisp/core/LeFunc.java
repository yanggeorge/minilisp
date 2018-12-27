package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;

import static com.threelambda.minilisp.core.Util.evalNumResult;

/**
 * @author yangming 2018/9/27
 */
public class LeFunc extends FuncType {

    public LeFunc() {
        super("LeFunc");
    }

    public Type eval(Visitor visitor, CellNode params) {
        try {
            CellNode cellNode = LambdaFunc.evalParam(visitor, params);

            Integer length = CellNodeUtil.length(cellNode);
            if (length != 2) {
                throw new RuntimeException("list size is not 2.");
            }

            SExprNode firstExpr = CellNodeUtil.getFirst(cellNode);
            SExprNode secondExpr = CellNodeUtil.getFirst(CellNodeUtil.nextCell(cellNode));

            Type firstResult = evalNumResult(visitor, firstExpr);
            Type secondResult = evalNumResult(visitor, secondExpr);

            NumType firstNum = (NumType) firstResult;
            NumType secondNum = (NumType) secondResult;
            if (firstNum.val <= secondNum.val) {
                return new BoolType(true);
            } else {
                return new BoolType(false);
            }


        } catch (Exception e) {
            throw new RuntimeException("Malformed Eq func.", e);
        }
    }

}
