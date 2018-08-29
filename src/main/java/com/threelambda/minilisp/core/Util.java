package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.Node;
import com.threelambda.minilisp.node.SExprNode;
import com.threelambda.minilisp.node.SymbolExprNode;
import com.threelambda.minilisp.node.SymbolNode;

/**
 * Created by ym on 6/4/2017.
 */
class Util {

    static boolean isLambdaFunc(SExprNode se) {
        try {
            SymbolExprNode symNode = (SymbolExprNode) se.node;
            SymbolNode sym = (SymbolNode) symNode.node;
            return sym.type.equals("ID") && sym.image.equals("lambda");
        } catch (Exception e) {
            return false;
        }

    }

    static LambdaFunc buildLambdaFunc(CellNode cellNode) {
        LambdaFunc lambdaFunc = new LambdaFunc();
        try {
            lambdaFunc.args = (CellNode) ((SExprNode)cellNode.car).node;
            lambdaFunc.body = (CellNode) cellNode.cdr;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Malformed lambda func.");
        }
        return lambdaFunc;
    }

    public static Type getSymbolName(Visitor visitor, SExprNode sExprNode) {
        try {
            SymbolExprNode symbolExprNode = (SymbolExprNode) sExprNode.node;
            SymbolNode sym = (SymbolNode) symbolExprNode.node;
            if (!sym.type.equals("ID")) {
                throw new Exception("sym type is not ID");
            }
            if (visitor.isKeyword(sym.image)) {
                throw new Exception("sym image is keyword.");
            }
            return new StringType(sym.image);

        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Malformed define func.");
        }


    }

    public static Node convertToSExpr(Type result) {
        if (result instanceof NumType) {
            NumType first = (NumType) result;
            SymbolNode sym = new SymbolNode("NUM", String.valueOf(first.val));
            SymbolExprNode sen = new SymbolExprNode(sym);
            return new SExprNode(sen);
        } else if (result instanceof StringType) {
            StringType first = (StringType) result;
            SymbolNode sym = new SymbolNode("ID", first.val);
            SymbolExprNode sen = new SymbolExprNode(sym);
            return new SExprNode(sen);
        } else if (result instanceof ExprType) {
            ExprType first = (ExprType) result;
            if (first.cellNode != null) {
                return first.cellNode;
            }
        } else {
            throw new RuntimeException("convertToSExpr failed.");
        }
        return null;
    }

}
