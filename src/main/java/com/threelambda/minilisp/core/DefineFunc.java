package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.Node;
import com.threelambda.minilisp.node.SExprNode;

/**
 * Created by ym on 6/2/2017.
 */
public class DefineFunc extends FuncType {


    public DefineFunc() {
        super("DefineFunc");
    }

    public Type eval(Visitor visitor, CellNode cellNode) {

        Node first = cellNode.car;
        Node second = ((CellNode) cellNode.cdr).car;
        String symbol = ((StringType) Util.getSymbolName(visitor, (SExprNode) first)).val;
        Env env = visitor.peekEnv();
        Type obj = null;
        try {
            obj = visitor.visit(second);
        } catch (Exception e) {
            throw new RuntimeException("Define eval fail.", e);
        }
        env.update(symbol, obj);
        return obj;
    }
}
