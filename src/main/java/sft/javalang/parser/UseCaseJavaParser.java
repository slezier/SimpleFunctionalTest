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
import japa.parser.ast.expr.*;
import japa.parser.ast.stmt.EmptyStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.Statement;
import sft.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class UseCaseJavaParser extends JavaFileParser {


    public UseCaseJavaParser(DefaultConfiguration configuration,Class<?> javaClass) throws IOException {
        super(configuration,javaClass);
    }

    public void feed(UseCase useCase) throws IOException {
        try {
            TypeDeclaration type = getMainType();
            try{
                feedTestClass(type, useCase);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't found class for subUseCase "+useCase.getName() + " (you probably use the "+useCase.classUnderTest.getCanonicalName()+" as a public field of an use case).",e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void feedTestClass(TypeDeclaration type, UseCase useCase) throws IOException {
        if (type.getComment() != null) {
            useCase.setComment(type.getComment().getContent());
        }
        int scenarioIndex = 0;
        for (BodyDeclaration bodyDeclaration : type.getMembers()) {
            if (bodyDeclaration instanceof MethodDeclaration) {
                MethodDeclaration methodDeclaration = (MethodDeclaration) bodyDeclaration;
                if (containsAnnotation(methodDeclaration, "Test")) {
                    feedAndOrderScenario(scenarioIndex, methodDeclaration, useCase);
                    scenarioIndex++;
                } else if (containsAnnotation(methodDeclaration, "BeforeClass")) {
                    feedTestContext(methodDeclaration, useCase.beforeUseCase,useCase);
                } else if (containsAnnotation(methodDeclaration, "AfterClass")) {
                    feedTestContext(methodDeclaration, useCase.afterUseCase,useCase);
                } else if (containsAnnotation(methodDeclaration, "Before")) {
                    feedTestContext(methodDeclaration, useCase.beforeScenario,useCase);
                } else if (containsAnnotation(methodDeclaration, "After")) {
                    feedTestContext(methodDeclaration, useCase.afterScenario,useCase);
                } else {
                    feedTestFixture(methodDeclaration, useCase);
                }
            }
        }
        for (Helper fixturesHelper : useCase.fixturesHelpers) {
            HelperJavaParser helperJavaParser = new HelperJavaParser(configuration,fixturesHelper.object);
            helperJavaParser.feed(fixturesHelper);
        }
    }

    private void feedTestContext(MethodDeclaration methodDeclaration, ContextHandler contextHandler,UseCase useCase) {
        contextHandler.fixtureCalls = extractFixtureCalls(useCase,methodDeclaration);
    }

    private void feedAndOrderScenario(int expectedIndex, MethodDeclaration methodDeclaration, UseCase useCase) {
        for (Scenario scenario : useCase.scenarios) {
            if (scenario.method.getName().equals(methodDeclaration.getName())) {
                int currentIndex = useCase.scenarios.indexOf(scenario);
                if(currentIndex != expectedIndex){
                    Collections.swap(useCase.scenarios,useCase.scenarios.indexOf(scenario),expectedIndex);
                }
                if (methodDeclaration.getComment() != null) {
                    scenario.setComment(methodDeclaration.getComment().getContent());
                }
                scenario.fixtureCalls.addAll(extractFixtureCalls(useCase,methodDeclaration));
                return;
            }
        }
    }

    private ArrayList<FixtureCall> extractFixtureCalls(UseCase useCase,MethodDeclaration methodDeclaration) {
        ArrayList<FixtureCall> fixtureCalls = new ArrayList<FixtureCall>();
        if( methodDeclaration.getBody().getStmts() == null){
            System.err.println("In class "+javaClass.getCanonicalName()+" method " + methodDeclaration.getName() + " don't have any fixture call.");
            return fixtureCalls;
        }
        int line = methodDeclaration.getBody().getBeginLine();
        for (Statement stmt : methodDeclaration.getBody().getStmts()) {
            if (stmt instanceof ExpressionStmt) {
                Expression expr = ((ExpressionStmt) stmt).getExpression();
                if (expr instanceof MethodCallExpr) {
                    final int emptyLines = stmt.getBeginLine() - 1 - line;
                    fixtureCalls.add(extractFixtureCall(useCase, (MethodCallExpr) expr,emptyLines));
                    line= stmt.getBeginLine();
                }
            }
        }
        return fixtureCalls;
    }

    private FixtureCall extractFixtureCall(UseCase useCase, MethodCallExpr methodCall,int emptyLine) {
        final String methodCallName = extractMethodCallName(methodCall);
        final ArrayList<String> parameters = extractParameters(methodCall);
        final Fixture fixture = useCase.getFixtureByMethodName(methodCallName);
        final int callLine = methodCall.getBeginLine();
        return new FixtureCall(methodCallName, callLine, fixture, parameters,emptyLine);
    }

    private ArrayList<String> extractParameters(MethodCallExpr methodCall) {
        ArrayList<String> parameters = new ArrayList<String>();
        if (methodCall.getArgs() != null) {
            for (Expression expression : methodCall.getArgs()) {
                if (expression instanceof StringLiteralExpr) {
                    StringLiteralExpr stringLiteralExpr = (StringLiteralExpr) expression;
                    parameters.add(stringLiteralExpr.getValue());
                } else if (expression instanceof LiteralExpr) {
                    LiteralExpr literalExpr = (LiteralExpr) expression;
                    parameters.add(literalExpr.toString());
                }
            }
        }
        return parameters;
    }

    private String extractMethodCallName(MethodCallExpr methodCall) {
        Expression scope = methodCall.getScope();
        String methodCallName = "";
        if (scope != null) {
            methodCallName = scope + ".";
        }
        methodCallName += methodCall.getName();
        return methodCallName;
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
