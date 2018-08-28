package com.threelambda.minilisp.core;

/**
 * Created by ym on 6/2/2017.
 */
public class SymbolType extends Type {
    public String id;
    public Type val;
    public SymbolType(String id) {
        super("SymbolType");
        this.id = id;
    }

    public SymbolType() {
        super("SymbolType");
    }

    @Override
    public String toString() {
        return String.format("<Symbol:%s>", val);
    }
}
