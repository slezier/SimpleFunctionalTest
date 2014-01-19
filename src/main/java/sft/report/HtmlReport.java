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
package sft.report;

import org.junit.runner.notification.RunListener;
import sft.Fixture;
import sft.UseCase;
import sft.javalang.parser.FixtureCall;
import sft.javalang.parser.JavaClassParser;
import sft.javalang.parser.TestFixture;
import sft.javalang.parser.TestMethod;
import sft.result.ScenarioResult;
import sft.result.UseCaseResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class HtmlReport extends RunListener {
    private final HtmlResources htmlResources = new HtmlResources();
    private final FileSystem fileSystem = new FileSystem();
    private final UseCaseResult useCaseResult;

    public HtmlReport(UseCaseResult useCase) {
        this.useCaseResult = useCase;
    }

    public void useCaseIsFinished() throws IOException, IllegalAccessException {
        htmlResources.ensureIsCreated();

        UseCase useCase = useCaseResult.useCase;
        Class<?> classUnderTest = useCase.classUnderTest;
        JavaClassParser javaTokenizer = new JavaClassParser(classUnderTest);

        File htmlFile = fileSystem.targetFolder.createFileFromClass(classUnderTest, ".html");

        Writer html = new OutputStreamWriter(new FileOutputStream(htmlFile));
        html.write("<html><head><title>\n");
        html.write(useCase.getName() + "\n");
        html.write("</title>\n");
        html.write(htmlResources.getIncludeCssDirectives(useCase.classUnderTest));
        html.write(htmlResources.getIncludeJsDirectives(useCase.classUnderTest));

        html.write("</head>\n");
        html.write("<body class=\"useCase " + htmlResources.convertIssue(useCaseResult.getIssue()) + "\"><div class=\"container\">\n");

        html.write("<div class=\"page-header\">");
        html.write("<div class=\"text-center\"><h1><span class=\"useCaseName\">" + useCase.getName() + "</span></h1></div>\n");

        if (javaTokenizer.testClass.haveComment()) {
            html.write("<div class=\"comment\">" + javaTokenizer.testClass.getComment() + "</div>");
        }
        html.write("</div>");

        if(javaTokenizer.testClass.beforeClass != null){
            html.write("<div class=\"panel panel-default beforeUseCase\"><div class=\"panel-body\">");
            for( FixtureCall fixtureCall:javaTokenizer.testClass.beforeClass.fixtureCalls){
                Fixture fixture = useCase.getFixtureByMethodName(fixtureCall.name);

                TestFixture testFixture = javaTokenizer.testClass.getTestFistureByMehtdname(fixtureCall.name);
                html.write("<div><span>" + fixture.getText(testFixture.parametersName,fixtureCall.parameters) + "</span></div>\n");
            }
            html.write("</div></div>");
        }

        for (TestMethod testMethod : javaTokenizer.testClass.testMethods) {
            for (ScenarioResult scenarioResult : useCaseResult.scenarioResults) {
                if (scenarioResult.scenario.method.getName().equals(testMethod.name)) {
                    ScenarioHtml scenarioHtml = new ScenarioHtml(htmlResources, useCase, html, testMethod, scenarioResult,javaTokenizer.testClass.before,javaTokenizer.testClass.after);
                    scenarioHtml.write();
                }
            }
        }

        if(javaTokenizer.testClass.afterClass != null){
            html.write("<div class=\"panel panel-default afterUseCase\"><div class=\"panel-body\">");
            for( FixtureCall fixtureCall:javaTokenizer.testClass.afterClass.fixtureCalls){
                Fixture fixture = useCase.getFixtureByMethodName(fixtureCall.name);
                TestFixture testFixture = javaTokenizer.testClass.getTestFistureByMehtdname(fixtureCall.name);
                html.write("<div><span>" + fixture.getText(testFixture.parametersName,fixtureCall.parameters) + "</span></div>\n");
            }
            html.write("</div></div>");
        }

        if (!useCaseResult.subUseCaseResults.isEmpty()) {
            html.write("<div class=\"panel panel-default\"><div class=\"panel-heading\"><h3>Related uses cases</h3></div><div class=\"panel-body\"><ul>");

            for (UseCaseResult subUseCaseResult : useCaseResult.subUseCaseResults) {
                html.write("<li class=\"relatedUseCase "+ htmlResources.convertIssue(subUseCaseResult.getIssue()) +"\"><a href=\"" + writeHtml(subUseCaseResult.useCase) + "\"><span>" + subUseCaseResult.useCase.getName() + "</span></a></li>");
                try {
                    new HtmlReport(subUseCaseResult).useCaseIsFinished();
                }catch (Throwable t){
                    html.write("<div>"+t.getMessage()+"</div>");
                }
            }
            html.write("</ul></div></div>");
        }

        html.write("</div></body></html>");

        html.close();
        System.out.println("Report wrote: "+htmlFile.getCanonicalPath());
    }

    private String writeHtml(UseCase subUseCase) {

        String url = "./";
        for (int i = 1; i < useCaseResult.useCase.classUnderTest.getCanonicalName().split("\\.").length; i++) {
            url += "../";
        }

        url += subUseCase.classUnderTest.getCanonicalName().replace(".", "/") + ".html";
        return url;
    }


}
