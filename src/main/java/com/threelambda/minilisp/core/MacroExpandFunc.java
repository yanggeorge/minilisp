package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;
import com.threelambda.minilisp.node.SymbolExprNode;

/**
 * @author yangming 2018/10/4
 */
public class MacroExpandFunc extends FuncType {

    public MacroExpandFunc() {
        super("MacroExpandFunc");
    }

    public Type eval(Visitor visitor, CellNode params) {
        try {
            //首先对入参进行求值。
            SExprNode first = CellNodeUtil.getFirst(params);
            Type result = visitor.visit(first);
            if (result instanceof ExprType) {
                return expand((ExprType) result, visitor);
            } else {
                return result;
            }
        } catch (Exception e) {
            throw new RuntimeException("MacroExpand eval fail.", e);
        }
    }

    public static Type expand(ExprType expr, Visitor visitor) {
        //1. 如果是 简单的SymbolExpr，则直接返回
        //2. 如果是 函数，则直接返回
        //3. 如果是 宏，则进行扩展
        //   3.1 宏内部的函数直接求值
        //   3.2 宏内部的宏则进行直接扩展，不绑定实参。
        CellNode cellNode = expr.cellNode;
        assert CellNodeUtil.length(cellNode) == 1;
        SExprNode firstSExpr = CellNodeUtil.getFirst(expr.cellNode);
        if (firstSExpr.node instanceof SymbolExprNode) {
            //1. 如果是 简单的SymbolExpr，则直接返回
            return expr;
        } else if (firstSExpr.node instanceof CellNode) {
            CellNode cell = (CellNode) firstSExpr.node;
            int length = CellNodeUtil.length(cell);

            SExprNode car = (SExprNode) cell.car;
            Type visit = visitor.visit(car);
            if (visit instanceof MacroFunc) {
                MacroFunc macroFunc = (MacroFunc) visit;
                CellNode params = CellNodeUtil.nextCell(cell);
                return expand(macroFunc.expand(visitor, params), visitor);
            } else {
                if (visit instanceof FuncType) {


                    return expr;
                }
            }
        }
        return null;
    }

}
