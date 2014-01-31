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


import sft.Fixture;
import sft.MethodCall;
import sft.result.ScenarioResult;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.regex.Matcher;

public class ScenarioHtml {

    private final HtmlResources htmlResources = new HtmlResources();
    private TemplateString contentInstruction = new TemplateString("<div><span>@@@content.instruction@@@</span></div>");
    private TemplateString displayedContexts = new TemplateString("<div class=\"displayableContext panel-body\">@@@displayedContexts@@@</div>");
    private TemplateString displayedContext = new TemplateString("<div>@@@displayedContext@@@</div>");
    private TemplateString stacktrace = new TemplateString("<div class=\"panel-body\"><div class=\"exception\"><a onClick=\"$(this).next().toggle()\" >@@@failure.className@@@: @@@failure.message@@@</a>" +
            "<pre class=\"stacktrace pre-scrollable\" >@@@failure.stacktrace@@@</pre></div></div>");
    private TemplateString parameter = new TemplateString("<i class=\"value\">@@@value@@@</i>");




    public String getInstructionWithParameter(MethodCall testFixture, Fixture fixture) {
        String instruction = fixture.getText();
        for (int index = 0; index < fixture.parametersName.size(); index++) {
            String name = fixture.parametersName.get(index);
            String value = Matcher.quoteReplacement(getParameter(testFixture.parameters.get(index)));
            instruction = instruction.replaceAll("\\$\\{" + name + "\\}", value).replaceAll("\\$\\{" + (index + 1) + "\\}", value);
        }
        return instruction;
    }


    public String getContentInstruction(ScenarioResult scenarioResult, MethodCall methodCall) {
        Fixture fixture = scenarioResult.scenario.useCase.getFixtureByMethodName(methodCall.name);
        String instruction = getInstructionWithParameter(methodCall, fixture);
        return contentInstruction.replace("@@@content\\.instruction@@@", instruction).getText();
    }

    public String getParameter(String value) {
        return parameter.replace("@@@value@@@", value).getText();
    }



    public String getStacktrace(ScenarioResult scenarioResult) {
        Throwable failure = scenarioResult.failure;
        if (failure != null) {
            StringWriter stringWriter = new StringWriter();
            failure.printStackTrace(new PrintWriter(stringWriter));
            return stacktrace.replace("@@@failure.className@@@", failure.getClass().getSimpleName())
                    .replace("@@@failure.message@@@", failure.getMessage())
                    .replace("@@@failure.stacktrace@@@", stringWriter.toString()).getText();
        }
        return "";
    }

    public String extractDisplayedContexts(ScenarioResult scenarioResult) {
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

    public String extractDisplayedContext(String value) {
        return displayedContext.replace("@@@displayedContext@@@", value).getText();
    }

}
