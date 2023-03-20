package com.threelambda.minilisp;

import java.io.StringReader;
import java.util.List;

import com.threelambda.minilisp.node.Node;
import org.junit.Test;

public class ParserTest {

    @Test
    public void test0() throws ParseException {
        String s = "1";
        call(s);
        s = "a";
        call(s);
        s = "22 aa";
        call(s);
    }

    @Test
    public void test01() throws ParseException {
        String s = "()";
        call(s);
    }

    @Test
    public void test1() throws ParseException {
        String s = "((a.b).(b.(c)))";
        call(s);
    }

    @Test
    public void test2() throws ParseException {
        String s = "(a b c)";
        call(s);
        s = "(a b . c)";
        call(s);
    }

    @Test
    public void test3() throws ParseException {
        String s = "(a .(b c))";
        call(s);
    }

    @Test
    public void test4() throws ParseException {
        String s = " ( ( a b ) . ( b c ) ) ";
        call(s);
    }

    @Test
    public void test5() throws ParseException {
        String s = "( a ( a b )  ( b c ) ) ";
        call(s);
    }

    @Test
    public void test6() throws ParseException {
        String s = "(define a 1) (define b -1) ";
        call(s);
    }

    @Test
    public void test7() throws ParseException {
        String s = " (+ 1 -2 a b (- 1 2)) ";
        call(s);
    }

    @Test
    public void test8() throws ParseException {
        String s = " (defun next (board x y)\n"
                + "  (let c (count board x y)\n"
                + "       (if (alive? board x y)\n"
                + "           (or (= c 2) (= c 3))\n"
                + "         (= c 3))))";
        System.out.println(s);
        call(s);
    }

    @Test
    public void test9() throws ParseException {
        String s = "(defun count (board x y)\n"
                + "  (let at (lambda (x y)\n"
                + "            (if (alive? board x y) 1 0))\n"
                + "       (+ (at (- x 1) (- y 1))\n"
                + "          (at (- x 1) y)\n"
                + "          (at (- x 1) (+ y 1))\n"
                + "          (at x (- y 1))\n"
                + "          (at x (+ y 1))\n"
                + "          (at (+ x 1) (- y 1))\n"
                + "          (at (+ x 1) y)\n"
                + "          (at (+ x 1) (+ y 1)))))";
        System.out.println(s);
        call(s);
    }

    @Test
    public void test10() throws ParseException {
        String s = " '(+ 1 2) ";
        call(s);
    }

    @Test
    public void test11() throws ParseException {
        String s = "(cons '1 '2)";
        call(s);
    }

    @Test
    public void test12() throws ParseException {
        String s = "(defmacro progn (expr . rest)\n"
                + "  (list (cons 'lambda (cons () (cons expr rest)))))";
        call(s);
    }

    @Test
    public void test13() throws ParseException {
        String s = ";;fadfsdfasdfasdfasdfa\n"
                + "(cons '1 '2)";
        call(s);
    }

    @Test
    public void test131() throws ParseException {
        String s = "(cons ;;fadfsdfasdfasdfasdfa\n"
                + "  '1 '2)";
        call(s);
    }

    @Test
    public void test132() throws ParseException {
        String s = ";;fadfsdfasdfasdfasdfa\n"
                + "  \n"
                + ";;fadfsdfasdfasdfasdf";

        call(s);
    }

    @Test
    public void test133() throws ParseException {
        String s = "(list ;;fadfsdfasdfasdfasdfa\n"
                + "  a  ;;fadfsdfasdfasdfasdfa\n "
                + "b)";
        call(s);
    }

