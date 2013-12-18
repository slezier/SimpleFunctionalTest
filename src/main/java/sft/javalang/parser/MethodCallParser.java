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

public class MethodCallParser {


    public static final Parser PARSER = new Parser();

    public boolean isA(StreamTokenizer tokenizer) {
        return tokenizer.ttype != '}';
    }

    public void parse(TestMethod testMethod, StreamTokenizer tokenizer) throws IOException {
        FixtureCall e = parseFixtureCall(tokenizer);
        testMethod.fixtureCalls.add(e);
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

}
