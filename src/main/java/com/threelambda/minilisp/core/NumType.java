package com.threelambda.minilisp.core;

/**
 * Created by ym on 6/1/2017.
 */
public class NumType extends Type {
    public Integer val;

    public NumType() {
        super("NumType");
    }

    public NumType(Integer val) {
        super("NumType");
        this.val = val;
    }

    @Override
    public String toString() {
        return String.format("%s", String.valueOf(val));
    }
}
