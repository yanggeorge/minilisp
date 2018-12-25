package com.threelambda.minilisp.core;

import com.threelambda.minilisp.node.*;

import java.util.Stack;
import java.util.UUID;

/**
 * Created by ym on 6/4/2017.
 */
public class LambdaFunc extends FuncType {

    public String id;
    public CellNode args;
    public CellNode body;


    public LambdaFunc() {
        super("LambdaFunc");
        this.id = UUID.randomUUID().toString();
        this.args = CellNode.NIL;
        this.body = CellNode.NIL;
    }


    public Type eval(Visitor visitor, CellNode cellNode) {

        if (closureEnvs == null) {
            return evalBody(visitor, cellNode);
        } else {
            //closure有自己的环境。
            //eval的时候先要替换。
            Stack<Env> old = visitor.getEnvStack();
            visitor.setEnvStack(closureEnvs);

            Type result = evalBody(visitor, cellNode);

            visitor.setEnvStack(old);
            return result;
        }
    }

    private Type evalBody(Visitor visitor, CellNode cellNode) {
        Env local = new Env();

        CellNode evaluatedParams = evalParam(visitor, cellNode);
        bindParam(visitor, args, evaluatedParams, local);

        visitor.pushEnv(local);
        Type result = null;
        CellNode bodyCopy = body;
        while (!bodyCopy.nil) {
            try {
                result = visitor.visit(bodyCopy.car);
                bodyCopy = (CellNode) bodyCopy.cdr;
            } catch (Exception e) {
                throw new RuntimeException("evalBody while loop fail.", e);
            }
        }

        assert result != null;
        if (result.kind.equals("FuncType")) {
            keepEnvs((FuncType) result, visitor.getEnvStack());
        }
        Env env = visitor.popEnv();

        return result;
    }

    /**
     * 顺序计算每个cell，把结果组成一个list
     *
     * @param visitor
     * @param params  实参
     */
    public static CellNode evalParam(Visitor visitor, CellNode params) {

        CellNode head = new CellNode();
        CellNode tail = head;
        while (!params.nil) {
            Type result = visitor.visit(params.car);
            if (result instanceof NumType) {
                SExprNode sExprNode = new SExprNode();
                SymbolExprNode symbolExprNode = new SymbolExprNode();
                sExprNode.node = symbolExprNode;

                NumType num = (NumType) result;
                symbolExprNode.node = new SymbolNode("NUM", num.val.toString());
                tail.car = sExprNode;
            } else if (result instanceof ExprType) {
                ExprType exprType = (ExprType) result;
                tail.car = exprType.sExprNode;
            } else if (result instanceof FuncType) {
                //当参数是函数的时候，需要进行lexical scope binding。
                keepEnvs((FuncType) result, visitor.getEnvStack());
                //需要增加一个FuncNode，否则无法把FuncType的结果构成一个CellNodeList
                FuncNode funcNode = new FuncNode();
                funcNode.func = (FuncType) result;
                SExprNode sExprNode = new SExprNode();
                sExprNode.node = funcNode;
                tail.car = sExprNode;
            }
            tail.cdr = new CellNode();
            tail = (CellNode) tail.cdr;
            params = (CellNode) params.cdr;
        }
        tail.nil = true;

        return head;

    }

    /**
     * 输入的是一个S表达式，args也是一个S表达式。需要进行解析
     *
     * @param visitor
     * @param args            函数的形参
     * @param evaluatedParams 参数求值后的list
     * @param local           局部环境
     */
    public static void bindParam(Visitor visitor, CellNode args, CellNode evaluatedParams, Env local) {
        if (args.car == null) {
            return;
        }

        if (args.car instanceof SExprNode && evaluatedParams.car instanceof SExprNode) {
            Type name = Util.getSymbolName(visitor, (SExprNode) args.car);
            StringType nameString = (StringType) name;
            SExprNode param = (SExprNode) evaluatedParams.car;

            if (param.node instanceof FuncNode) {
                //如果入参是一个函数
                FuncNode funcNode = (FuncNode) param.node;
                local.update(nameString.val, funcNode.func);
            } else {
                ExprType exprType = new ExprType();
                exprType.sExprNode = param;
                local.update(nameString.val, exprType);
            }
        } else {
            throw new RuntimeException("Parameter must be Symbol");
        }

        if (!(evaluatedParams.cdr instanceof CellNode)) {
            throw new RuntimeException("params must be simple list");
        }

        if (args.cdr instanceof CellNode) {
            CellNode argsCdr = (CellNode) args.cdr;
            CellNode paramsCdr = (CellNode) evaluatedParams.cdr;
            if (!argsCdr.nil) {
                bindParam(visitor, argsCdr, paramsCdr, local);
            }
        } else if (args.cdr instanceof SExprNode) {
            SExprNode argsCdr = (SExprNode) args.cdr;
            if (argsCdr.node instanceof CellNode) {
                CellNode argsCdrNode = (CellNode) argsCdr.node;
                CellNode paramsCdr = (CellNode) evaluatedParams.cdr;
                if (!argsCdrNode.nil) {
                    bindParam(visitor, argsCdrNode, paramsCdr, local);
                }
            } else if (argsCdr.node instanceof SymbolExprNode) {
                Type name = Util.getSymbolName(visitor, argsCdr);
                StringType nameString = (StringType) name;

                SExprNode param = new SExprNode();
                param.node = evaluatedParams.cdr;
                ExprType exprType = new ExprType();
                exprType.sExprNode = param;
                local.update(nameString.val, exprType);
            }


        }
    }

    /**
     * 把当前环境保存在闭包中
     *
     * @param func
     * @param envs
     */
    public static void keepEnvs(FuncType func, Stack<Env> envs) {
        func.closureEnvs = new Stack<Env>();
        for (Env env : envs) {
            func.closureEnvs.add(env.getCopy());
        }
    }

    @Override
    public String toString() {
        return String.format("(lambda (%s) %s)", args.toString(""), body.toString(""));
    }
}