    @Test
    public void test14() throws ParseException {
        String s = ";;;\n"
                + ";;; Conway's game of life\n"
                + ";;;\n"
                + "\n"
                + ";; (progn expr ...)\n"
                + ";; => ((lambda () expr ...))\n"
                + "(defmacro progn (expr . rest)\n"
                + "  (list (cons 'lambda (cons () (cons expr rest)))))\n"
                + "\n"
                + "(defun list (x . y)\n"
                + "  (cons x y))\n"
                + "\n"
                + "(defun not (x)\n"
                + "  (if x () t))\n"
                + "\n"
                + ";; (let var val body ...)\n"
                + ";; => ((lambda (var) body ...) val)\n"
                + "(defmacro let (var val . body)\n"
                + "  (cons (cons 'lambda (cons (list var) body))\n"
                + "\t(list val)))\n"
                + "\n"
                + ";; (and e1 e2 ...)\n"
                + ";; => (if e1 (and e2 ...))\n"
                + ";; (and e1)\n"
                + ";; => e1\n"
                + "(defmacro and (expr . rest)\n"
                + "  (if rest\n"
                + "      (list 'if expr (cons 'and rest))\n"
                + "    expr))\n"
                + "\n"
                + ";; (or e1 e2 ...)\n"
                + ";; => (let <tmp> e1\n"
                + ";;      (if <tmp> <tmp> (or e2 ...)))\n"
                + ";; (or e1)\n"
                + ";; => e1\n"
                + ";;\n"
                + ";; The reason to use the temporary variables is to avoid evaluating the\n"
                + ";; arguments more than once.\n"
                + "(defmacro or (expr . rest)\n"
                + "  (if rest\n"
                + "      (let var (gensym)\n"
                + "           (list 'let var expr\n"
                + "                 (list 'if var var (cons 'or rest))))\n"
                + "    expr))\n"
                + "\n"
                + ";; (when expr body ...)\n"
                + ";; => (if expr (progn body ...))\n"
                + "(defmacro when (expr . body)\n"
                + "  (cons 'if (cons expr (list (cons 'progn body)))))\n"
                + "\n"
                + ";; (unless expr body ...)\n"
                + ";; => (if expr () body ...)\n"
                + "(defmacro unless (expr . body)\n"
                + "  (cons 'if (cons expr (cons () body))))\n"
                + "\n"
                + ";;;\n"
                + ";;; Numeric operators\n"
                + ";;;\n"
                + "\n"
                + "(defun <= (e1 e2)\n"
                + "  (or (< e1 e2)\n"
                + "      (= e1 e2)))\n"
                + "\n"
                + ";;;\n"
                + ";;; List operators\n"
                + ";;;\n"
                + "\n"
                + ";;; Applies each element of lis to fn, and returns their return values as a list.\n"
                + "(defun map (lis fn)\n"
                + "  (when lis\n"
                + "    (cons (fn (car lis))\n"
                + "\t  (map (cdr lis) fn))))\n"
                + "\n"
                + ";; Returns nth element of lis.\n"
                + "(defun nth (lis n)\n"
                + "  (if (= n 0)\n"
                + "      (car lis)\n"
                + "    (nth (cdr lis) (- n 1))))\n"
                + "\n"
                + ";; Returns the nth tail of lis.\n"
                + "(defun nth-tail (lis n)\n"
                + "  (if (= n 0)\n"
                + "      lis\n"
                + "    (nth-tail (cdr lis) (- n 1))))\n"
                + "\n"
                + ";; Returns a list consists of m .. n-1 integers.\n"
                + "(defun %iota (m n)\n"
                + "  (unless (<= n m)\n"
                + "    (cons m (%iota (+ m 1) n))))\n"
                + "\n"
                + ";; Returns a list consists of 0 ... n-1 integers.\n"
                + "(defun iota (n)\n"
                + "  (%iota 0 n))\n"
                + "\n"
                + ";;;\n"
                + ";;; Main\n"
                + ";;;\n"
                + "\n"
                + "(define width 10)\n"
                + "(define height 10)\n"
                + "\n"
                + ";; Returns location (x, y)'s element.\n"
                + "(defun get (board x y)\n"
                + "  (nth (nth board y) x))\n"
                + "\n"
                + ";; Returns true if location (x, y)'s value is \"@\".\n"
                + "(defun alive? (board x y)\n"
                + "  (and (<= 0 x)\n"
                + "       (< x height)\n"
                + "       (<= 0 y)\n"
                + "       (< y width)\n"
                + "       (eq (get board x y) '@)))\n"
                + "\n"
                + ";; Print out the given board.\n"
                + "(defun print (board)\n"
                + "  (if (not board)\n"
                + "      '$\n"
                + "    (println (car board))\n"
                + "    (print (cdr board))))\n"
                + "\n"
                + "(defun count (board x y)\n"
                + "  (let at (lambda (x y)\n"
                + "            (if (alive? board x y) 1 0))\n"
                + "       (+ (at (- x 1) (- y 1))\n"
                + "          (at (- x 1) y)\n"
                + "          (at (- x 1) (+ y 1))\n"
                + "          (at x (- y 1))\n"
                + "          (at x (+ y 1))\n"
                + "          (at (+ x 1) (- y 1))\n"
                + "          (at (+ x 1) y)\n"
                + "          (at (+ x 1) (+ y 1)))))\n"
                + "\n"
                + "(defun next (board x y)\n"
                + "  (let c (count board x y)\n"
                + "       (if (alive? board x y)\n"
                + "           (or (= c 2) (= c 3))\n"
                + "         (= c 3))))\n"
                + "\n"
                + "(defun run (board)\n"
                + "  (while t\n"
                + "    (print board)\n"
                + "    (println '*)\n"
                + "    (let newboard (map (iota height)\n"
                + "                       (lambda (y)\n"
                + "                         (map (iota width)\n"
                + "                              (lambda (x)\n"
                + "                                (if (next board x y) '@ '_)))))\n"
                + "         (setq board newboard))))\n"
                + "\n"
                + "(run '((_ _ _ _ _ _ _ _ _ _)\n"
                + "       (_ _ _ _ _ _ _ _ _ _)\n"
                + "       (_ _ _ _ _ _ _ _ _ _)\n"
                + "       (_ _ _ _ _ _ _ _ _ _)\n"
                + "       (_ _ _ _ _ _ _ _ _ _)\n"
                + "       (_ _ _ _ _ _ _ _ _ _)\n"
                + "       (_ _ _ _ _ _ _ _ _ _)\n"
                + "       (_ @ @ @ _ _ _ _ _ _)\n"
                + "       (_ _ _ @ _ _ _ _ _ _)\n"
                + "       (_ _ @ _ _ _ _ _ _ _)))\n";
        call(s);
    }

