package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.*;

import java.util.Stack;

/**
 * Created by ym on 5/31/2017.
 */
public class LispVisitor implements Visitor {

    public String[] KEYWORDS = new String[]{
            "lambda", "println", "define", "defun",
            "setq", "quote", "cons", "if", "defmacro",
            "macroexpand"
    };

    private Stack<Env> envs = new Stack<>();

    public LispVisitor(Env env) {
        pushEnv(env);
    }

    public LispVisitor() {
    }

    @Override
    public Type visitExprNode(ExprNode exprNode) {
        Node node = exprNode.node;
        switch (node.kind) {
            case "NonSExprNode":
                return this.visitNonSExprNode((NonSExprNode) node);
            case "SExprNode":
                SExprNode se = (SExprNode) node;
                return this.visitSExprNode(se);
        }
        return null;
    }

    @Override
    public Type visitSExprNode(SExprNode sExprNode) {
        Node child = sExprNode.node;
        String kind = child.kind;
        switch (kind) {
            case "SymbolExprNode":
                SymbolExprNode sym = (SymbolExprNode) child;
                return this.visitSymbolExprNode(sym);
            case "CellNode":
                CellNode cellNode = (CellNode) child;
                return this.visitCellNode(cellNode);
            case "SQuoteExprNode":
                SQuoteExprNode sQuoteExprNode = (SQuoteExprNode) child;
                return this.visitSQuoteExprNode(sQuoteExprNode);
        }

        return null;
    }

    @Override
    public Type visitNonSExprNode(NonSExprNode node) {
        return new StringType("");
    }

    @Override
    public Type visitSymbolExprNode(SymbolExprNode symbolExprNode) {
        Node child = symbolExprNode.node;
        String kind = child.kind;
        if (kind.equalsIgnoreCase("SymbolNode")) {
            return this.visitSymbolNode((SymbolNode) child);
        }
        return null;
    }

    @Override
    public Type visitSQuoteExprNode(SQuoteExprNode node) {
        SExprNode sExprNode = (SExprNode) node.node;
        ExprType exprType = new ExprType();
        CellNode cellNode = new CellNode();
        cellNode.car = sExprNode;
        cellNode.cdr = CellNode.NIL;
        exprType.cellNode = cellNode;
        return exprType;
    }

    @Override
    public Type visitCellNode(CellNode node) {
        if (node.nil) {
            return new NullType();
        }
        Node first = node.car;
        CellNode params = (CellNode) node.cdr;
        if (first instanceof SExprNode) {
            if (Util.isLambdaFunc((SExprNode) first)) {
                return Util.buildLambdaFunc(params);
            } else {
                Type result = this.visitSExprNode((SExprNode) first);
                if (result instanceof FuncType) {
                    FuncType func = (FuncType) result;
                    if (func instanceof PrintLnFunc) {
                        PrintLnFunc printLnFunc = (PrintLnFunc) func;
                        result = printLnFunc.eval(this, params);
                        return result;
                    } else if (func instanceof DefineFunc) {
                        DefineFunc defineFunc = (DefineFunc) func;
                        result = defineFunc.eval(this, params);
                        return result;
                    } else if (func instanceof AddFunc) {
                        AddFunc addFunc = (AddFunc) func;
                        result = addFunc.eval(this, params);
                        return result;
                    } else if (func instanceof MinusFunc) {
                        MinusFunc minusFunc = (MinusFunc) func;
                        result = minusFunc.eval(this, params);
                        return result;
                    } else if (func instanceof LambdaFunc) {
                        LambdaFunc lambdaFunc = (LambdaFunc) func;
                        result = lambdaFunc.eval(this, params);
                        return result;
                    } else if (func instanceof DefunFunc) {
                        DefunFunc defunFunc = (DefunFunc) func;
                        result = defunFunc.eval(this, params);
                        return result;
                    } else if (func instanceof SetqFunc) {
                        SetqFunc setqFunc = (SetqFunc) func;
                        result = setqFunc.eval(this, params);
                        return result;
                    } else if (func instanceof QuoteFunc) {
                        QuoteFunc quoteFunc = (QuoteFunc) func;
                        result = quoteFunc.eval(this, params);
                        return result;
                    } else if (func instanceof ConsFunc) {
                        ConsFunc consFunc = (ConsFunc) func;
                        result = consFunc.eval(this, params);
                        return result;
                    } else if (func instanceof IfFunc) {
                        IfFunc ifFunc = (IfFunc) func;
                        result = ifFunc.eval(this, params);
                        return result;
                    } else if (func instanceof EqFunc) {
                        EqFunc eqFunc = (EqFunc) func;
                        result = eqFunc.eval(this, params);
                        return result;
                    } else if (func instanceof LtFunc) {
                        LtFunc eqFunc = (LtFunc) func;
                        result = eqFunc.eval(this, params);
                        return result;
                    } else if (func instanceof GtFunc) {
                        GtFunc eqFunc = (GtFunc) func;
                        result = eqFunc.eval(this, params);
                        return result;
                    } else if (func instanceof LeFunc) {
                        LeFunc eqFunc = (LeFunc) func;
                        result = eqFunc.eval(this, params);
                        return result;
                    } else if (func instanceof GeFunc) {
                        GeFunc eqFunc = (GeFunc) func;
                        result = eqFunc.eval(this, params);
                        return result;
                    } else if (func instanceof DefMacroFunc) {
                        DefMacroFunc defmacroFunc = (DefMacroFunc) func;
                        result = defmacroFunc.eval(this, params);
                        return result;
                    } else if (func instanceof MacroExpandFunc) {
                        MacroExpandFunc macroExpandFunc = (MacroExpandFunc) func;
                        result = macroExpandFunc.eval(this, params);
                        return result;
                    }
                } else if (result instanceof Macro) {
                    Macro macro = (Macro) result;
                    result = macro.eval(this, params);
                    return result;
                }
                throw new RuntimeException("The head of a list must be a function");
            }
        }
        return new NullType();
    }

