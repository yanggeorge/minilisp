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
    public void test() throws ParseException {
        String s = "\n" +
                "(println ((lambda () t)) ) \n"  ;
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