    @Test
    public void test15() throws ParseException {
        String s = ";;;\n"
                + ";;; N-queens puzzle solver.\n"
                + ";;;\n"
                + ";;; The N queens puzzle is the problem of placing N chess queens on an N x N\n"
                + ";;; chessboard so that no two queens attack each\n"
                + ";;; other. http://en.wikipedia.org/wiki/Eight_queens_puzzle\n"
                + ";;;\n"
                + ";;; This program solves N-queens puzzle by depth-first backtracking.\n"
                + ";;;\n"
                + "\n"
                + ";;;\n"
                + ";;; Basic macros\n"
                + ";;;\n"
                + ";;; Because the language does not have quasiquote, we need to construct an\n"
                + ";;; expanded form using cons and list.\n"
                + ";;;\n"
                + "\n"
                + ";; (progn expr ...)\n"
                + ";; => ((lambda () expr ...))\n"
                + "(defmacro progn (expr . rest)\n"
                + "  (list (cons 'lambda (cons () (cons expr rest)))))\n"
                + "\n"
                + "(defun list (x . y)\n"
                + "  (cons x y))\n"
                + "\n"
                + "(defun not (x)\n"
                + "  (if x () t))\n"
                + "\n"
                + ";; (let1 var val body ...)\n"
                + ";; => ((lambda (var) body ...) val)\n"
                + "(defmacro let1 (var val . body)\n"
                + "  (cons (cons 'lambda (cons (list var) body))\n"
                + "\t(list val)))\n"
                + "\n"
                + ";; (and e1 e2 ...)\n"
                + ";; => (if e1 (and e2 ...))\n"
                + ";; (and e1)\n"
                + ";; => e1\n"
                + "(defmacro and (expr . rest)\n"
                + "  (if rest\n"
                + "      (list 'if expr (cons 'and rest))\n"
                + "    expr))\n"
                + "\n"
                + ";; (or e1 e2 ...)\n"
                + ";; => (let1 <tmp> e1\n"
                + ";;      (if <tmp> <tmp> (or e2 ...)))\n"
                + ";; (or e1)\n"
                + ";; => e1\n"
                + ";;\n"
                + ";; The reason to use the temporary variables is to avoid evaluating the\n"
                + ";; arguments more than once.\n"
                + "(defmacro or (expr . rest)\n"
                + "  (if rest\n"
                + "      (let1 var (gensym)\n"
                + "\t    (list 'let1 var expr\n"
                + "\t\t  (list 'if var var (cons 'or rest))))\n"
                + "    expr))\n"
                + "\n"
                + ";; (when expr body ...)\n"
                + ";; => (if expr (progn body ...))\n"
                + "(defmacro when (expr . body)\n"
                + "  (cons 'if (cons expr (list (cons 'progn body)))))\n"
                + "\n"
                + ";; (unless expr body ...)\n"
                + ";; => (if expr () body ...)\n"
                + "(defmacro unless (expr . body)\n"
                + "  (cons 'if (cons expr (cons () body))))\n"
                + "\n"
                + ";;;\n"
                + ";;; Numeric operators\n"
                + ";;;\n"
                + "\n"
                + "(defun <= (e1 e2)\n"
                + "  (or (< e1 e2)\n"
                + "      (= e1 e2)))\n"
                + "\n"
                + ";;;\n"
                + ";;; List operators\n"
                + ";;;\n"
                + "\n"
                + ";; Applies each element of lis to pred. If pred returns a true value, terminate\n"
                + ";; the evaluation and returns pred's return value. If all of them return (),\n"
                + ";; returns ().\n"
                + "(defun any (lis pred)\n"
                + "  (when lis\n"
                + "    (or (pred (car lis))\n"
                + "\t(any (cdr lis) pred))))\n"
                + "\n"
                + ";;; Applies each element of lis to fn, and returns their return values as a list.\n"
                + "(defun map (lis fn)\n"
                + "  (when lis\n"
                + "    (cons (fn (car lis))\n"
                + "\t  (map (cdr lis) fn))))\n"
                + "\n"
                + ";; Returns nth element of lis.\n"
                + "(defun nth (lis n)\n"
                + "  (if (= n 0)\n"
                + "      (car lis)\n"
                + "    (nth (cdr lis) (- n 1))))\n"
                + "\n"
                + ";; Returns the nth tail of lis.\n"
                + "(defun nth-tail (lis n)\n"
                + "  (if (= n 0)\n"
                + "      lis\n"
                + "    (nth-tail (cdr lis) (- n 1))))\n"
                + "\n"
                + ";; Returns a list consists of m .. n-1 integers.\n"
                + "(defun %iota (m n)\n"
                + "  (unless (<= n m)\n"
                + "    (cons m (%iota (+ m 1) n))))\n"
                + "\n"
                + ";; Returns a list consists of 0 ... n-1 integers.\n"
                + "(defun iota (n)\n"
                + "  (%iota 0 n))\n"
                + "\n"
                + ";; Returns a new list whose length is len and all members are init.\n"
                + "(defun make-list (len init)\n"
                + "  (unless (= len 0)\n"
                + "    (cons init (make-list (- len 1) init))))\n"
                + "\n"
                + ";; Applies fn to each element of lis.\n"
                + "(defun for-each (lis fn)\n"
                + "  (or (not lis)\n"
                + "      (progn (fn (car lis))\n"
                + "\t     (for-each (cdr lis) fn))))\n"
                + "\n"
                + ";;;\n"
                + ";;; N-queens solver\n"
                + ";;;\n"
                + "\n"
                + ";; Creates size x size list filled with val \"x\".\n"
                + "(defun make-board (size)\n"
                + "  (map (iota size)\n"
                + "       (lambda (_)\n"
                + "\t (make-list size 'x))))\n"
                + "\n"
                + ";; Returns location (x, y)'s element.\n"
                + "(defun get (board x y)\n"
                + "  (nth (nth board x) y))\n"
                + "\n"
                + ";; Set val \"@\" to location (x, y).\n"
                + "(defun set (board x y)\n"
                + "  (setcar (nth-tail (nth board x) y) '@))\n"
                + "\n"
                + ";; Set val \"x\" to location (x, y).\n"
                + "(defun clear (board x y)\n"
                + "  (setcar (nth-tail (nth board x) y) 'x))\n"
                + "\n"
                + ";; Returns true if location (x, y)'s value is \"@\".\n"
                + "(defun set? (board x y)\n"
                + "  (eq (get board x y) '@))\n"
                + "\n"
                + ";; Print out the given board.\n"
                + "(defun print (board)\n"
                + "  (if (not board)\n"
                + "      '$\n"
                + "    (println (car board))\n"
                + "    (print (cdr board))))\n"
                + "\n"
                + ";; Returns true if we cannot place a queen at position (x, y), assuming that\n"
                + ";; queens have already been placed on each row from 0 to x-1.\n"
                + "(defun conflict? (board x y)\n"
                + "  (any (iota x)\n"
                + "       (lambda (n)\n"
                + "\t (or\n"
                + "\t  ;; Check if there's no conflicting queen upward\n"
                + "\t  (set? board n y)\n"
                + "\t  ;; Upper left\n"
                + "\t  (let1 z (+ y (- n x))\n"
                + "\t\t(and (<= 0 z)\n"
                + "\t\t     (set? board n z)))\n"
                + "\t  ;; Upper right\n"
                + "\t  (let1 z (+ y (- x n))\n"
                + "\t\t(and (< z board-size)\n"
                + "\t\t     (set? board n z)))))))\n"
                + "\n"
                + ";; Find positions where we can place queens at row x, and continue searching for\n"
                + ";; the next row.\n"
                + "(defun %solve (board x)\n"
                + "  (if (= x board-size)\n"
                + "      ;; Problem solved\n"
                + "      (progn (print board)\n"
                + "\t     (println '$))\n"
                + "    (for-each (iota board-size)\n"
                + "\t      (lambda (y)\n"
                + "\t\t(unless (conflict? board x y)\n"
                + "\t\t  (set board x y)\n"
                + "\t\t  (%solve board (+ x 1))\n"
                + "\t\t  (clear board x y))))))\n"
                + "\n"
                + "(defun solve (board)\n"
                + "  (println 'start)\n"
                + "  (%solve board 0)\n"
                + "  (println 'done))\n"
                + "\n"
                + ";;;\n"
                + ";;; Main\n"
                + ";;;\n"
                + "\n"
                + "(define board-size 8)\n"
                + "(define board (make-board board-size))\n"
                + "(solve board)\n";
        call(s);
    }

    private void call(String s) throws ParseException {
        MiniLisp parser = new MiniLisp(new StringReader(s));
        List<Node> nodes = parser.Input();
        System.out.println(nodes.size());
    }
}
