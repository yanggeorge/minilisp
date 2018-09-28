package com.threelambda.minilisp.core;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Created by ym on 6/1/2017.
 */
public class Env {

    private HashMap<String,Type> hash = new HashMap<String, Type>();

    public Env(HashMap<String, Type> hash) {
        this.hash = hash;
    }

    public Env() {
    }

    public boolean isDefined(String symbol) {
        return hash.containsKey(symbol);
    }

    public Type getSymbolDef(String symbol) {
        return hash.getOrDefault(symbol, null);
    }

    public Env initPrimitiveFunc(){
        hash.put("define", new DefineFunc());
        hash.put("println", new PrintLnFunc());
        hash.put("<AddFunc>", new AddFunc());
        hash.put("<MinusFunc>", new MinusFunc());
        hash.put("defun", new DefunFunc());
        hash.put("setq", new SetqFunc());
        hash.put("quote", new QuoteFunc());
        hash.put("cons", new ConsFunc());
        hash.put("if", new IfFunc());
        hash.put("<EqFunc>", new EqFunc());
        hash.put("<LtFunc>", new LtFunc());
        hash.put("<LeFunc>", new LeFunc());
        hash.put("<GtFunc>", new GtFunc());
        hash.put("<GeFunc>", new GeFunc());
        return this;
    }

    public void update(String symbol, Type object) {
        hash.put(symbol, object);
    }

    public Type get(String symbol) {
        if(!isDefined(symbol)){
            throw new RuntimeException(String.format("<Symbol:%s>not defined.", symbol));
        }
        return hash.get(symbol);
    }

    public Env getCopy() {
        Env env = new Env();
        for (Entry<String, Type> entry : hash.entrySet()) {
            env.update(entry.getKey(),entry.getValue());
        }
        return env;
    }

    public static void main(String[] args) {
        Env env = new Env();
        env.initPrimitiveFunc();
        System.out.println(env.get("println"));
        env.update("a", new NumType(1));
        System.out.println(env.get("a"));
        env.update("a", new NumType(2));
        System.out.println(env.get("a"));
    }
}
