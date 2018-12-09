package com.threelambda.minilisp.node;

import com.threelambda.minilisp.core.Visitor;

/**
 * Created by YM on 5/26/2017.
 */
public abstract class Node {
    public String tab = "    ";
    public String kind = "";

    public Node(String kind) {
        this.kind = kind;
    }

    abstract public void accept(Visitor visitor) throws Exception;

    public String toString(String indent) {
        return "Node";
    }
}
