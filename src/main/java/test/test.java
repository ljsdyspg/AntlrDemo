package test;

import antlr.CMMLEXERLexer;
import org.antlr.v4.runtime.*;

import java.io.*;
import java.util.*;


public class test {

    public static void main(String[] args) throws IOException {

        //获取测试CMM代码
        String fileName = "test.cmm";
        File file = new File(fileName);
        FileInputStream inputFile = new FileInputStream(file);
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file));

        //输出
        PrintStream printStream = new PrintStream(new FileOutputStream(new File("output.txt")));

        //词法分析
        ANTLRInputStream input = new ANTLRInputStream(inputFile);
        CMMLEXERLexer lexer = new CMMLEXERLexer(input);

        //存储Token
        ArrayDeque<Token> arrayDeque = new ArrayDeque<Token>();
        String[] tokenTags = CMMLEXERLexer._TOKEN_TAGS;
        int lineNum = 1;

        //遍历CMM
        for (Token token = lexer.nextToken(); token.getType() != Token.EOF; token = lexer.nextToken()) {
            if (lineNum != token.getLine()) {
                for (int i=lineNum; i<token.getLine(); i++) {
                    lineNumberReader.setLineNumber(i);
                    String s = lineNumberReader.readLine();
                    System.out.printf("%d: %s\n", i, s);
                    printStream.printf("%d: %s\n", i, s);
                }
                //判定条件为是否是注释行
                while (!arrayDeque.isEmpty()) {
                    Token t = arrayDeque.pollFirst();
                    if (!tokenTags[t.getType()].equals("COMMENT") && !tokenTags[t.getType()].equals("LINE_COMMENT"))
                    {
                        System.out.printf("\t%s %s\n", tokenTags[t.getType()], t.getText());
                        printStream.printf("\t%s %s\n", tokenTags[t.getType()], t.getText());
                    }
                }
                lineNum = token.getLine();
            }
            arrayDeque.addLast(token);
        }

        Token token = lexer.nextToken();
        if (token.getType() == Token.EOF){
            lineNumberReader.setLineNumber(lineNum);
            String s = lineNumberReader.readLine();
            System.out.printf("%d: %s\n", lineNum, s);
            printStream.printf("%d: %s\n", lineNum, s);
            lineNum++;
            System.out.println(lineNum+": EOF" );
            printStream.println(lineNum+": EOF" );
        }
        inputFile.close();
        printStream.close();
    }
}
