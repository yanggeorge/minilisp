package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.*;

import java.util.UUID;

/**
 * @author yangming 2018/10/3
 */
public class Macro extends MacroType {

    public String id;
    public CellNode args;
    public CellNode body;

    public Macro() {
        super("Macro");
        this.id = UUID.randomUUID().toString();
        this.args = CellNode.NIL;
        this.body = CellNode.NIL;
    }

    public Type eval(Visitor visitor, CellNode params) {
        try {
            ExprType expr = this.expand(visitor, params);
            Type result = visitor.visit(expr.sExprNode);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Macro eval fail", e);
        }
    }

    public ExprType expand(Visitor visitor, CellNode params) {

        //args与params进行绑定
        Env local = new Env();
        LambdaFunc.bindParam(visitor, args, params, local);

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

                    if (visit instanceof Macro) {

                    } else {
                        if (visit instanceof FuncType) {
                            FuncType func = (FuncType) visit;
                            CellNode nextCell = CellNodeUtil.nextCell(cellNode);
                            result = func.eval(visitor, nextCell);
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
        exprType.sExprNode = new SExprNode();
        SymbolExprNode symbolExprNode = new SymbolExprNode();
        exprType.sExprNode.node = symbolExprNode;
        if (result instanceof NumType) {
            NumType num = (NumType) result;
            symbolExprNode.node = new SymbolNode("Num", num.val.toString());
        }
        return exprType;

    }

    @Override
    public String toString() {
        return String.format("(macro (%s) %s)", args.toString(""), body.toString(""));
    }
}
