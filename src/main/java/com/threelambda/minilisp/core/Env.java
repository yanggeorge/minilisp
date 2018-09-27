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
        DefineFunc defineFunc = new DefineFunc();
        hash.put("define", defineFunc);
        PrintLnFunc printLnFunc = new PrintLnFunc();
        hash.put("println", printLnFunc);
        AddFunc addFunc = new AddFunc();
        hash.put("<AddFunc>", addFunc);
        MinusFunc minusFunc = new MinusFunc();
        hash.put("<MinusFunc>", minusFunc);
        DefunFunc defunFunc = new DefunFunc();
        hash.put("defun", defunFunc);
        SetqFunc setqFunc = new SetqFunc();
        hash.put("setq", setqFunc);
        QuoteFunc quoteFunc = new QuoteFunc();
        hash.put("quote", quoteFunc);
        ConsFunc consFunc = new ConsFunc();
        hash.put("cons", consFunc);
        IfFunc ifFunc = new IfFunc();
        hash.put("if", ifFunc);
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
