package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;

/**
 * @author yangming 2018/8/29
 */
public class CellNodeUtil {

    /**
     * 获取第一个cell的car下的SExpr
     * @param cellNode
     * @return
     */
    public static SExprNode getFirst(CellNode cellNode) {

        return null;
    }

    /**
     * 获取最后一个cell的car下的SExpr
     * 如果最后一个cell为nil则car是null
     * @param cellNode
     * @return
     */
    public static SExprNode getLast(CellNode cellNode) {
        return null;
    }

    /**
     * 返回下一个cell
     * @param cellNode
     * @return
     */
    public static CellNode nextCell(CellNode cellNode) {
        return null;
    }
}
