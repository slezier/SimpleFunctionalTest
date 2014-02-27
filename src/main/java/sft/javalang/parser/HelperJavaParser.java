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
package sft.javalang.parser;

import japa.parser.ParseException;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import sft.DefaultConfiguration;
import sft.Helper;

import java.io.IOException;

public class HelperJavaParser extends JavaFileParser {


    public HelperJavaParser(DefaultConfiguration configuration,Object helperObject) {
        super(configuration,helperObject.getClass());
    }


    public void feed(Helper helper) throws IOException {

        try {
            TypeDeclaration type = getMainType();
            feedHelperClass(type, helper);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    private void feedHelperClass(TypeDeclaration type, Helper helper) {
        for (BodyDeclaration bodyDeclaration : type.getMembers()) {
            if (bodyDeclaration instanceof MethodDeclaration) {
                MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
                feedTestFixture(methodDeclaration, helper);
            }
        }
    }
}
