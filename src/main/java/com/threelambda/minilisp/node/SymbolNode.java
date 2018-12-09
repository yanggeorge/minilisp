package com.threelambda.minilisp.node;

import com.threelambda.minilisp.core.Visitor;

/**
 * Created by YM on 5/26/2017.
 */
public class SymbolNode extends Node {

    public String type;

    public String image;

    public SymbolNode(String type, String image) {
        super("SymbolNode");
        this.type = type;
        this.image = image;
    }

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visitSymbolNode(this);
    }

    @Override
    public String toString(String indent) {
        return indent + image;
    }
}
