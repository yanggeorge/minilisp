package com.threelambda.minilisp.core;

/**
 * Created by ym on 9/24/2018.
 */
public class BoolType extends Type {

    private static Boolean value;

    public BoolType(String kind) {
        super("BoolType");
    }

    public static void setValue(Boolean value) {
        BoolType.value = value;
    }

    public static Boolean getValue() {
        return value;
    }
}
