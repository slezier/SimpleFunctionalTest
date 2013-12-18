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


import java.util.ArrayList;

public class FixtureCall {

    public final String name;
    public final int line;
    public ArrayList<String> parameters = new ArrayList<String>();

    public FixtureCall(String name,int line) {
        this.name = name;
        this.line = line;
    }

}
