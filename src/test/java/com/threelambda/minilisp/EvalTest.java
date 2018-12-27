package com.threelambda.minilisp;

import com.threelambda.minilisp.core.Env;
import com.threelambda.minilisp.core.LispVisitor;
import com.threelambda.minilisp.node.Node;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.List;

/**
 * Created by ym on 5/31/2017.
 */
public class EvalTest {
    @Test
    public void test0() throws ParseException {
        String s = "1\n";
        String ret = call(s);
        Assert.assertEquals("", ret);
    }

    @Test
    public void test1() throws ParseException {
        String s = "(println 1)\n";
        String ret = call(s);
        Assert.assertEquals("1\n", ret);
    }

    @Test
    public void test2() throws ParseException {
        String s = "(println (define a 1))\n" +
                "(define a 2)\n"+
                "(define b 3)\n" +
                "(println a)\n" +
                "(println b)\n";
        String ret = call(s);
        Assert.assertEquals("1\n2\n3\n", ret);
    }

    @Test
    public void test3() throws ParseException {
        String s = "(println (+ 1 2))\n";
        String ret = call(s);
        Assert.assertEquals("3\n", ret);
    }

    @Test
    public void test4() throws ParseException {
        String s = "(println (- 1 2))\n";
        String ret = call(s);
        Assert.assertEquals("-1\n", ret);
    }

    @Test
    public void test5() throws ParseException {
        String s = "(println (+ (- 2 1) 3 4))\n";
        String ret = call(s);
        Assert.assertEquals("8\n", ret);
    }

    @Test
    public void test6() throws ParseException{
        String s = "(println ((lambda (x) x) 1))\n";
        String ret = call(s);
        Assert.assertEquals("1\n",ret);
    }

    @Test
    public void test7() throws ParseException{
        String s = "(define a (lambda (x) (+ x 1)))\n";
        s += "(println (a 2))\n";
        String ret = call(s);
        Assert.assertEquals("3\n",ret);
    }

    @Test
    public void test8() throws ParseException{
        String s = "(define a (lambda (x) x))\n";
        s += "(println (a 1))\n";
        String ret = call(s);
        Assert.assertEquals("1\n",ret);
    }

    @Test
    public void test9() throws ParseException{
        String s = "(define a (lambda (x) x))\n";
        String ret = call(s);
        Assert.assertEquals("",ret);
    }

    @Test
    public void test91() throws ParseException{
        String s = "(define put println)\n";
        s += "(put 1)\n";
        String ret = call(s);
        Assert.assertEquals("1\n",ret);
    }

    @Test
    public void test10() throws ParseException{
        String s = "(define a\n"
                + "  (lambda (x)\n"
                + "    (println x)\n"
                + "    ((lambda (x)\n"
                + "       (println (+ 1 x))\n"
                + "        x) 2)))\n"
                + "\n"
                + "(println (a 10))";

        String ret = call(s);
        Assert.assertEquals("10\n"
                + "3\n"
                + "2\n", ret);
    }

    @Test
    public void test11() throws ParseException{
        String s = "(defun a (x)\n"
                + "  (println x)\n"
                + "  )\n"
                + "(a 12)";
        String ret = call(s);
        Assert.assertEquals("12\n", ret);
    }

    @Test
    public void test12() throws ParseException{
        String s = "((defun a (x)\n"
                + "  (println x)\n"
                + "  ) 5)\n";
        String ret = call(s);
        Assert.assertEquals("5\n", ret);
    }

    @Test
    public void test121() throws ParseException{
        String s = "(define count 0)\n"
            + "(setq count (+ count 2))\n"
            + "(println count)";
        String ret = call(s);
        Assert.assertEquals("2\n", ret);
    }

    /**
     * 测试闭包。累加器。
     * @throws ParseException
     */
    @Test
    public void test13() throws ParseException{
        String s = "(define closure\n"
                + "  (lambda ()\n"
                + "    (define count 0)\n"
                + "    (lambda ()\n"
                + "      (setq count (+ count 1))\n"
                + "      (println count))))\n"

                + "(define counter (closure))\n"
                + "(counter)\n"
                + "(counter)\n";
        String ret = call(s);
        Assert.assertEquals("1\n"
                + "2\n", ret);
    }

