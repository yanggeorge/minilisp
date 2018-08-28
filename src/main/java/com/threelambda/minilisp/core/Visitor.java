package com.threelambda.minilisp.core;

import java.util.Stack;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.ExprNode;
import com.threelambda.minilisp.node.Node;
import com.threelambda.minilisp.node.NonSExprNode;
import com.threelambda.minilisp.node.SExprNode;
import com.threelambda.minilisp.node.SQuoteExprNode;
import com.threelambda.minilisp.node.SymbolExprNode;
import com.threelambda.minilisp.node.SymbolNode;

/**
 * Created by ym on 5/31/2017.
 */
public interface Visitor {
    Type visitExprNode(ExprNode node) ;

    Type visitSExprNode(SExprNode node) ;

    Type visitNonSExprNode(NonSExprNode node) ;

    Type visitSymbolExprNode(SymbolExprNode node) ;

    Type visitSQuoteExprNode(SQuoteExprNode node) ;

    Type visitSymbolNode(SymbolNode node) ;

    Type visit(Node node) ;

    Type visitCellNode(CellNode node) ;

    void pushEnv(Env env);

    Env popEnv();

    Env peekEnv();

    Type seekValue(String name);

    Type seekAndSetValue(String name, Type val);


    boolean isKeyword(String image);

    Stack<Env> getEnvStack();

    void setEnvStack(Stack<Env> envStack);
}
