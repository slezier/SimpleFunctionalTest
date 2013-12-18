/*******************************************************************************
 * Copyright (c) 2013 Sylvain Lézier.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sylvain Lézier - initial implementation
 *******************************************************************************/
package sft.javalang.parser;


import sft.report.FileSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

public class JavaClassParser {


    public static final CommentParser COMMENT_PARSER = new CommentParser();
    public static final TestClassParser CLASS_PARSER = new TestClassParser();
    public static final TestMethodParser TEST_METHOD_PARSER = new TestMethodParser();
    public static final BeforeClassParser BEFORE_CLASS_PARSER = new BeforeClassParser();
    public static final AfterClassParser AFTER_CLASS_PARSER = new AfterClassParser();
    public static final BeforeParser BEFORE_PARSER = new BeforeParser();
    public static final AfterParser AFTER_PARSER = new AfterParser();
    public TestClass testClass = new TestClass();

    private static final FileSystem fileSystem = new FileSystem();



    public JavaClassParser(Class<?> javaClass) throws IOException {
        testClass = parseTestClass(javaClass);
    }

    /*

     root = ANY_CHAR^[comment] public class ANY_CHAR { javaItem } ;
     javaItem = ANY_CHAR^(comment | testMethod | contextMethod);
     comment = '\*' ANY_CHAR '*\' ;
     testMethod = '@Test' WHITE_SPACE 'public void' WHITE_SPACE methodName ANY_CHAR^'{' {methods} '}';
     contextMethod = '@' ('beforeUseCase'|'afterUseCase'|'beforeClass'|'afterClass')
     */
    public TestClass parseTestClass(Class<?> javaClass) throws IOException {

        TestClass result = new TestClass();


        File javaFile = fileSystem.getSourceFile(javaClass);

        InputStream javaIn = new FileInputStream(javaFile);
        StreamTokenizer tokenizer = new StreamTokenizer(new BufferedReader(new InputStreamReader(javaIn, "UTF-8")));
        tokenizer.wordChars('_', '_');
        tokenizer.wordChars('/', '/');
        tokenizer.wordChars('*', '*');
        tokenizer.wordChars('\'', '\'');
        tokenizer.wordChars(';', ';');
        tokenizer.wordChars('"', '"');
        tokenizer.wordChars('@', '@');
        /*
            COMMENT
            @Test
              - Before
              - After
              - BeforeClass
              - AfterClass
        */

        String comment = null;
        while (true) {
            JavaElementParser nextElementToParse = getNextElementToParse(tokenizer);
            if (nextElementToParse == null) {
                break;
            } else {
                comment = nextElementToParse.parse(result, comment, tokenizer);
            }
        }
        javaIn.close();

        return result;
    }

    private JavaElementParser getNextElementToParse(StreamTokenizer tokenizer) throws IOException {
        while (true) {
            if (COMMENT_PARSER.isA(tokenizer)) {
                return COMMENT_PARSER;
            } else if (CLASS_PARSER.isA(tokenizer)) {
                return CLASS_PARSER;
            } else if (TEST_METHOD_PARSER.isA(tokenizer)) {
                return TEST_METHOD_PARSER;
            } else if (BEFORE_CLASS_PARSER.isA(tokenizer)) {
                return BEFORE_CLASS_PARSER;
            } else if (AFTER_CLASS_PARSER.isA(tokenizer)) {
                return AFTER_CLASS_PARSER;
            } else if (BEFORE_PARSER.isA(tokenizer)) {
                return BEFORE_PARSER;
            } else if (AFTER_PARSER.isA(tokenizer)) {
                return AFTER_PARSER;
            } else if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
                return null;
            }
            tokenizer.nextToken();
        }
    }

}