    @Test
    public void test131() throws ParseException{
        String s = "(defun closure ()\n"
                + "    (define count 0)\n"
                + "    (lambda ()\n"
                + "      (setq count (+ count 1))\n"
                + "      (println count)))\n"

                + "(define counter (closure))\n"
                + "(counter)\n"
                + "(counter)\n";
        String ret = call(s);
        Assert.assertEquals("1\n"
                + "2\n", ret);
    }

    @Test
    public void test14() throws ParseException{
        String s = "(lambda ()\n"
                + "  (define count 1)\n"
                + "  (println count))";
        String ret = call(s);
        Assert.assertEquals("", ret);
    }


    @Test
    public void test15() throws ParseException{
        String s = "(defun call (f) ((lambda (var) (f)) 5))\n"
                + "(println ((lambda (var) (call (lambda () var))) 3) )";
        String ret = call(s);
        Assert.assertEquals("3\n", ret);
    }

    @Test
    public void test151() throws ParseException {
        String s = "(println (+ (quote 1) (quote 2)))\n";
        String ret = call(s);
        Assert.assertEquals("3\n",ret);
    }

    @Test
    public void test152() throws ParseException {
        String s = "(println (quote a))\n";
        String ret = call(s);
        Assert.assertEquals("a\n",ret);
    }

    @Test
    public void test153() throws ParseException {
        String s = "(println (quote (a b)))\n";
        String ret = call(s);
        Assert.assertEquals("(a b)\n",ret);
    }

    @Test
    public void test154() throws ParseException {
        String s = "(define a (quote (+ (- 2 1) 3)))\n"
            + "(println a)";
        String ret = call(s);
        Assert.assertEquals("(+ (- 2 1) 3)\n",ret);
    }
    @Test
    public void test155() throws ParseException {
        String s = "(println 'a)";
        String ret = call(s);
        Assert.assertEquals("a\n",ret);
    }

    @Test
    public void test16() throws ParseException {
        String s = "(println (+ '2 (quote 1)))\n";
        String ret = call(s);
        Assert.assertEquals("3\n",ret);
        s = "(println (- '2 (quote 1)))\n";
        ret = call(s);
        Assert.assertEquals("1\n",ret);
    }

    @Test
    public void test17() throws ParseException {
        String s = "(define a 'a)\n"
            + "(println a)";
        String ret = call(s);
        Assert.assertEquals("a\n",ret);
        s = "(define a '(+ (- 2 1) 3) )\n"
            + "(println a)";
        ret = call(s);
        Assert.assertEquals("(+ (- 2 1) 3)\n",ret);
    }

    @Test
    public void test181() throws ParseException {
        String s = "(println (cons '1 '(2 3 4 5)))\n";
        String ret = call(s);
        Assert.assertEquals("(1 2 3 4 5)\n", ret);
        s = "(println (cons '(1) '(2 3 4 5)))\n";
        ret = call(s);
        Assert.assertEquals("((1) 2 3 4 5)\n", ret);
    }

    @Test
    public void test182() throws ParseException {
        String s = "(println (cons '1 '2))\n";
        String ret = call(s);
        Assert.assertEquals("(1 . 2)\n", ret);
    }

    @Test
    public void test183() throws ParseException {
        String s = "(println (cons '1 '2))\n";
        String ret = call(s);
        Assert.assertEquals("(1 . 2)\n", ret);
    }

    @Test
    public void test184() throws ParseException {
        String s = "(println (cons 1 2))\n";
        String ret = call(s);
        Assert.assertEquals("(1 . 2)\n", ret);
    }

    @Test
    public void test185() throws ParseException {
        String s = "(println (cons '(2 3) '1 ))\n";
        String ret = call(s);
        Assert.assertEquals("((2 3) . 1)\n", ret);
        s = "(println (cons '(2 3) 1 ))\n";
        ret = call(s);
        Assert.assertEquals("((2 3) . 1)\n", ret);
        s = "(println (cons (+ 2 3) 1 ))\n";
        ret = call(s);
        Assert.assertEquals("(5 . 1)\n", ret);
    }

    @Test
    public void test186() throws ParseException {
        String s = "(println (cons 1 ()))\n";
        String ret = call(s);
        Assert.assertEquals("(1)\n", ret);
    }

    @Test
    public void test187() throws ParseException {
        String s = "(println (cons 1 '()))\n";
        String ret = call(s);
        Assert.assertEquals("(1)\n", ret);
    }

