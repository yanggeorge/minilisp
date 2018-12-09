package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;

/**
 * Created by ym on 6/2/2017.
 */
public class PrintLnFunc extends FuncType {


    public PrintLnFunc() {
        super("PrintLnFunc");
    }

    public Type eval(Visitor visitor, CellNode cellNode) {
        String s = "";
        while (!cellNode.nil) {
            Type result = visitor.visit(cellNode.car);
            String kind = result.kind;
            Object val = null;
            switch (kind) {
                case "NumType":
                    val = result;
                    break;
                case "StringType":
                    val = ((StringType) result).val;
                    break;
                case "FuncType":
                    val = result;
                    break;
                case "NullType":
                    val = "()";
                    break;
                case "ExprType":
                    val = result.toString();
                    break;
                case "BoolType":
                    val = ((BoolType) result).toString();
                    break;
                default:
                    throw new RuntimeException("type not known.");
            }
            if (s.length() == 0) {
                s += String.format("%s", val);
            } else {
                s += String.format(" %s", val);
            }
            cellNode = (CellNode) cellNode.cdr;
        }
        if (s.length() != 0) {
            //NonSExprNode返回""
            s += "\n";
            System.out.printf(s);
        }
        return new StringType("");
    }
}
