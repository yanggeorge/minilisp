package com.threelambda.minilisp.node;

import com.threelambda.minilisp.core.FuncType;
import com.threelambda.minilisp.core.Visitor;

public class FuncNode extends Node {
    public FuncNode() {
        super("FuncNode");
    }

    public FuncType func;

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visitFuncNode(this);
    }

    @Override
    public String toString(String indent) {
        return func.toString();
    }

    @Override
    public String toString() {
        return func.toString();
    }
}
