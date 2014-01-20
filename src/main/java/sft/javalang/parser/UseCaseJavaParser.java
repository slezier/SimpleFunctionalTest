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


import japa.parser.ParseException;
import japa.parser.ast.Comment;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.*;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.Statement;
import sft.ContextHandler;
import sft.Helper;
import sft.Scenario;
import sft.UseCase;

import java.io.IOException;
import java.util.ArrayList;

public class UseCaseJavaParser extends JavaFileParser {
    public TestClass testClass = new TestClass();


    public UseCaseJavaParser(Class<?> javaClass) throws IOException {
        super(javaClass);
        try {
            TypeDeclaration type = getMaintType();
            testClass = extractTestClass(type);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void feed(UseCase useCase) throws IOException {
        try {
            TypeDeclaration type = getMaintType();
            feedTestClass(type, useCase);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private TestClass feedTestClass(TypeDeclaration type, UseCase useCase) throws IOException {
        if (type.getComment() != null) {
            useCase.setComment(type.getComment().getContent());
        }
        for (BodyDeclaration bodyDeclaration : type.getMembers()) {
            if (bodyDeclaration instanceof MethodDeclaration) {
                MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
                if (containsAnnotation(methodDeclaration, "Test")) {
                    feedScenario(methodDeclaration, useCase);
                } else if (containsAnnotation(methodDeclaration, "BeforeClass")) {
                    feedTestContext(methodDeclaration, useCase.beforeUseCase);
                } else if (containsAnnotation(methodDeclaration, "AfterClass")) {
                    feedTestContext(methodDeclaration, useCase.afterUseCase);
                } else if (containsAnnotation(methodDeclaration, "Before")) {
                    feedTestContext(methodDeclaration, useCase.beforeScenario);
                } else if (containsAnnotation(methodDeclaration, "After")) {
                    feedTestContext(methodDeclaration, useCase.afterScenario);
                } else {
                    feedTestFixture(methodDeclaration, useCase);
                }
            }
        }
        for (Helper fixturesHelper : useCase.fixturesHelpers) {
            HelperJavaParser helperJavaParser = new HelperJavaParser(fixturesHelper.object);
            helperJavaParser.feed(fixturesHelper);
        }
        return testClass;
    }


    private TestClass extractTestClass(TypeDeclaration type) {
        TestClass testClass = new TestClass();
        for (BodyDeclaration bodyDeclaration : type.getMembers()) {
            if (bodyDeclaration instanceof MethodDeclaration) {
                MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
                if (containsAnnotation(methodDeclaration, "Test")) {
                    testClass.testMethods.add(extractTestMethod(testClass, methodDeclaration));
                }
            }
        }
        return testClass;
    }


    private void feedTestContext(MethodDeclaration methodDeclaration, ContextHandler beforeUseCase) {
        beforeUseCase.methodCalls = extractFixtureCalls(methodDeclaration);
    }

    private void feedScenario(MethodDeclaration methodDeclaration, UseCase useCase) {
        for (Scenario scenario : useCase.scenarios) {
            if (scenario.getName().equals(methodDeclaration.getName())) {
                if (methodDeclaration.getComment() != null) {
                    scenario.setComment(methodDeclaration.getComment().getContent());
                }
                scenario.methodCalls.addAll(extractFixtureCalls(methodDeclaration));
                return;
            }
        }
    }

    private TestMethod extractTestMethod(TestClass testClass, MethodDeclaration methodDeclaration) {
        Comment methodComment = methodDeclaration.getComment();
        String methodName = methodDeclaration.getName();
        TestMethod testMethod = new TestMethod(testClass, methodName);
        if (methodComment != null) {
            testMethod.setComment(methodComment.getContent());
        }
        testMethod.methodCalls.addAll(extractFixtureCalls(methodDeclaration));
        return testMethod;
    }


    private ArrayList<MethodCall> extractFixtureCalls(MethodDeclaration methodDeclaration) {
        ArrayList<MethodCall> methodCalls = new ArrayList<MethodCall>();
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
                    MethodCall call = new MethodCall(methodCallName, methodCall.getBeginLine());
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
                    methodCalls.add(call);
                }
            }

        }
        return methodCalls;
    }

    private boolean containsAnnotation(BodyDeclaration methodDeclaration, String annotation) {
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
