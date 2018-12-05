package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.*;

import java.util.UUID;

/**
 * @author yangming 2018/10/3
 */
public class MacroFunc extends FuncType {

    public String id;
    public CellNode args;
    public CellNode body;

    public MacroFunc() {
        super("MacroFunc");
        this.id = UUID.randomUUID().toString();
        this.args = CellNode.NIL;
        this.body = CellNode.NIL;
    }

    public Type eval(Visitor visitor, CellNode params) {
        try {
            ExprType expr = this.expand(visitor, params);
            Type result = visitor.visit(expr.cellNode);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("MacroFunc eval fail", e);
        }
    }

    public ExprType expand(Visitor visitor, CellNode params) {

        //args与params进行绑定
        Env local = new Env();
        LambdaFunc.evalParam(visitor, args, params, local);

        visitor.pushEnv(local);
        Type result = null;
        CellNode bodyCopy = body;
        while (!bodyCopy.nil) {
            try {
                SExprNode expr = (SExprNode) bodyCopy.car;
                Node node = expr.node;
                if (node instanceof SymbolExprNode) {
                    result = visitor.visit(node);
                } else if (node instanceof CellNode) {
                    CellNode cellNode = (CellNode) expr.node;
                    SExprNode firstSExpr = CellNodeUtil.getFirst(cellNode);
                    Type visit = visitor.visit(firstSExpr);

                    if (visit instanceof MacroFunc) {

                    } else {
                        if (visit instanceof FuncType) {
                            FuncType func = (FuncType) visit;
                            CellNode nextCell = CellNodeUtil.nextCell(cellNode);
                            CellNode funcParams = bindParam(nextCell, visitor);
                            result = func.eval(visitor, funcParams);
                        }
                    }
                } else if (node instanceof SQuoteExprNode) {

                }

                bodyCopy = (CellNode) bodyCopy.cdr;
            } catch (Exception e) {
                throw new RuntimeException("evalBody while loop fail.", e);
            }
        }

        assert result != null;
        visitor.popEnv();

        if (result instanceof ExprType) {
            return (ExprType) result;
        }
        //把result包装为ExprType
        ExprType exprType = new ExprType();
        CellNode cellNode = new CellNode();
        exprType.cellNode = cellNode;
        SExprNode sExprNode = new SExprNode();
        cellNode.car = sExprNode;
        cellNode.cdr = CellNode.NIL;
        SymbolExprNode symbolExprNode = new SymbolExprNode();
        sExprNode.node = symbolExprNode;
        if (result instanceof NumType) {
            NumType num = (NumType) result;
            symbolExprNode.node = new SymbolNode("Num", num.val.toString());
        }
        return exprType;

    }

    private CellNode bindParam(CellNode cellNode, Visitor visitor) {
        //顺序计算每个cell，把结果组成一个list
        CellNode head = new CellNode();
        CellNode tail = head;
        while (!cellNode.nil) {
            Type result = visitor.visit(cellNode.car);
            if (result instanceof NumType) {
                SExprNode sExprNode = new SExprNode();
                SymbolExprNode symbolExprNode = new SymbolExprNode();
                sExprNode.node = symbolExprNode;

                NumType num = (NumType) result;
                symbolExprNode.node = new SymbolNode("Num", num.val.toString());
                tail.car = sExprNode;
            } else if (result instanceof ExprType) {
                ExprType exprType = (ExprType) result;
                tail.car = exprType.cellNode.car;
            }
            tail.cdr = new CellNode();
            tail = (CellNode) tail.cdr;
            cellNode = (CellNode) cellNode.cdr;
        }
        tail.nil = true;

        return head;
    }

}
