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


import sft.*;
import sft.result.ScenarioResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.regex.Matcher;

public class ScenarioHtml {

    private final HtmlResources htmlResources;
    private final UseCase useCase;
    private final Writer htmlWriter;
    private final Scenario scenario;
    private final ScenarioResult scenarioResult;
    private final ContextHandler after;
    private final ContextHandler before;

    private String scenarioTemplate =
        "<div class=\"scenario @@@scenario.issue@@@ panel panel-default\">"+
            "<div class=\"panel-heading\"><h3><span class=\"scenarioName\">@@@scenario.name@@@</span></h3></div>\n"+
            "@@@scenario.comment@@@"      +
            "@@@scenario.before@@@" +
            "<div class=\"panel-body\">\n"+
                "@@@scenario.instructions@@@" +
            "</div>\n"      +
            "@@@scenario.after@@@" +
            "@@@scenario.context@@@"+
            "@@@scenario.exception@@@"+
        "</div>\n";

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

        String scenarioH = scenarioTemplate.replaceAll("@@@scenario.issue@@@",htmlResources.convertIssue(scenarioResult.issue)) ;
        scenarioH = scenarioH.replaceAll("@@@scenario.name@@@",scenarioResult.scenario.getName()) ;
        scenarioH = scenarioH.replaceAll("@@@scenario.comment@@@", getComment()) ;
        scenarioH = scenarioH.replaceAll("@@@scenario.before@@@", getBeforeScenario()) ;

        String instructions= "";
        if (scenarioResult.beforeScenarioFailed()) {
            instructions += getScenarioCalls(Issue.IGNORED);
        } else if (scenarioResult.afterScenarioFailed()) {
            instructions += getScenarioCalls(Issue.SUCCEEDED);
        } else if (scenarioResult.issue == Issue.FAILED) {
            instructions += getTestFailed();
        } else {
            instructions += getScenarioCalls(scenarioResult.issue);
        }
        scenarioH = scenarioH.replaceAll("@@@scenario\\.instructions@@@",instructions) ;



        String afterScenario = "";
        if (useCase.afterScenario != null) {
            afterScenario += "<div class=\"afterScenario panel-body\"><hr/>";

            for (MethodCall methodCall : after.methodCalls) {
                Fixture fixture = useCase.getFixtureByMethodName(methodCall.name);
                afterScenario += "<div><span>" + fixture.getText(fixture.parametersName, methodCall.parameters) + "</span></div>\n";
            }
            afterScenario += "</div>";
        }
        scenarioH = scenarioH.replaceAll("@@@scenario\\.after@@@",afterScenario) ;
        scenarioH = scenarioH.replaceAll("@@@scenario\\.context@@@",extractDisplayedContext()) ;
        scenarioH = scenarioH.replaceAll("@@@scenario\\.exception@@@", Matcher.quoteReplacement(getStacktrace())) ;

        htmlWriter.write(scenarioH);
    }

    private String beforeScenario = "<div class=\"beforeScenario panel-body\">";
    private String getBeforeScenario() {
        String beforeScenario = "";
        if (useCase.beforeScenario != null) {
            beforeScenario += "<div class=\"beforeScenario panel-body\">";
            for (MethodCall methodCall : before.methodCalls) {
                Fixture fixture = useCase.getFixtureByMethodName(methodCall.name);
                beforeScenario += "<div><span>" + fixture.getText(fixture.parametersName, methodCall.parameters) + "</span></div>\n";
            }
            beforeScenario += "<hr/></div>";
        }
        return beforeScenario;
    }

    private String scenarioComment = "<div class=\"comment\">@@@scenario.comment.value@@@</div>";
    private String getComment() {
        String comment="";
        if (scenario.getComment() != null) {
            comment= scenarioComment.replaceAll("@@@scenario\\.comment\\.value@@@", scenario.getComment());
        }
        return comment;
    }

    private String getTestFailed() {
        String result = "";
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

            result += "<div class=\"instruction " + htmlResources.convertIssue(testIssue) + "\"><span>" + fixture.getText(fixture.parametersName, testFixture.parameters) + "</span></div>\n";
        }
        return result;
    }

    private String getStacktrace() {
        String result = "";
        Throwable failure = scenarioResult.failure;
        if (failure != null) {
            result += "<div class=\"panel-body\"><div class=\"exception\"><a onClick=\"$(this).next().toggle()\" >" + failure.getClass().getSimpleName() + ": " + failure.getMessage() + "</a>" +
                    "<pre class=\"stacktrace pre-scrollable\" >";
            StringWriter stringWriter = new StringWriter();
            failure.printStackTrace(new PrintWriter(stringWriter));
            result += stringWriter.toString();
            result += failure.toString();
            result += "</pre></div></div>";
        }
        return result;
    }

    private String extractDisplayedContext() {
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

    private String getScenarioCalls(Issue issue) {
        String result = "";
        for (MethodCall testFixture : scenario.methodCalls) {
            Fixture fixture = useCase.getFixtureByMethodName(testFixture.name);
            result += "<div class=\"instruction " + htmlResources.convertIssue(issue) + "\"><span>" + fixture.getText(fixture.parametersName, testFixture.parameters) + "</span></div>\n";
        }
        return result;
    }
}
