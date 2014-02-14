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

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import sft.DefaultConfiguration;
import sft.Fixture;
import sft.FixturesHolder;
import sft.environment.FileSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JavaFileParser {
    protected final Class<?> javaClass;
    protected DefaultConfiguration configuration;

    public JavaFileParser(DefaultConfiguration configuration,Class<?> aClass) {
        this.configuration = configuration;
        this.javaClass = aClass;
    }

    protected TypeDeclaration getMainType() throws ParseException, IOException {
        File javaFile = configuration.getSourceFolder().getFileFromClass(javaClass, ".java");
        CompilationUnit cu = JavaParser.parse(javaFile, "UTF-8");
        return cu.getTypes().get(0);
    }

    protected void feedTestFixture(MethodDeclaration methodDeclaration, FixturesHolder helper) {
        for (Fixture fixture : helper.fixtures) {
            if (fixture.method.getName().equals(methodDeclaration.getName())) {
                fixture.parametersName = extractParametersName(methodDeclaration);
                return;
            }
        }
    }

    private ArrayList<String> extractParametersName(MethodDeclaration methodDeclaration) {
        ArrayList<String> parametersName = new ArrayList<String>();
        if (methodDeclaration.getParameters() != null) {
            for (Parameter parameter : methodDeclaration.getParameters()) {
                parametersName.add(parameter.getId().getName());
            }
        }
        return parametersName;
    }
}
