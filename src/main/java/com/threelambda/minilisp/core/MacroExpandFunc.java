package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;

/**
 * @author yangming 2018/10/4
 */
public class MacroExpandFunc extends FuncType {

    public MacroExpandFunc() {
        super("MacroExpandFunc");
    }

    public Type eval(Visitor visitor, CellNode params) {
        //1. 如果是 简单的symbol，则直接返回
        //2. 如果是 函数，则直接返回
        //3. 如果是 宏，则进行进行计算
        try {
            SExprNode first = CellNodeUtil.getFirst(params);
            Type result = visitor.visit(first);
            if (result instanceof ExprType) {
                ExprType expr = (ExprType) result;
                CellNode node = expr.cellNode;

                return expr;
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("MacroExpand eval fail.", e);
        }
    }

}
