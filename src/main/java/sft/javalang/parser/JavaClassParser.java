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
import japa.parser.ast.Comment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.LiteralExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.Statement;
import sft.report.FileSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JavaClassParser {
    private static final FileSystem fileSystem = new FileSystem();
    public TestClass testClass = new TestClass();


    public JavaClassParser(Class<?> javaClass) throws IOException {
        testClass = parseTestClass(javaClass);
    }

    public TestClass parseTestClass(Class<?> javaClass) throws IOException {
        File javaFile = fileSystem.sourceFolder.getFileFromClass(javaClass, ".java");
        return extractTestClass(javaFile);
    }

    private TestClass extractTestClass(File javaFile) throws IOException {
        try {
            CompilationUnit cu = JavaParser.parse(javaFile, "UTF-8");
            TypeDeclaration type = cu.getTypes().get(0);

            TestClass testClass = new TestClass();
            if (type.getComment() != null) {
                testClass.setComment(type.getComment().getContent());
            }
            for (BodyDeclaration bodyDeclaration : type.getMembers()) {
                if (bodyDeclaration instanceof MethodDeclaration) {
                    MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
                    if (methodContainsAnnotation(methodDeclaration, "Test")) {
                        testClass.testMethods.add(extractTestMethod(testClass,methodDeclaration));
                    } else if (methodContainsAnnotation(methodDeclaration, "BeforeClass")) {
                        testClass.beforeClass = extractTestContext(methodDeclaration);
                    } else if (methodContainsAnnotation(methodDeclaration, "AfterClass")) {
                        testClass.afterClass = extractTestContext(methodDeclaration);
                    } else if (methodContainsAnnotation(methodDeclaration, "Before")) {
                        testClass.before = extractTestContext(methodDeclaration);
                    } else if (methodContainsAnnotation(methodDeclaration, "After")) {
                        testClass.after = extractTestContext(methodDeclaration);
                    } else {
                        testClass.fixtures.add(extractTestFixture(methodDeclaration));
                    }
                }
            }
            return testClass;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private OtherMethod extractTestFixture(MethodDeclaration methodDeclaration) {
        String methodName = methodDeclaration.getName();
        ArrayList<String> parametersName = extractParametersName(methodDeclaration);

        OtherMethod otherMethod = new OtherMethod(methodName, parametersName);
        return otherMethod;
    }

    private ArrayList<String> extractParametersName(MethodDeclaration methodDeclaration) {
        ArrayList<String> parametersName = new ArrayList<String>();
        if( methodDeclaration.getParameters() != null ){
            for (Parameter parameter : methodDeclaration.getParameters()) {
                parametersName.add(parameter.getId().getName());
            }
        }
        return  parametersName;
    }

    private TestContext extractTestContext(MethodDeclaration methodDeclaration) {
        TestContext testContext = new TestContext();
        testContext.fixtureCalls.addAll(extractFixtureCalls(methodDeclaration));
        return testContext;
    }

    private TestMethod extractTestMethod( TestClass testClass,MethodDeclaration methodDeclaration) {
        Comment methodComment = methodDeclaration.getComment();
        String methodName = methodDeclaration.getName();
        TestMethod testMethod = new TestMethod(testClass,methodName);
        if (methodComment != null) {
            testMethod.setComment(methodComment.getContent());
        }
        testMethod.fixtureCalls.addAll(extractFixtureCalls(methodDeclaration));
        return testMethod;
    }

    private ArrayList<FixtureCall> extractFixtureCalls(MethodDeclaration methodDeclaration) {
        ArrayList<FixtureCall> fixtureCalls = new ArrayList<FixtureCall>();
        for (Statement stmt : methodDeclaration.getBody().getStmts()) {
            if (stmt instanceof ExpressionStmt) {
                Expression expr = ((ExpressionStmt) stmt).getExpression();
                if (expr instanceof MethodCallExpr) {
                    MethodCallExpr methodCall = (MethodCallExpr) expr;
                    Expression scope = methodCall.getScope();
                    String methodCallName = "";
                    if (scope != null) {
                        methodCallName = scope + ".";
                    }
                    methodCallName += methodCall.getName();
                    FixtureCall call = new FixtureCall(methodCallName, methodCall.getBeginLine());
                    if (methodCall.getArgs() != null) {
                        for (Expression expression : methodCall.getArgs()) {
                            if (expression instanceof StringLiteralExpr) {
                                StringLiteralExpr stringLiteralExpr = (StringLiteralExpr) expression;
                                call.parameters.add(stringLiteralExpr.getValue());
                            } else if (expression instanceof LiteralExpr) {
                                LiteralExpr literalExpr = (LiteralExpr) expression;
                                call.parameters.add(literalExpr.toString());
                            }
                        }
                    }
                    fixtureCalls.add(call);
                }
            }

        }
        return fixtureCalls;
    }

    private boolean methodContainsAnnotation(MethodDeclaration methodDeclaration, String annotation) {
        if (methodDeclaration.getAnnotations() != null) {
            for (AnnotationExpr annotationExpr : methodDeclaration.getAnnotations()) {
                if (annotation.equals(annotationExpr.getName().toString())) {
                    return true;
                }
            }
        }
        return false;
    }

}
