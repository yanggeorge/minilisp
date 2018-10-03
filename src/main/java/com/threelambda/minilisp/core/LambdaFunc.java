package com.threelambda.minilisp.core;

import java.util.Stack;
import java.util.UUID;

import com.threelambda.minilisp.node.CellNode;
import com.threelambda.minilisp.node.SExprNode;
import com.threelambda.minilisp.node.SymbolExprNode;

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

        evalParam(visitor, args, cellNode, local);

        visitor.pushEnv(local);
        Type result = null;
        CellNode bodyCopy = body;
        while(!bodyCopy.nil){
            try {
                result = visitor.visit(bodyCopy.car);
                bodyCopy = (CellNode) bodyCopy.cdr;
            } catch (Exception e) {
                e.printStackTrace();
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
     * 输入的是一个S表达式，args也是一个S表达式。需要进行解析
     * @param visitor
     * @param args  函数的形参
     * @param params 实参
     * @param local
     */
    private void evalParam(Visitor visitor, CellNode args, CellNode params, Env local) {
        if (args.car == null) {
            return;
        }

        if(args.car instanceof SExprNode && params.car instanceof SExprNode){
            Type name = Util.getSymbolName(visitor, (SExprNode) args.car);
            StringType nameString = (StringType) name;
            SExprNode param = (SExprNode) params.car;
            Type tmp = visitor.visit(param);
            if(tmp instanceof FuncType){
                //当参数是函数的时候，需要进行lexical scope binding。
                keepEnvs((FuncType) tmp, visitor.getEnvStack());
            }
            local.update(nameString.val, tmp);
        }else{
            throw new RuntimeException("Parameter must be Symbol");
        }

        if(!(params.cdr instanceof CellNode)){
            throw new RuntimeException("params must be simple list");
        }

        if(args.cdr instanceof CellNode ){
            CellNode argsCdr = (CellNode) args.cdr;
            CellNode paramsCdr = (CellNode) params.cdr;
            if(!argsCdr.nil ) {
                evalParam(visitor, argsCdr, paramsCdr, local);
            }
        } else if (args.cdr instanceof SExprNode) {
            SExprNode argsCdr = (SExprNode) args.cdr;
            if (argsCdr.node instanceof CellNode) {
                CellNode argsCdrNode = (CellNode) argsCdr.node;
                CellNode paramsCdr = (CellNode) params.cdr;
                if (!argsCdrNode.nil) {
                    evalParam(visitor, argsCdrNode, paramsCdr, local);
                }
            } else if (argsCdr.node instanceof SymbolExprNode) {
                Type name = Util.getSymbolName(visitor, argsCdr);
                StringType nameString = (StringType) name;

                SExprNode param = new SExprNode();
                param.node = params.cdr;
                ExprType exprType = new ExprType();
                exprType.cellNode = new CellNode();
                exprType.cellNode.car = param;
                exprType.cellNode.cdr = CellNode.NIL;
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
    private void keepEnvs(FuncType func, Stack<Env> envs) {
        func.closureEnvs = new Stack<Env>();
        for (Env env : envs) {
            func.closureEnvs.add(env.getCopy());
        }
    }
}
