package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.Node;
import com.threelambda.minilisp.node.SExprNode;
import com.threelambda.minilisp.node.SymbolExprNode;
import com.threelambda.minilisp.node.SymbolNode;

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
            SExprNode first = (SExprNode)cellNode.car;
            SExprNode second = (SExprNode)((CellNode)cellNode.cdr).car;
            SExprNode third = (SExprNode)((CellNode)((CellNode)cellNode.cdr).cdr).car;
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
            return (ExprType)result;
        } else if (result instanceof NumType) {
            //如果是numType包装成ExprType
            NumType firstNum = (NumType)result;
            ExprType expr = new ExprType();
            expr.cellNode = new CellNode();
            expr.cellNode.car = new SExprNode();
            SymbolExprNode symbolExprNode = new SymbolExprNode();
            ((SExprNode)expr.cellNode.car).node = symbolExprNode;
            symbolExprNode.node = new SymbolNode("NUM", firstNum.val.toString());
            return expr;
        } else if (result instanceof StringType) {
            //todo 目前不支持String类型
            return new ExprType();
        }
        return null;
    }

    private ExprType cons(ExprType first, ExprType second) {
        if(second == null){
            // null when eval '()'
            ExprType expr = new ExprType();
            expr.cellNode = new CellNode();
            expr.cellNode.car = new SExprNode();
            ((SExprNode)expr.cellNode.car).node = ((SExprNode)first.cellNode.car).node;
            expr.cellNode.cdr = CellNode.NIL;

            ExprType result = new ExprType();
            result.cellNode = new CellNode();
            result.cellNode.car = new SExprNode();
            result.cellNode.cdr = CellNode.NIL;
            ((SExprNode)result.cellNode.car).node = expr.cellNode;
            return result;
        }else if (isAtom(second.cellNode)) {
            //atom element
            ExprType expr = new ExprType();
            expr.cellNode = new CellNode();
            expr.cellNode.car = new SExprNode();
            ((SExprNode)expr.cellNode.car).node = ((SExprNode)first.cellNode.car).node;
            expr.cellNode.cdr = second.cellNode.car;

            ExprType result = new ExprType();
            result.cellNode = new CellNode();
            result.cellNode.car = new SExprNode();
            result.cellNode.cdr = CellNode.NIL;
            ((SExprNode)result.cellNode.car).node = expr.cellNode;
            return result;
        } else {
            //list expr
            ExprType expr = new ExprType();
            expr.cellNode = new CellNode();
            expr.cellNode.car = new SExprNode();
            ((SExprNode)expr.cellNode.car).node = ((SExprNode)first.cellNode.car).node;
            expr.cellNode.cdr = new CellNode();
            ((CellNode)expr.cellNode.cdr).car = new SExprNode();
            if( ((CellNode)((SExprNode)second.cellNode.car).node).nil){
                expr.cellNode.cdr  = ((SExprNode)second.cellNode.car).node;
            }else {
                ((SExprNode)((CellNode)expr.cellNode.cdr).car).node
                    = ((SExprNode)((CellNode)((SExprNode)second.cellNode.car).node).car).node;
                ((CellNode)expr.cellNode.cdr).cdr = ((CellNode)((SExprNode)second.cellNode.car).node).cdr;
            }

            ExprType result = new ExprType();
            result.cellNode = new CellNode();
            result.cellNode.car = new SExprNode();
            result.cellNode.cdr = CellNode.NIL;
            ((SExprNode)result.cellNode.car).node = expr.cellNode;
            return result;
        }
    }

    private boolean isAtom(CellNode cellNode) {
        SExprNode sExprNode = (SExprNode)cellNode.car;
        Node node = sExprNode.node;
        return node instanceof SymbolExprNode;
    }

}
