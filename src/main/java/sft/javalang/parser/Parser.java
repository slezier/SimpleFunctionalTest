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

public class Parser {
    public void jumpWord(StreamTokenizer tokenizer, String word) throws IOException {
        if (nextWordIs(tokenizer, word)) {
            jumpNextWord(tokenizer);
        }
    }

    public void jumpNextWord(StreamTokenizer tokenizer) throws IOException {
        tokenizer.nextToken();
    }

    public boolean nextWordIs(StreamTokenizer tokenizer, String word) {
        return tokenizer.ttype == StreamTokenizer.TT_WORD && word.equals(tokenizer.sval);
    }

    public void expectWord(StreamTokenizer tokenizer, String word) throws IOException {
        boolean isExpectedWord = nextWordIs(tokenizer, word);
        if (!isExpectedWord) {
            throw new RuntimeException("Expecting word '" + word + "' having " + tokenizer);
        }
        tokenizer.nextToken();
    }

    public void expectChar(StreamTokenizer tokenizer, char value) throws IOException {
        if (tokenizer.ttype != value) {
            throw new RuntimeException("Expecting char '" + value + "' having " + tokenizer);
        }
        tokenizer.nextToken();
    }

    public boolean nextCharIs(StreamTokenizer tokenizer, char character) {
        return tokenizer.ttype == character;
    }

    public String extractNextWord(StreamTokenizer tokenizer) throws IOException {
        String result = tokenizer.sval;
        tokenizer.nextToken();
        return result;
    }

    protected void jumpChar(StreamTokenizer tokenizer, char value) throws IOException {
        if (tokenizer.ttype != value) {
            jumpNextChar(tokenizer);
        }

    }

    private void jumpNextChar(StreamTokenizer tokenizer) throws IOException {
        tokenizer.nextToken();
    }
}
