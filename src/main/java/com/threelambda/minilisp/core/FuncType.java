package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;

import java.util.Stack;

/**
 * Created by ym on 6/1/2017.
 */
public abstract class FuncType extends Type {

    public Stack<Env> closureEnvs = null;
    public String name ;
    public FuncType() {
        super("FuncType");
    }

    public FuncType(String name) {
        super("FuncType");
        this.name = name;
    }

    abstract Type eval(Visitor visitor, CellNode cellNode);

    @Override
    public String toString() {
        return String.format("<FuncType:%s>", name);
    }
}
