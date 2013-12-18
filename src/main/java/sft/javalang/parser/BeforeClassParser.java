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

class BeforeClassParser extends JavaElementParser {

    @Override
    public String parse(TestClass testClass, String comment, StreamTokenizer tokenizer) throws IOException {
        extractDeclaration(tokenizer);
        testClass.beforeClass = parseTestMethod(tokenizer);
        return null;

    }
    protected TestContext parseTestMethod(StreamTokenizer tokenizer) throws IOException {
        TestContext testMethod = new TestContext();
        testMethod.fixtureCalls.addAll(parseFixtureCalls(tokenizer));
        return testMethod;
    }


    private void extractDeclaration(StreamTokenizer tokenizer) throws IOException {
        PARSER.expectWord(tokenizer,"@BeforeClass");
        if(tokenizer.sval.startsWith("@")) {
            extractAnnotation(tokenizer);
        }
        PARSER.expectWord(tokenizer, "public");
        PARSER.expectWord(tokenizer, "static");
        PARSER.expectWord(tokenizer, "void");
        PARSER.extractNextWord(tokenizer);
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
    }

    protected Collection<FixtureCall> parseFixtureCalls(StreamTokenizer tokenizer) throws IOException {
        Collection<FixtureCall> fixtureCalls = new ArrayList<FixtureCall>();
        while (haveNextFixture(tokenizer)) {
            FixtureCall e = parseFixtureCall(tokenizer);
            fixtureCalls.add(e);
        }
        return fixtureCalls;
    }
    private boolean haveNextFixture(StreamTokenizer tokenizer) {
        return tokenizer.ttype != '}';
    }

    private FixtureCall parseFixtureCall(StreamTokenizer tokenizer) throws IOException {
        FixtureCall result = new FixtureCall(tokenizer.sval, tokenizer.lineno());
        tokenizer.nextToken();
        PARSER.expectChar(tokenizer, '(');
        while (!PARSER.nextCharIs(tokenizer, ')')) {
            String e = extractFixtureParameter(tokenizer);
            result.parameters.add(e);
        }
        PARSER.expectChar(tokenizer, ')');
        PARSER.expectWord(tokenizer, ";");

        return result;
    }

    private String extractFixtureParameter(StreamTokenizer tokenizer) throws IOException {
        String result = extractParameterValue(tokenizer);
        if (PARSER.nextCharIs(tokenizer, ',')) {
            tokenizer.nextToken();
        }
        return result;
    }

    private String extractParameterValue(StreamTokenizer tokenizer) throws IOException {
        String value = "";
        while (true) {
            if (tokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                value += new Integer((int) tokenizer.nval).toString();
            } else if (tokenizer.ttype == StreamTokenizer.TT_WORD) {
                value += tokenizer.sval;
            } else {
                return value;
            }
            tokenizer.nextToken();
        }
    }



    private void extractAnnotation(StreamTokenizer tokenizer) throws IOException {
        while (!"public".equals(tokenizer.sval)) {
            tokenizer.nextToken();
        }
    }



    @Override
    public boolean isA(StreamTokenizer tokenizer) throws IOException {
        return PARSER.nextWordIs(tokenizer, "@BeforeClass");
    }

}
