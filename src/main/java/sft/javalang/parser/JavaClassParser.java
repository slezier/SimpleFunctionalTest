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
import japa.parser.ast.Node;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
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
import sft.ContextHandler;
import sft.Fixture;
import sft.Scenario;
import sft.UseCase;
import sft.report.FileSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JavaClassParser {
    private static final FileSystem fileSystem = new FileSystem();
    private final Class<?> javaClass;
    public TestClass testClass = new TestClass();


    public JavaClassParser(Class<?> javaClass) throws IOException {
        this.javaClass = javaClass;
        File javaFile = fileSystem.sourceFolder.getFileFromClass(javaClass, ".java");
        testClass = extractTestClass(javaFile);
    }

    public void feed(UseCase useCase) throws IOException {
        File javaFile = fileSystem.sourceFolder.getFileFromClass(javaClass, ".java");

        try {
            CompilationUnit cu = JavaParser.parse(javaFile, "UTF-8");
            TypeDeclaration type = cu.getTypes().get(0);
            extractTestClass(type,useCase);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private TestClass extractTestClass(File javaFile) throws IOException {
        try {
            CompilationUnit cu = JavaParser.parse(javaFile, "UTF-8");
            TypeDeclaration type = cu.getTypes().get(0);
            return extractTestClass(type);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private TestClass extractTestClass(TypeDeclaration type,UseCase useCase) {
        if (type.getComment() != null) {
            useCase.setComment(type.getComment().getContent());
        }
        for (BodyDeclaration bodyDeclaration : type.getMembers()) {
            if(bodyDeclaration instanceof FieldDeclaration){
                FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration ;
                if(containsAnnotation(fieldDeclaration,"FixturesHelper")){

                    System.out.println(fieldDeclaration.getVariables().get(0).getId());
                }
            }else if (bodyDeclaration instanceof MethodDeclaration) {
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
        return testClass;
    }



    private TestClass extractTestClass(TypeDeclaration type) {
        TestClass testClass = new TestClass();
        if (type.getComment() != null) {
            testClass.setComment(type.getComment().getContent());
        }
        for (BodyDeclaration bodyDeclaration : type.getMembers()) {
            if(bodyDeclaration instanceof FieldDeclaration){
                FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration ;
                if(containsAnnotation(fieldDeclaration,"FixturesHelper")){

                    System.out.println(fieldDeclaration.getVariables().get(0).getId());
                }
            }else if (bodyDeclaration instanceof MethodDeclaration) {
                MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
                if (containsAnnotation(methodDeclaration, "Test")) {
                    testClass.testMethods.add(extractTestMethod(testClass,methodDeclaration));
                } else if (containsAnnotation(methodDeclaration, "BeforeClass")) {
                    testClass.beforeClass = extractTestContext(methodDeclaration);
                } else if (containsAnnotation(methodDeclaration, "AfterClass")) {
                    testClass.afterClass = extractTestContext(methodDeclaration);
                } else if (containsAnnotation(methodDeclaration, "Before")) {
                    testClass.before = extractTestContext(methodDeclaration);
                } else if (containsAnnotation(methodDeclaration, "After")) {
                    testClass.after = extractTestContext(methodDeclaration);
                } else {
                    testClass.fixtures.add(extractTestFixture(methodDeclaration));
                }
            }
        }
        return testClass;
    }

    private void feedTestFixture(MethodDeclaration methodDeclaration, UseCase useCase) {
        for (Fixture fixture : useCase.fixtures) {
            if(fixture.getName().equals(methodDeclaration.getName())){
                fixture.parametersName =    extractParametersName(methodDeclaration);
                return;
            }
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


    private void feedTestContext(MethodDeclaration methodDeclaration, ContextHandler beforeUseCase) {
        beforeUseCase.methodCalls = extractFixtureCalls(methodDeclaration);
    }

    private TestContext extractTestContext(MethodDeclaration methodDeclaration) {
        TestContext testContext = new TestContext();
        testContext.methodCalls.addAll(extractFixtureCalls(methodDeclaration));
        return testContext;
    }

    private void feedScenario(MethodDeclaration methodDeclaration, UseCase useCase) {
        for (Scenario scenario : useCase.scenarios) {
            if(scenario.getName().equals(methodDeclaration.getName())){
                if (methodDeclaration.getComment()!= null) {
                    scenario.setComment(methodDeclaration.getComment().getContent());
                }
                scenario.methodCalls.addAll(extractFixtureCalls(methodDeclaration));
                return;
            }
        }
    }

    private TestMethod extractTestMethod( TestClass testClass,MethodDeclaration methodDeclaration) {
        Comment methodComment = methodDeclaration.getComment();
        String methodName = methodDeclaration.getName();
        TestMethod testMethod = new TestMethod(testClass,methodName);
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
