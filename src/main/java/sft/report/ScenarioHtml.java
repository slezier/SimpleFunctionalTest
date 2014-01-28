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
import sft.MethodCall;
import sft.Scenario;
import sft.UseCase;
import sft.result.ScenarioResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
    private TemplateString scenarioTemplate =new TemplateString(
            "<div class=\"scenario @@@scenario.issue@@@ panel panel-default\">" +
                    "<div class=\"panel-heading\"><h3><span class=\"scenarioName\">@@@scenario.name@@@</span></h3></div>\n" +
                    "@@@scenario.comment@@@" +
                    "@@@scenario.before@@@" +
                    "<div class=\"panel-body\">\n" +
                    "@@@scenario.instructions@@@" +
                    "</div>\n" +
                    "@@@scenario.after@@@" +
                    "@@@scenario.context@@@" +
                    "@@@scenario.exception@@@" +
                    "</div>\n");
    private TemplateString scenarioComment = new TemplateString("<div class=\"comment\">@@@scenario.comment.value@@@</div>");
    private TemplateString beforeScenario = new TemplateString("<div class=\"beforeScenario panel-body\">@@@scenario.before.instructions@@@<hr/></div>");
    private TemplateString contentInstruction = new TemplateString("<div><span>@@@content.instruction@@@</span></div>");
    private TemplateString scenarioInstruction = new TemplateString("<div class=\"instruction @@@instruction.issue@@@\"><span>@@@instruction.text@@@</span></div>\n");
    private TemplateString afterScenario = new TemplateString("<div class=\"afterScenario panel-body\"><hr/>@@@scenario.after.instructions@@@</div>");
    private TemplateString displayedContexts = new TemplateString("<div class=\"displayableContext panel-body\">@@@displayedContexts@@@</div>");
    private TemplateString displayedContext = new TemplateString("<div>@@@displayedContext@@@</div>");
    private TemplateString stacktrace = new TemplateString("<div class=\"panel-body\"><div class=\"exception\"><a onClick=\"$(this).next().toggle()\" >@@@failure.className@@@: @@@failure.message@@@</a>" +
            "<pre class=\"stacktrace pre-scrollable\" >@@@failure.stacktrace@@@</pre></div></div>");

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
        htmlWriter.write(scenarioTemplate
                .replace("@@@scenario.issue@@@", htmlResources.convertIssue(scenarioResult.issue))
                .replace("@@@scenario.name@@@", scenarioResult.scenario.getName())
                .replace("@@@scenario.comment@@@", getComment())
                .replace("@@@scenario.before@@@", getBeforeScenario())
                .replace("@@@scenario.instructions@@@", getScenarioInstructions())
                .replace("@@@scenario.after@@@", getAfterScenario())
                .replace("@@@scenario.context@@@", extractDisplayedContexts())
                .replace("@@@scenario.exception@@@", getStacktrace())
                .getText());
    }

    private String getScenarioInstructions() {
        Issue lastIssue;
        if (scenarioResult.beforeScenarioFailed() || scenarioResult.issue == Issue.IGNORED) {
            lastIssue = Issue.IGNORED;
        } else {
            lastIssue = Issue.SUCCEEDED;
        }

        String result = "";
        for (MethodCall testFixture : scenario.methodCalls) {
            Fixture fixture = useCase.getFixtureByMethodName(testFixture.name);
            final Issue issue;
            if (lastIssue == Issue.SUCCEEDED && scenarioResult.failureOccurs(fixture, testFixture)) {
                issue = Issue.FAILED;
                lastIssue = Issue.IGNORED;
            } else {
                issue = lastIssue;
            }
            result += scenarioInstruction.replace("@@@instruction.issue@@@", htmlResources.convertIssue(issue))
                    .replace("@@@instruction.text@@@", fixture.getText(fixture.parametersName, testFixture.parameters))
                    .getText();
        }
        return result;
    }

    private String getBeforeScenario() {
        if (useCase.beforeScenario != null) {
            String instructions = "";
            for (MethodCall methodCall : before.methodCalls) {
                instructions += getContentInstruction(methodCall);
            }
            return beforeScenario.replace("@@@scenario.before.instructions@@@", instructions).getText();
        }
        return "";
    }

    private String getAfterScenario() {
        if (useCase.afterScenario != null) {
            String instructions = "";
            for (MethodCall methodCall : after.methodCalls) {
                instructions += getContentInstruction(methodCall);
            }
            return afterScenario.replace("@@@scenario\\.after\\.instructions@@@", instructions).getText();
        }
        return "";
    }

    private String getContentInstruction(MethodCall methodCall) {
        Fixture fixture = useCase.getFixtureByMethodName(methodCall.name);
        return contentInstruction.replace("@@@content\\.instruction@@@", fixture.getText(fixture.parametersName, methodCall.parameters)).getText();
    }

    private String getComment() {
        String comment = "";
        if (scenario.getComment() != null) {
            comment = scenarioComment.replace("@@@scenario\\.comment\\.value@@@", scenario.getComment()).getText();
        }
        return comment;
    }

    private String getStacktrace() {
        Throwable failure = scenarioResult.failure;
        if (failure != null) {
            StringWriter stringWriter = new StringWriter();
            failure.printStackTrace(new PrintWriter(stringWriter));
            return stacktrace.replace("@@@failure.className@@@", failure.getClass().getSimpleName())
                    .replace( "@@@failure.message@@@", failure.getMessage())
                    .replace("@@@failure.stacktrace@@@", stringWriter.toString()).getText();
        }
        return "";
    }

    private String extractDisplayedContexts() {
        List<String> values = scenarioResult.contextToDisplay;
        if (!values.isEmpty()) {
            String htmlText = "";
            for (String value : values) {
                htmlText += extractDisplayedContext(value);
            }
            return displayedContexts.replace("@@@displayedContexts@@@", htmlText).getText();
        }
        return "";
    }

    private String extractDisplayedContext(String value) {
        return displayedContext.replace("@@@displayedContext@@@", value).getText();
    }

}
