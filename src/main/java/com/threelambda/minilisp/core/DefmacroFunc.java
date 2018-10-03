package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;

/**
 * @author yangming 2018/10/3
 */
public class DefmacroFunc extends FuncType {

    public DefmacroFunc() {
        super("defmacro");
    }

    public Type eval(Visitor visitor, CellNode cellNode) {
        MacroFunc macroFunc = null;
        try {
            SExprNode first = (SExprNode)cellNode.car;
            CellNode second = (CellNode)cellNode.cdr;
            StringType name = (StringType)Util.getSymbolName(visitor, first);
            macroFunc = Util.buildMacroFunc(second);
            visitor.peekEnv().update(name.val, macroFunc);
        } catch (Exception e) {
            throw new RuntimeException("DefmacroFunc eval fail.", e);
        }
        return null;
    }
}
