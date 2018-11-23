package com.threelambda.minilisp.core;

import java.util.UUID;

import com.threelambda.minilisp.node.CellNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public Type eval(Visitor visitor, CellNode cellNode) {
        try {

        } catch (Exception e) {
            throw new RuntimeException("MacroFunc eval fail", e);
        }
        return new NullType();
    }

    public ExprType expand(Visitor visitor, CellNode params) {


        return null;
    }
}
