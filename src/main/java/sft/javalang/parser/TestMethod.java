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
import java.util.List;

public class TestMethod {

    public final TestClass testClass;
    public final String name;
    public final List<MethodCall> methodCalls = new ArrayList<MethodCall>();
    private String comment = null;

    public TestMethod(TestClass testClass,String name) {
        this.testClass=testClass;
        this.name = name;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return this.comment;
    }
}