    @Test
    public void test188() throws ParseException {
        String s = "(println (cons 1 '(2)))\n";
        String ret = call(s);
        Assert.assertEquals("(1 2)\n", ret);
    }

    @Test
    public void test190() throws ParseException {
        String s = "(defun list (x y) \n"
            + "       (println x)\n"
            + "       (println y))\n"
            + "      (list 1 2)\n";
        String ret = call(s);
        Assert.assertEquals("1\n2\n",ret);
    }

    @Test
    public void test1901() throws ParseException {
        String s = "(defun list (x .(y z)) \n"
            + "       (println x)\n"
            + "       (println y)"
            + "       (println z))\n"
            + "      (list 1 2 3)\n";
        String ret = call(s);
        Assert.assertEquals("1\n2\n3\n",ret);
    }

    @Test
    public void test1902() throws ParseException {
        String s = "(defun list (x  y . z ) \n"
            + "       (println x)\n"
            + "       (println y)"
            + "       (println z))\n"
            + "      (list 1 2 3)\n";
        String ret = call(s);
        Assert.assertEquals("1\n2\n(3)\n",ret);
    }

    @Test
    public void test1903() throws ParseException {
        String s = "(defun list (x  y . (z) ) \n"
            + "       (println x)\n"
            + "       (println y)"
            + "       (println z))\n"
            + "      (list 1 2 3)\n";
        String ret = call(s);
        Assert.assertEquals("1\n2\n3\n",ret);
    }

    @Test
    public void test1904() throws ParseException {
        String s = "(defun list (x . ( y . (z) )) \n"
            + "       (println x)\n"
            + "       (println y)"
            + "       (println z))\n"
            + "      (list 1 2 3)\n";
        String ret = call(s);
        Assert.assertEquals("1\n2\n3\n",ret);
    }

    @Test
    public void test191() throws ParseException {
        String s = "(defun list (x . y) \n"
            + "       (println x)\n"
            + "       (println y))\n"
            + "      (list 1 2)\n";
        String ret = call(s);
        Assert.assertEquals("1\n(2)\n",ret);
    }

    @Test
    public void test192() throws ParseException {
        String s = "(defun list (x . y) \n" +
            "(cons x y))\n" +
            "(println (list 1 2))";
        String ret = call(s);
        Assert.assertEquals("(1 2)\n",ret);
    }

    /**
     * 条件判断
     * @throws ParseException
     */
    @Test
    public void test201() throws ParseException {
        String s = "(println (if 1 'a)) \n" ;
        String ret = call(s);
        Assert.assertEquals("a\n",ret);
    }

    @Test
    public void test202() throws ParseException {
        String s = "(println (if () 'a 'b)) \n" ;
        String ret = call(s);
        Assert.assertEquals("b\n",ret);
    }

    @Test
    public void test203() throws ParseException {
        String s = "(println (if () 'a 'b 'c)) \n" ;
        String ret = call(s);
        Assert.assertEquals("c\n",ret);
    }

    @Test
    public void test204() throws ParseException {
        String s = "(println (if 1 () 'b)) \n" ;
        String ret = call(s);
        Assert.assertEquals("()\n",ret);
    }

    @Test
    public void test205() throws ParseException {
        String s = "(println (if () () 'b)) \n" ;
        String ret = call(s);
        Assert.assertEquals("b\n",ret);
    }


    @Test
    public void test210() throws ParseException {
        String s = "(println (= 1 1)) \n" ;
        String ret = call(s);
        Assert.assertEquals("t\n",ret);
    }

    @Test
    public void test211() throws ParseException {
        String s = "(println (= 1 2)) \n" ;
        String ret = call(s);
        Assert.assertEquals("()\n",ret);
    }

    @Test
    public void test212() throws ParseException {
        String s = "(println (if (= 1 1) 'a 'b )) \n" ;
        String ret = call(s);
        Assert.assertEquals("a\n",ret);
        s = "(println (if (= 1 2) 'a 'b )) \n" ;
        ret = call(s);
        Assert.assertEquals("b\n",ret);
    }

    @Test
    public void test213() throws ParseException {
        String s = "(println (if (< 1 2) 'a 'b )) \n" ;
        String ret = call(s);
        Assert.assertEquals("a\n",ret);
        s = "(println (if (< 1 1) 'a 'b )) \n" ;
        ret = call(s);
        Assert.assertEquals("b\n",ret);
    }

