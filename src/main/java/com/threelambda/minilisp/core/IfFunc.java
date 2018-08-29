package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;

/**
 * @author yangming 2018/8/29
 */
public class IfFunc extends FuncType {
    public IfFunc() {
        super("IfFunc");
    }

    /**
     * 第一个cellNode是condition
     * 如果为true则返回第二个cellNode
     * 如果为false则返回最后一个cellNode
     *
     * 注意与rui314的实现保持一致
     * @param visitor
     * @param cellNode
     * @return
     */
    public Type eval(Visitor visitor, CellNode cellNode) {
        try {
            

        } catch (Exception e) {
            throw new RuntimeException("Malformed if func.");
        }

        return new StringType("");
    }
}
