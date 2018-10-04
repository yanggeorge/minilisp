package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;

/**
 * @author yangming 2018/10/4
 */
public class MacroExpandFunc extends FuncType {

    public MacroExpandFunc() {
        super("MacroExpandFunc");
    }

    public Type eval(Visitor visitor, CellNode cellNode) {

        return null;
    }

}
