package com.threelambda.minilisp.core;

/**
 * Created by ym on 6/2/2017.
 */
public class StringType extends Type {
    public String val;

    public StringType(String val) {
        super("StringType");
        this.val = val;
    }

    public StringType() {
        super("StringType");
    }

    @Override
    public String toString() {
        return String.format("%s", val);
    }
}
