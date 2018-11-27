package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;
import com.threelambda.minilisp.node.SymbolExprNode;
import com.threelambda.minilisp.node.SymbolNode;

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

        } catch (Exception e) {
            throw new RuntimeException("MacroFunc eval fail", e);
        }
        return new NullType();
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
                CellNode cellNode = (CellNode) expr.node;
                SExprNode firstSExpr = CellNodeUtil.getFirst(cellNode);
                Type visit = visitor.visit(firstSExpr);

                if (visit instanceof MacroFunc) {

                } else {
                    if (visit instanceof FuncType) {
                        result = visitor.visit(cellNode);
                    }
                }

                bodyCopy = (CellNode) bodyCopy.cdr;
            } catch (Exception e) {
                throw new RuntimeException("evalBody while loop fail.", e);
            }
        }

        assert result != null;
        visitor.popEnv();

        //把result包装为ExprType
        ExprType exprType = new ExprType();
        CellNode cellNode = new CellNode();
        exprType.cellNode = cellNode;
        SExprNode sExprNode = new SExprNode();
        cellNode.car = sExprNode;
        cellNode.cdr = CellNode.NIL;
        SymbolExprNode symbolExprNode = new SymbolExprNode();
        sExprNode.node = symbolExprNode;
        if(result instanceof NumType) {
            NumType num = (NumType) result;
            symbolExprNode.node = new SymbolNode("Num", num.val.toString());
        }
        return exprType;
    }
}
