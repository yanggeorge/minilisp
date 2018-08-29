package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;

/**
 * Created by YM on 6/8/2017.
 */
public class SetqFunc extends FuncType {

    public SetqFunc() {
        super("SetqFunc");
    }

    public Type eval(Visitor visitor, CellNode cellNode) {
        try {
            SExprNode first = (SExprNode)cellNode.car;
            SExprNode second = (SExprNode)((CellNode)cellNode.cdr).car;
            CellNode third = (CellNode)((CellNode)cellNode.cdr).cdr;
            assert third.nil == true;
            StringType name = (StringType)Util.getSymbolName(visitor, first);
            Type val = visitor.visit(second);
            visitor.seekAndSetValue(name.val, val);

        } catch (Exception e) {
            throw new RuntimeException("Malformed setq func.");
        }

        return new StringType("");
    }
}
