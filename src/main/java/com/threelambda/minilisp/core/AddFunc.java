package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.CellNode;

/**
 * Created by ym on 6/3/2017.
 */
public class AddFunc extends FuncType {

    public AddFunc() {
        super("AddFunc");
    }

    public Type eval(Visitor visitor, CellNode cellNode){
        Integer result = 0;
        while(! cellNode.nil){
            Type tmp = null;

            try {
                tmp = visitor.visit(cellNode.car);
                if(tmp.kind.equals("NumType")){
                    result += ((NumType)tmp).val;

                }else if(tmp.kind.equals("ExprType")){
                    ExprType e = (ExprType) tmp;
                    tmp = visitor.visit(e.cellNode.car);
                    result += ((NumType)tmp).val;
                }
            cellNode = (CellNode)cellNode.cdr;
            } catch (Exception e) {
                throw new RuntimeException("Malformed AddFunc.",e);
            }
        }
        return new NumType(result);
    }
}
