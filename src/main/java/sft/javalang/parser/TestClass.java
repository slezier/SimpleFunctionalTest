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


public class TestClass {

    public final List<TestMethod> testMethods = new ArrayList<TestMethod>();
    public final List<OtherMethod> fixtures =new ArrayList<OtherMethod>();
    private String comment;
    public TestContext beforeClass;
    public TestContext afterClass;
    public TestContext after;
    public TestContext before;

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public boolean haveComment() {
        return comment != null;
    }

    public OtherMethod getTestFistureByMehtdname(String name) {
        for (OtherMethod fixture : fixtures) {
            if( fixture.methodName.endsWith(name)){
                return fixture;
            }
        }
        throw new RuntimeException("Method not found "+ name);
    }
}
