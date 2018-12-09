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
}