    @Test
    public void test214() throws ParseException {
        String s = "(println (if (> 2 1) 'a 'b )) \n" ;
        String ret = call(s);
        Assert.assertEquals("a\n",ret);
        s = "(println (if (> 2 2) 'a 'b )) \n" ;
        ret = call(s);
        Assert.assertEquals("b\n",ret);
    }

    @Test
    public void test215() throws ParseException {
        String s = "(println (if (<= 2 2) 'a 'b )) \n" ;
        String ret = call(s);
        Assert.assertEquals("a\n",ret);
        s = "(println (if (<= 2 1) 'a 'b )) \n" ;
        ret = call(s);
        Assert.assertEquals("b\n",ret);
    }

    @Test
    public void test216() throws ParseException {
        String s = "(println (if (>= 2 2) 'a 'b )) \n" ;
        String ret = call(s);
        Assert.assertEquals("a\n",ret);
        s = "(println (if (>= 1 2) 'a 'b )) \n" ;
        ret = call(s);
        Assert.assertEquals("b\n",ret);
    }

    /**
     * 默认支持递归
     * @throws ParseException
     */
    @Test
    public void test220() throws ParseException {
        String s = ";;recursive func \n" +
                "(defun fab (x)                             \n" +
                "   (if (<= x 2)                            \n" +
                "       1                                   \n" +
                "       (+ (fab (- x 1))                    \n" +
                "          (fab (- x 2)))))                 \n" +
                "(println (fab 7))    ;; -> 13              \n" +
                "(println (fab 20))   ;; -> 6765            \n" ;
        String ret = call(s);
        Assert.assertEquals("13\n6765\n",ret);
    }

    @Test
    public void test230() throws ParseException {
        String s = "\n" +
            "(defun seven () 7)                         \n" +
            "(println ((lambda () (seven))) )           \n" ;
        String ret = call(s);
        Assert.assertEquals("7\n",ret);
    }


    @Test
    public void test235() throws ParseException {
        String s = "\n"
            + "(defmacro fool (x)   \n"
            + "   (+ x 1))   \n"
            + "(println (macroexpand '(fool 2)))  ;; -> 3  \n"  ;
        String ret = call(s);
        Assert.assertEquals("3\n", ret);
    }

    @Test
    public void test236() throws ParseException {
        String s = "\n"
            + "(println (macroexpand '(+ 1 2)))  ;; -> (+ 1 2)  \n"  ;
        String ret = call(s);
        Assert.assertEquals("(+ 1 2)\n", ret);
    }

    @Test
    public void test237() throws ParseException {
        String s = "\n"
            + "(println (macroexpand 'a))  ;; -> a  \n"  ;
        String ret = call(s);
        Assert.assertEquals("a\n", ret);
    }


    @Test
    public void test238() throws ParseException {
        String s = "\n"
            + "(println (macroexpand (+ 1 2)))  ;; -> 3  \n"  ;
        String ret = call(s);
        Assert.assertEquals("3\n", ret);
    }

    @Test
    public void test239() throws ParseException {
        String s = "\n" +
                "(defmacro seven () 7) \n"
                + "(println (macroexpand '(seven) ))  ;; -> 7 \n"  ;
        String ret = call(s);
        Assert.assertEquals("7\n", ret);
    }

    @Test
    public void test240() throws ParseException {
        String s = "\n" +
                "(defun list (x . y)  (cons x y)) \n"
                + "(println (macroexpand '(list '+ 1 2) ))  ;; -> (list '+ 1 2)\n"  ;
        String ret = call(s);
        Assert.assertEquals("(list '+ 1 2)\n", ret);
    }

    @Test
    public void test241() throws ParseException {
        String s = "\n" +
                "(defun list (x . y)  (cons x y)) \n" +
                "(defmacro add (x) (list '+ 1 x)) \n"
                + "(println (macroexpand '(add 2) ))  ;; \n"  ;
        String ret = call(s);
        Assert.assertEquals("(+ 1 2)\n", ret);
    }

    @Test
    public void test242() throws ParseException {
        String s = "\n" +
                "(defun list (x . y)  (cons x y)) \n" +
                "(defmacro add (x) (list '+ 1 x)) \n"
                + "(println (add 2))  ;; \n"  ;
        String ret = call(s);
        Assert.assertEquals("3\n", ret);
    }

