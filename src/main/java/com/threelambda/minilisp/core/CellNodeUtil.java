package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.Node;
import com.threelambda.minilisp.node.SExprNode;
import javafx.scene.control.Cell;

/**
 * @author yangming 2018/8/29
 */
public class CellNodeUtil {

    /**
     * 获取第一个cell的car下的SExpr
     *
     * @param cellNode
     * @return
     */
    public static SExprNode getFirst(CellNode cellNode) {
        if (cellNode != null) {
            return (SExprNode)cellNode.car;
        }
        return null;
    }

    /**
     * 返回下一个cell
     *
     * @param cellNode
     * @return
     */
    public static CellNode nextCell(CellNode cellNode) {
        if (!cellNode.nil) {
            Node cdr = cellNode.cdr;
            if (cdr instanceof CellNode) {
                return (CellNode)cdr;
            } else if (cdr instanceof SExprNode) {
                return null;
            }
        }

        return null;
    }

    /**
     * 获取最后一个cell的car下的SExpr
     * 如果最后一个cell为nil则car是null
     *
     * @param cellNode
     * @return
     */
    public static SExprNode getLastCar(CellNode cellNode) {
        CellNode lastCell = getLastCell(cellNode);
        return getFirst(lastCell);
    }


    public static CellNode getLastCell(CellNode cellNode) {
        CellNode curr = cellNode;
        CellNode next = nextCell(curr);
        while (next != null) {
            curr = next;
            next = nextCell(curr);
        }
        return curr;
    }

    public static Integer length(CellNode cellNode) {
        if (cellNode == null) {
            return 0;
        }
        int i = 0;
        while (cellNode != null && !cellNode.nil) {
            i ++ ;
            cellNode = nextCell(cellNode);
        }
        return i;
    }
}
