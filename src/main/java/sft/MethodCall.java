/*******************************************************************************
 * Copyright (c) 2013, 2014 Sylvain Lézier.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sylvain Lézier - initial implementation
 *******************************************************************************/
package sft;


import java.util.ArrayList;

public class MethodCall {

    public final String name;
    public final int line;
    public ArrayList<String> parameters = new ArrayList<String>();

    public MethodCall(String name, int line) {
        this.name = name;
        this.line = line;
    }

}
