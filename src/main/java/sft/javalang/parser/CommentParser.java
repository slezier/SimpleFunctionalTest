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

import static java.lang.Math.abs;

public class CommentParser extends JavaElementParser {
    @Override
    public String parse(TestClass testClass, String comment, StreamTokenizer tokenizer) throws IOException {
        return extractComment(tokenizer);
    }

    private String extractComment(StreamTokenizer tokenizer) throws IOException {
        boolean lastTokenWasString = true;
        String comment = "";
        if (isA(tokenizer)) {
            tokenizer.nextToken();
            while (!"*/".equals(tokenizer.sval)) {


                if (tokenizer.sval == null) {

                    if (tokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                        comment += " ";
                        if (abs(tokenizer.nval) == tokenizer.nval) {
                            comment += (int) tokenizer.nval;
                        } else {
                            comment += tokenizer.nval;
                        }
                        lastTokenWasString = true;

                    } else {
                        String typeVal = new String(new char[]{(char) tokenizer.ttype});
                        comment += typeVal;
                        lastTokenWasString = false;
                    }
                } else {
                    if (lastTokenWasString) {
                        comment += " ";
                    }
                    comment += tokenizer.sval;
                    lastTokenWasString = true;
                }
                tokenizer.nextToken();
            }
            tokenizer.nextToken();
            return comment;
        }
        return null;
    }

    public  boolean isA(StreamTokenizer tokenizer) {
        return "/*".equals(tokenizer.sval);
    }

}
