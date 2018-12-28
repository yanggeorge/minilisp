package com.threelambda.minilisp;

import com.threelambda.minilisp.core.Env;
import com.threelambda.minilisp.core.LispVisitor;
import com.threelambda.minilisp.node.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

public class Main {

    /**
     * Main entry point.
     */
    public static void main(String args[]) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        LispVisitor visitor = new LispVisitor((new Env()).initPrimitiveFunc());

        StringBuilder sb = null;
        while ((line = in.readLine()) != null) {
            try {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(line).append("\n");

                MiniLisp parser = new MiniLisp(new StringReader(sb.toString()));
                List<Node> result = parser.Input();
                for (Node node : result) {
                    visitor.visit(node);
                }
            } catch (ParseException e) {
                //System.out.println(e);
                //模拟REPL，当有解析问题时，认为没有输入完整。
                continue;
            } catch (Exception e) {
                System.out.println(sb.toString());
                e.printStackTrace();
            }
            sb = null;
        }

    }
}