    @Override
    public Type visitSymbolNode(SymbolNode node) {
        String type = node.type;
        String image = node.image;
        switch (type) {
            case "PLUS":
                return this.seekValue("<AddFunc>");
            case "MINUS":
                return this.seekValue("<MinusFunc>");
            case "NUM":
                return new NumType(Integer.valueOf(image));
            case "ID":
                return this.seekValue(image);
            case "EQ":
                return this.seekValue("<EqFunc>");
            case "LT":
                return this.seekValue("<LtFunc>");
            case "LE":
                return this.seekValue("<LeFunc>");
            case "GT":
                return this.seekValue("<GtFunc>");
            case "GE":
                return this.seekValue("<GeFunc>");
            default:
                throw new RuntimeException("Symbol not exist:" + node.toString());
        }
    }

    @Override
    public boolean isKeyword(String image) {
        for (String keyword : KEYWORDS) {
            if (keyword.equals(image)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Type visit(Node node) {
        String kind = node.kind;
        switch (kind) {
            case "ExprNode":
                return this.visitExprNode((ExprNode) node);
            case "SExprNode":
                return this.visitSExprNode((SExprNode) node);
            case "CellNode":
                return this.visitCellNode((CellNode) node);
            case "SymbolExprNode":
                return this.visitSymbolExprNode((SymbolExprNode) node);
            case "SymbolNode":
                return this.visitSymbolNode((SymbolNode) node);
            default:
                throw new RuntimeException("node kind not exist.");
        }
    }

    @Override
    public Type visitFuncNode(FuncNode funcNode) {
        return null;
    }

    @Override
    public void pushEnv(Env env) {
        this.envs.push(env);
    }

    @Override
    public Env popEnv() {
        return this.envs.pop();
    }

    @Override
    public Env peekEnv() {
        return this.envs.peek();
    }

    @Override
    public Type seekValue(String name) {
        //envs的末尾是栈顶。需要从末尾开始遍历。
        for (int i = this.envs.size() - 1; i > -1; i--) {
            Env env = this.envs.get(i);
            if (env.isDefined(name)) {
                return env.get(name);
            }
        }
        throw new RuntimeException(String.format("<Symbol:%s> is not defined.", name));
    }

    @Override
    public Type seekAndSetValue(String name, Type val) {
        //envs的末尾是栈顶。需要从末尾开始遍历。
        for (int i = this.envs.size() - 1; i > -1; i--) {
            Env env = this.envs.get(i);
            if (env.isDefined(name)) {
                env.update(name, val);
                return new NullType();
            }
        }
        throw new RuntimeException(String.format("<Symbol:%s> is not defined.", name));
    }

    @Override
    public Stack<Env> getEnvStack() {
        return this.envs;
    }

    @Override
    public void setEnvStack(Stack<Env> envs) {
        this.envs = envs;
    }
}