    @Test
    public void test244() throws ParseException {
        String s = "\n" +
                "(defun list (x . y)  (cons x y)) \n" +
                "(defmacro add (x) (list '+ 1 x)) \n"
                + "(println (macroexpand '(add (+ 2 3) )))  ;; \n"  ;
        String ret = call(s);
        Assert.assertEquals("(+ 1 (+ 2 3))\n", ret);
    }

    @Test
    public void test245() throws ParseException {
        String s = "(defun list (x . y) (cons x y))\n" +
                "  (defmacro if-zero (x then) (list 'if (list '= x 0) then))\n" +
                "  (println (macroexpand '(if-zero x (print x))) )" +
                " \n"  ;
        String ret = call(s);
        Assert.assertEquals("(if (= x 0) (print x))\n", ret);
    }

    @Test
    public void test246() throws ParseException {
        String s = "(defun list (x . y) (cons x y))\n" +
                "  (defmacro if-zero (x then) (list 'if (list '= x 0) then))\n" +
                "  (println (if-zero 0 42))" +
                " \n"  ;
        String ret = call(s);
        Assert.assertEquals("42\n", ret);
    }

    @Test
    public void test250() throws ParseException {
        String s = "\n" +
                "(define a (cons 1 2)) \n"
                + "(println (car a))  ;; \n"  ;
        String ret = call(s);
        Assert.assertEquals("1\n", ret);
    }

    @Test
    public void test260() throws ParseException {
        String s = "\n" +
                "(define a (cons 1 2)) \n"
                + "(println (cdr a))  ;; \n"  ;
        String ret = call(s);
        Assert.assertEquals("2\n", ret);
    }

    @Test
    public void test261() throws ParseException {
        String s = "\n" +
                "(define a (cons 1 '(2))) \n"
                + "(println (cdr a))  ;; \n"  ;
        String ret = call(s);
        Assert.assertEquals("(2)\n", ret);
    }

    @Test
    public void test270() throws ParseException {
        String s = "\n" +
                "(define a (cons 1 2)) \n"
                + "(setcar a '3)  \n"
                + "(println a)  ;; \n"  ;
        String ret = call(s);
        Assert.assertEquals("(3 . 2)\n", ret);
    }

    @Test
    public void test280() throws ParseException {
        String s = "\n" +
                "(println ((lambda () t)) ) \n"  ;
        String ret = call(s);
        Assert.assertEquals("t\n", ret);
    }

    @Test
    public void test290() throws ParseException {
        String s = "\n" +
                "(println (eq 'a 'a) ) \n"  ;
        String ret = call(s);
        Assert.assertEquals("t\n", ret);
    }

    @Test
    public void test291() throws ParseException {
        String s = "\n" +
                "(println (eq 'a 'b) ) \n"  ;
        String ret = call(s);
        Assert.assertEquals("()\n", ret);
    }

    @Test
    public void test300() throws ParseException {
        String s = "(define i 0)\n" +
                "(while (< i 10) \n" +
                " (println i) \n" +
                " (setq i (+ i 1)) ) \n" +
                " \n"  ;
        String ret = call(s);
        Assert.assertEquals("0\n1\n2\n3\n4\n5\n6\n7\n8\n9\n", ret);
    }

