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


import sft.ContextHandler;
import sft.Fixture;
import sft.Scenario;
import sft.UseCase;
import sft.MethodCall;
import sft.result.ScenarioResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

public class ScenarioHtml {

    private final HtmlResources htmlResources;
    private final UseCase useCase;
    private final Writer htmlWriter;
    private final Scenario scenario;
    private final ScenarioResult scenarioResult;
    private final ContextHandler after;
    private final ContextHandler before;

    public ScenarioHtml(HtmlResources htmlResources, UseCase useCase, Writer htmlWriter, Scenario scenario, ScenarioResult scenarioResult, ContextHandler before, ContextHandler after) {
        this.useCase = useCase;
        this.htmlWriter = htmlWriter;
        this.scenario = scenario;
        this.scenarioResult = scenarioResult;
        this.htmlResources = htmlResources;
        this.before = before;
        this.after = after;
    }

    public void write() throws IOException, IllegalAccessException {
        htmlWriter.write("<div class=\"scenario " + htmlResources.convertIssue(scenarioResult.issue) + " panel panel-default\">");
        htmlWriter.write("<div class=\"panel-heading\"><h3><span class=\"scenarioName\">" + scenarioResult.scenario.getName() + "</span></h3></div>\n");


        if (scenario.getComment() != null) {
            htmlWriter.write("<div class=\"comment\">" + scenario.getComment() + "</div>");
        }
        if (useCase.beforeScenario != null) {
            htmlWriter.write("<div class=\"beforeScenario panel-body\">");
            for (MethodCall methodCall : before.methodCalls) {
                Fixture fixture = useCase.getFixtureByMethodName(methodCall.name);
                htmlWriter.write("<div><span>" + fixture.getText(fixture.parametersName, methodCall.parameters) + "</span></div>\n");
            }
            htmlWriter.write("<hr/></div>");
        }

        htmlWriter.write("<div class=\"panel-body\">\n");
        if(scenarioResult.beforeScenarioFailed()){
            writeScenarioCalls(Issue.IGNORED);
        }else if(scenarioResult.afterScenarioFailed()){
                writeScenarioCalls(Issue.SUCCEEDED);
        }else if (scenarioResult.issue == Issue.FAILED) {
            writeTestFailed();
        } else {
            writeScenarioCalls(scenarioResult.issue);
        }
        htmlWriter.write("</div>\n");

        if (useCase.afterScenario != null) {
            htmlWriter.write("<div class=\"afterScenario panel-body\"><hr/>");

            for (MethodCall methodCall : after.methodCalls) {
                Fixture fixture = useCase.getFixtureByMethodName(methodCall.name);
                htmlWriter.write("<div><span>" + fixture.getText(fixture.parametersName, methodCall.parameters) + "</span></div>\n");
            }
            htmlWriter.write("</div>");
        }

        htmlWriter.write(extractDisplayedContext());

        htmlWriter.write("</div>\n");
    }

    private void writeTestFailed() throws IOException, IllegalAccessException {
        Fixture failedCall = scenarioResult.getFailedCall();

        boolean failureAppend = false;

        for (MethodCall testFixture : scenario.methodCalls) {
            Fixture fixture = useCase.getFixtureByMethodName(testFixture.name);

            Issue testIssue;
            if (fixture == failedCall && scenarioResult.getFailedLine() == testFixture.line) {
                failureAppend = true;
                testIssue = Issue.FAILED;
            } else if (!failureAppend) {
                testIssue = Issue.SUCCEEDED;
            } else {
                testIssue = Issue.IGNORED;
            }

            htmlWriter.write("<div class=\"instruction " + htmlResources.convertIssue(testIssue) + "\"><span>" + fixture.getText(fixture.parametersName, testFixture.parameters) + "</span></div>\n");
        }

        Throwable failure = scenarioResult.failure;
        htmlWriter.write("<div class=\"exception\"><a onClick=\"$(this).next().toggle()\" >" + failure.getClass().getSimpleName() + ": " + failure.getMessage() + "</a>" +
                "<pre class=\"stacktrace pre-scrollable\" >");
        PrintWriter printWriter = new PrintWriter(htmlWriter);
        failure.printStackTrace(printWriter);
        htmlWriter.write("</pre></div>");
    }

    private String extractDisplayedContext() throws IllegalAccessException {
        List<String> values = scenarioResult.contextToDisplay;
        if (!values.isEmpty()) {
            String htmlText = "<div class=\"displayableContext panel-body\">";
            for (String value : values) {
                htmlText += "<div>" + value + "</div>";

            }
            htmlText += "</div>";
            return htmlText;
        }
        return "";
    }

    private void writeScenarioCalls(Issue issue) throws IOException {
        for (MethodCall testFixture : scenario.methodCalls) {
            Fixture fixture = useCase.getFixtureByMethodName(testFixture.name);
            htmlWriter.write("<div class=\"instruction " + htmlResources.convertIssue(issue) + "\"><span>" + fixture.getText(fixture.parametersName, testFixture.parameters) + "</span></div>\n");
        }
    }


}
