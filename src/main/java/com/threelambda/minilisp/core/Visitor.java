package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.*;

import java.util.LinkedList;

/**
 * Created by ym on 5/31/2017.
 */
public interface Visitor {
    Type visitExprNode(ExprNode node);

    Type visitSExprNode(SExprNode node);

    Type visitNonSExprNode(NonSExprNode node);

    Type visitSymbolExprNode(SymbolExprNode node);

    Type visitSQuoteExprNode(SQuoteExprNode node);

    Type visitSymbolNode(SymbolNode node);

    Type visit(Node node);

    Type visitCellNode(CellNode node);

    void pushEnv(Env env);

    Env popEnv();

    Env peekEnv();

    Type seekValue(String name);

    Type seekAndSetValue(String name, Type val);


    boolean isKeyword(String image);

    LinkedList<Env> getEnvStack();

    void setEnvStack(LinkedList<Env> envStack);

    Type visitFuncNode(FuncNode funcNode);

}
