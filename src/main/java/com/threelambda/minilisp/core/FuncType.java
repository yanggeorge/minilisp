package com.threelambda.minilisp.core;

import java.util.Stack;

/**
 * Created by ym on 6/1/2017.
 */
public class FuncType extends Type {

    public Stack<Env> closureEnvs = null;
    public String name ;
    public FuncType() {
        super("FuncType");
    }

    public FuncType(String name) {
        super("FuncType");
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("<FuncType:%s>", name);
    }
}