    @Test
    public void test() throws ParseException {
        String s = ";;;\n" +
                ";;; N-queens puzzle solver.\n" +
                ";;;\n" +
                ";;; The N queens puzzle is the problem of placing N chess queens on an N x N\n" +
                ";;; chessboard so that no two queens attack each\n" +
                ";;; other. http://en.wikipedia.org/wiki/Eight_queens_puzzle\n" +
                ";;;\n" +
                ";;; This program solves N-queens puzzle by depth-first backtracking.\n" +
                ";;;\n" +
                "\n" +
                ";;;\n" +
                ";;; Basic macros\n" +
                ";;;\n" +
                ";;; Because the language does not have quasiquote, we need to construct an\n" +
                ";;; expanded form using cons and list.\n" +
                ";;;\n" +
                "\n" +
                ";; (progn expr ...)\n" +
                ";; => ((lambda () expr ...))\n" +
                "(defmacro progn (expr . rest)\n" +
                "  (list (cons 'lambda (cons () (cons expr rest)))))\n" +
                "\n" +
                "(defun list (x . y)\n" +
                "  (cons x y))\n" +
                "\n" +
                "(defun not (x)\n" +
                "  (if x () t))\n" +
                "\n" +
                ";; (let1 var val body ...)\n" +
                ";; => ((lambda (var) body ...) val)\n" +
                "(defmacro let1 (var val . body)\n" +
                "  (cons (cons 'lambda (cons (list var) body))\n" +
                "\t(list val)))\n" +
                "\n" +
                ";; (and e1 e2 ...)\n" +
                ";; => (if e1 (and e2 ...))\n" +
                ";; (and e1)\n" +
                ";; => e1\n" +
                "(defmacro and (expr . rest)\n" +
                "  (if rest\n" +
                "      (list 'if expr (cons 'and rest))\n" +
                "    expr))\n" +
                "\n" +
                ";; (or e1 e2 ...)\n" +
                ";; => (let1 <tmp> e1\n" +
                ";;      (if <tmp> <tmp> (or e2 ...)))\n" +
                ";; (or e1)\n" +
                ";; => e1\n" +
                ";;\n" +
                ";; The reason to use the temporary variables is to avoid evaluating the\n" +
                ";; arguments more than once.\n" +
                ";;(defmacro or (expr . rest)\n" +
                ";;  (if rest\n" +
                ";;      (let1 var (gensym)\n" +
                ";;\t    (list 'let1 var expr\n" +
                ";;\t\t  (list 'if var var (cons 'or rest))))\n" +
                " ;;   expr))\n" +
                "(defmacro or (expr . rest)\n" +
                "  (if rest\n" +
                "      (let1 var expr\n" +
                "\t\t  (list 'if var var (cons 'or rest)))\n" +
                "    expr))\n" +
                "\n" +
                ";; (when expr body ...)\n" +
                ";; => (if expr (progn body ...))\n" +
                "(defmacro when (expr . body)\n" +
                "  (cons 'if (cons expr (list (cons 'progn body)))))\n" +
                "\n" +
                ";; (unless expr body ...)\n" +
                ";; => (if expr () body ...)\n" +
                "(defmacro unless (expr . body)\n" +
                "  (cons 'if (cons expr (cons () body))))\n" +
                "\n" +
                ";;;\n" +
                ";;; Numeric operators\n" +
                ";;;\n" +
                "\n" +
                "\n" +
                ";;;\n" +
                ";;; List operators\n" +
                ";;;\n" +
                "\n" +
                ";; Applies each element of lis to pred. If pred returns a true value, terminate\n" +
                ";; the evaluation and returns pred's return value. If all of them return (),\n" +
                ";; returns ().\n" +
                "(defun any (lis pred)\n" +
                "  (when lis\n" +
                "    (or (pred (car lis))\n" +
                "\t(any (cdr lis) pred))))\n" +
                "\n" +
                ";;; Applies each element of lis to fn, and returns their return values as a list.\n" +
                "(defun map (lis fn)\n" +
                "  (when lis\n" +
                "    (cons (fn (car lis))\n" +
                "\t  (map (cdr lis) fn))))\n" +
                "\n" +
                ";; Returns nth element of lis.\n" +
                "(defun nth (lis n)\n" +
                "  (if (= n 0)\n" +
                "      (car lis)\n" +
                "    (nth (cdr lis) (- n 1))))\n" +
                "\n" +
                ";; Returns the nth tail of lis.\n" +
                "(defun nth-tail (lis n)\n" +
                "  (if (= n 0)\n" +
                "      lis\n" +
                "    (nth-tail (cdr lis) (- n 1))))\n" +
                "\n" +
                ";; Returns a list consists of m .. n-1 integers.\n" +
                "(defun %iota (m n)\n" +
                "  (unless (<= n m)\n" +
                "    (cons m (%iota (+ m 1) n))))\n" +
                "\n" +
                ";; Returns a list consists of 0 ... n-1 integers.\n" +
                "(defun iota (n)\n" +
                "  (%iota 0 n))\n" +
                "\n" +
                ";; Returns a new list whose length is len and all members are init.\n" +
                "(defun make-list (len init)\n" +
                "  (unless (= len 0)\n" +
                "    (cons init (make-list (- len 1) init))))\n" +
                "\n" +
                ";; Applies fn to each element of lis.\n" +
                "(defun for-each (lis fn)\n" +
                "  (or (not lis)\n" +
                "      (progn (fn (car lis))\n" +
                "\t     (for-each (cdr lis) fn))))\n" +
                "\n" +
                ";;;\n" +
                ";;; N-queens solver\n" +
                ";;;\n" +
                "\n" +
                ";; Creates size x size list filled with symbol \"x\".\n" +
                "(defun make-board (size)\n" +
                "  (map (iota size)\n" +
                "       (lambda (_)\n" +
                "\t (make-list size 'x))))\n" +
                "\n" +
                ";; Returns location (x, y)'s element.\n" +
                "(defun get (board x y)\n" +
                "  (nth (nth board x) y))\n" +
                ";; Set symbol \"@\" to location (x, y).\n" +
                "(defun set (board x y)\n" +
                "  (setcar (nth-tail (nth board x) y) '@))\n" +
                "\n" +
                "" +
                ";; Set symbol \"x\" to location (x, y).\n" +
                "(defun clear (board x y)\n" +
                "  (setcar (nth-tail (nth board x) y) 'x))\n" +
                "\n" +
                ";; Returns true if location (x, y)'s value is \"@\".\n" +
                "(defun set? (board x y)\n" +
                "  (eq (get board x y) '@))\n" +
                "\n" +
                ";; Print out the given board.\n" +
                "(defun print (board)\n" +
                "  (if (not board)\n" +
                "      '$\n" +
                "    (println (car board))\n" +
                "    (print (cdr board))))\n" +
                "\n" +
                ";; Returns true if we cannot place a queen at position (x, y), assuming that\n" +
                ";; queens have already been placed on each row from 0 to x-1.\n" +
                "(defun conflict? (board x y)\n" +
                "  (any (iota x)\n" +
                "       (lambda (n)\n" +
                "\t (or\n" +
                "\t  ;; Check if there's no conflicting queen upward\n" +
                "\t  (set? board n y)\n" +
                "\t  ;; Upper left\n" +
                "\t  (let1 z (+ y (- n x))\n" +
                "\t\t(and (<= 0 z)\n" +
                "\t\t     (set? board n z)))\n" +
                "\t  ;; Upper right\n" +
                "\t  (let1 z (+ y (- x n))\n" +
                "\t\t(and (< z board-size)\n" +
                "\t\t     (set? board n z)))))))\n" +
                "(defun %solve (board x)\n" +
                "  (if (= x board-size)\n" +
                "      ;; Problem solved\n" +
                "      (progn (print board)\n" +
                "\t     (println '$))\n" +
                "    (for-each (iota board-size)\n" +
                "\t      (lambda (y)\n" +
                "\t\t(unless (conflict? board x y)\n" +
                "\t\t  (set board x y)\n" +
                "\t\t  (%solve board (+ x 1))\n" +
                "\t\t  (clear board x y))))))\n" +
                "\n" +
                "(defun solve (board)\n" +
                "  (println 'start)\n" +
                "  (%solve board 0)\n" +
                "  (println 'done))\n" +
                "\n" +
                ";;;\n" +
                ";;; Main\n" +
                ";;;\n" +
                "(define board-size 8)\n" +
                "(define board (make-board board-size))\n" +
                "(print board)\n" +
                ";;(println 'a) \n" +
                ";;(set board 0 1)\n" +
                "(solve board)\n" +
                ";;(println (conflict? board 0 0))\n"
                ;
        String ret = call(s);
    }

    private String call(String s) throws ParseException {
        System.out.println(s);
        String ret = "";
        MiniLisp parser = new MiniLisp(new StringReader(s));
        List<Node> result = parser.Input();
        PrintStream old = System.out;
        ByteArrayOutputStream baos = redirect();
        LispVisitor visitor = new LispVisitor((new Env()).initPrimitiveFunc());

        for (Node node : result) {
            visitor.visit(node);
        }
        redirectBack(old);
        System.out.println(baos.toString());
        return baos.toString();
    }

    private void redirectBack(PrintStream old) {
        System.setOut(old);
    }


    private ByteArrayOutputStream redirect(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        // IMPORTANT: Save the old System.out!
        PrintStream old = System.out;
        // Tell Java to use your special stream
        System.setOut(ps);
        // Print some output: goes to your special stream
        return baos;
    }
}
