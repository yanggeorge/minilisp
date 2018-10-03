package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;

/**
 * Created by YM on 6/7/2017.
 */
public class DefunFunc extends FuncType {

    public DefunFunc() {
        super("DefunFunc");
    }

    // defun 经过eval返回一个lambda func 。
    public Type eval(Visitor visitor, CellNode cellNode) {
        LambdaFunc lambdaFunc = null;

        try {
            SExprNode first = (SExprNode)cellNode.car;
            CellNode second = (CellNode)cellNode.cdr;
            StringType name = (StringType) Util.getSymbolName(visitor, first);
            lambdaFunc = Util.buildLambdaFunc(second);
            visitor.peekEnv().update(name.val, lambdaFunc);
        } catch (Exception e) {
            throw new RuntimeException("Defun eval fail.", e);
        }

        return lambdaFunc;
    }



}
