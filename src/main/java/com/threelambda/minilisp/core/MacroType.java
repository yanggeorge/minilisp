package com.threelambda.minilisp.core;

import java.util.Stack;

/**
 * @author yangming 2018/10/3
 */
public class MacroType extends Type {

    public Stack<Env> closureEnvs = null;
    public String name;

    public MacroType(String kind) {
        super("MacroType");
    }

    @Override
    public String toString() {
        return String.format("<MacroType:%s>", name);
    }
}
