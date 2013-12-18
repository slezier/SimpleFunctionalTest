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

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collection;

class TestMethodParser extends JavaElementParser {


    private final static MethodCallParser METHOD_CALL_PARSER= new MethodCallParser();

    @Override
    public String parse(TestClass testClass, String comment, StreamTokenizer tokenizer) throws IOException {
        TestMethod e = parseTestMethod(tokenizer, comment);
        testClass.testMethods.add(e);
        return null;
    }


    protected TestMethod parseTestMethod(StreamTokenizer tokenizer, String comment) throws IOException {
        String methodName = extractMethodName(tokenizer);
        TestMethod testMethod = new TestMethod(methodName);
        testMethod.setComment(comment);
        parseFixtureCalls(testMethod,tokenizer);

        return testMethod;
    }


    private String extractMethodName(StreamTokenizer tokenizer) throws IOException {
        PARSER.expectWord(tokenizer,"@Test");
        while (tokenizer.sval.startsWith("@")) {
            extractAnnotation(tokenizer);
        }
        PARSER.expectWord(tokenizer, "public");
        PARSER.expectWord(tokenizer, "void");
        String methodName = PARSER.extractNextWord(tokenizer);
        PARSER.expectChar(tokenizer, '(');
        PARSER.expectChar(tokenizer, ')');
        if (PARSER.nextWordIs(tokenizer, "throws")) {
            PARSER.jumpWord(tokenizer, "throws");
            PARSER.jumpNextWord(tokenizer);
            while (PARSER.nextCharIs(tokenizer, ',')) {
                PARSER.jumpChar(tokenizer, ',');
                PARSER.jumpNextWord(tokenizer);
            }
        }
        PARSER.expectChar(tokenizer, '{');
        return methodName;
    }

    protected Collection<FixtureCall> parseFixtureCalls(TestMethod testMethod, StreamTokenizer tokenizer) throws IOException {
        Collection<FixtureCall> fixtureCalls = new ArrayList<FixtureCall>();
        while (METHOD_CALL_PARSER.isA(tokenizer)) {
            METHOD_CALL_PARSER.parse(testMethod,tokenizer);
        }
        return fixtureCalls;
    }

    private void extractAnnotation(StreamTokenizer tokenizer) throws IOException {
        while (!"public".equals(tokenizer.sval)) {
            tokenizer.nextToken();
        }
    }


    @Override
    public boolean isA(StreamTokenizer tokenizer) throws IOException {
        return PARSER.nextWordIs(tokenizer, "@Test");
    }
}
