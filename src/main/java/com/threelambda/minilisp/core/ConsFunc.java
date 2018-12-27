package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.*;

/**
 * Created by ym on 7/24/2017.
 */
public class ConsFunc extends FuncType {
    public ConsFunc() {
        super("ConsFunc");
    }

    public Type eval(Visitor visitor, CellNode cellNode) {
        ExprType expr = new ExprType();
        try {
            SExprNode first = (SExprNode) cellNode.car;
            SExprNode second = (SExprNode) ((CellNode) cellNode.cdr).car;
            SExprNode third = (SExprNode) ((CellNode) ((CellNode) cellNode.cdr).cdr).car;
            assert third == null;
            Type firstResult = visitor.visit(first);
            Type secondResult = visitor.visit(second);

            ExprType firstExpr = getExprType(firstResult);
            ExprType secondExpr = getExprType(secondResult);
            return this.cons(firstExpr, secondExpr);
        } catch (Exception e) {
            throw new RuntimeException("Malformed ConsFunc.", e);
        }
    }

    private ExprType getExprType(Type result) {
        if (result instanceof ExprType) {
            return (ExprType) result;
        } else if (result instanceof NumType) {
            //如果是numType包装成ExprType
            NumType firstNum = (NumType) result;
            ExprType expr = new ExprType();
            expr.sExprNode = Util.convertToSExpr(firstNum);
            return expr;
        } else if (result instanceof StringType) {
            //todo 目前不支持String类型
            return new ExprType();
        } else if (result instanceof FuncType) {
            FuncType funcType = (FuncType) result;
            String s = funcType.toString();
            ExprType expr = new ExprType();
            expr.sExprNode = new SExprNode();
            SymbolExprNode symbolExprNode = new SymbolExprNode();
            expr.sExprNode.node = symbolExprNode;
            symbolExprNode.node = new SymbolNode("ID", s);
            return expr;
        } else if (result == null) {
            //包装成"()"
//            ExprType exprType = new ExprType();
//            exprType.sExprNode = new SExprNode();
//            exprType.sExprNode.node = CellNode.NIL;
            return Util.getEmptyList();
        }
        return null;
    }

    private ExprType cons(ExprType first, ExprType second) {
        if (second == null) {
            // null when eval '()'
            ExprType result = new ExprType();
            CellNode cellNode = new CellNode();
            cellNode.car = first.sExprNode;
            cellNode.cdr = CellNode.NIL;
            result.sExprNode = new SExprNode();
            result.sExprNode.node = cellNode;
            return result;
        } else if (isAtom(second.sExprNode)) {
            //atom element
            CellNode cellNode = new CellNode();
            cellNode.car = first.sExprNode;
            cellNode.cdr = second.sExprNode;

            ExprType result = new ExprType();
            result.sExprNode = new SExprNode();
            result.sExprNode.node = cellNode;
            return result;
        } else {
            //list expr
            ExprType expr = new ExprType();
            CellNode cellNode = new CellNode();
            cellNode.car = first.sExprNode;
            cellNode.cdr = second.sExprNode.node;
            expr.sExprNode = new SExprNode();
            expr.sExprNode.node = cellNode;
            return expr;
        }
    }

    private boolean isAtom(SExprNode sExprNode) {
        Node node = sExprNode.node;
        return node instanceof SymbolExprNode;
    }

}
