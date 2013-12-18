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

class TestClassParser extends JavaElementParser {
    @Override
    public String parse(TestClass testClass, String comment, StreamTokenizer tokenizer) throws IOException {
        testClass.setComment(comment);
        PARSER.jumpWord(tokenizer, "class");
        return null;
    }

    @Override
    public boolean isA(StreamTokenizer tokenizer) {
        return "class".equals(tokenizer.sval);
    }


}
