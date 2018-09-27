package com.threelambda.minilisp.core;

/**
 * Created by ym on 9/24/2018.
 */
public class BoolType extends Type {

    private Boolean value;

    public BoolType(String kind) {
        super("BoolType");
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}
