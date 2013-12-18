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

public abstract class JavaElementParser {

    public abstract String parse(TestClass testClass, String comment, StreamTokenizer tokenizer) throws IOException;
    public abstract boolean isA(StreamTokenizer tokenizerS) throws IOException;


    final static Parser PARSER = new Parser();


}
