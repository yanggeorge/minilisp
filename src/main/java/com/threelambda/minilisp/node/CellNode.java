package com.threelambda.minilisp.node;

import com.threelambda.minilisp.core.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yangming 2018/1/15
 */
public class CellNode extends Node {

    private static final Logger logger = LoggerFactory.getLogger(CellNode.class);

    public Node car;
    public Node cdr;

    public boolean nil = false;

    public static CellNode NIL = new CellNode();

    static {
        NIL.nil = true;
    }

    public CellNode() {
        super("CellNode");
    }

    public boolean getNil() {
        return nil;
    }

    public void setNil(boolean nil) {
        this.nil = nil;
    }

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visitCellNode(this);
    }

    @Override
    public String toString(String indent) {
        if (nil) {
            return "";
        }
        String s = indent;
        if (car == null) {
            return "";
        }

        if (car instanceof SExprNode) {
            SExprNode carNode = (SExprNode) car;
            s += carNode.toString("");
        } else {
            throw new RuntimeException("Not support node");
        }

        if (cdr == null) {
            return s;
        }

        if (cdr instanceof SExprNode) {
            SExprNode sExprNode = (SExprNode) cdr;
            s += " . " + sExprNode.toString("");
        } else if (cdr instanceof CellNode) {
            CellNode next = (CellNode) cdr;
            if (!next.nil) {
                s += " " + next.toString("");
            }
        } else {
            throw new RuntimeException("Not support node");
        }

        return s;
    }

    @Override
    public String toString() {
        return toString("");
    }
}
