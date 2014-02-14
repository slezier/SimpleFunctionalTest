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
import sft.DefaultConfiguration;
import sft.Fixture;
import sft.MethodCall;
import sft.Report;
import sft.Scenario;
import sft.UseCase;
import sft.result.ContextResult;
import sft.result.Issue;
import sft.result.ScenarioResult;
import sft.result.UseCaseResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.regex.Matcher;


public class HtmlReport extends Report {
    public static final String HTML_DEPENDENCIES_FOLDER = "sft-html-default";
    private HtmlResources htmlResources;

    private String useCaseTemplate =
            "<html>\n" +
                    "  <head>\n" +
                    "    <title>@@@name@@@</title>\n" +
                    "@@@include.css@@@" +
                    "@@@include.js@@@" +
                    "  </head>\n" +
                    "  <body class=\"useCase @@@useCase.issue@@@\">\n" +
                    "    <div class=\"container\">\n" +
                    "      <div class=\"page-header\">\n" +
                    "        <div class=\"text-center\">\n" +
                    "          <h1><span class=\"useCaseName\">@@@name@@@</span></h1>\n" +
                    "        </div>\n" +
                    "@@@useCase.comment@@@" +
                    "      </div>\n" +
                    "@@@beforeUseCase@@@" +
                    "@@@scenarios@@@" +
                    "@@@afterUseCase@@@" +
                    "@@@relatedUseCases@@@" +
                    "    </div>\n" +
                    "  </body>\n" +
                    "</html>";
    private String commentTemplate =
            "        <div class=\"comment\">\n" +
                    "@@@comment@@@" +
                    "        </div>\n";
    private String beforeUseCaseTemplate =
            "      <div class=\"panel panel-default beforeUseCase @@@useCase.before.issue@@@\">\n" +
                    "        <div class=\"panel-body\">\n" +
                    "@@@instructions@@@" +
                    "        </div>\n" +
                    "@@@exception@@@" +
                    "      </div>\n";
    private String scenarioTemplate =
            "      <div class=\"scenario @@@scenario.issue@@@ panel panel-default\">\n" +
                    "        <div class=\"panel-heading\">\n" +
                    "          <h3><span class=\"scenarioName\">@@@scenario.name@@@</span></h3>\n" +
                    "        </div>\n" +
                    "@@@scenario.comment@@@" +
                    "@@@scenario.before@@@" +
                    "        <div class=\"panel-body\">\n" +
                    "@@@scenario.instructions@@@" +
                    "        </div>\n" +
                    "@@@scenario.after@@@" +
                    "@@@scenario.context@@@" +
                    "@@@scenario.exception@@@" +
                    "      </div>\n";
    private String stacktraceTemplate =
            "      <div class=\"panel-body\">\n" +
                    "        <div class=\"exception\">\n" +
                    "          <a onClick=\"$(this).next().toggle()\" >@@@failure.className@@@: @@@failure.message@@@</a>\n" +
                    "          <pre class=\"stacktrace pre-scrollable\" >@@@failure.stacktrace@@@</pre>\n" +
                    "        </div>\n" +
                    "      </div>\n";
    private String beforeScenarioTemplate =
            "        <div class=\"beforeScenario panel-body\">\n" +
                    "@@@scenario.before.instructions@@@" +
                    "          <hr/>\n" +
                    "        </div>";
    private String scenarioInstructionTemplate =
            "          <div class=\"instruction @@@instruction.issue@@@\">\n" +
                    "            <span>@@@instruction.text@@@</span>" +
                    "          </div>\n";
    private String afterScenarioTemplate =
            "        <div class=\"afterScenario panel-body\">\n" +
                    "          <hr/>\n " +
                    "@@@scenario.after.instructions@@@" +
                    "        </div>";
    private String displayedContextsTemplate =
            "        <div class=\"displayableContext panel-body\">\n" +
                    "@@@displayedContexts@@@" +
                    "        </div>\n";
    private String displayedContextTemplate =
            "        <div>\n" +
                    "@@@displayedContext@@@" +
                    "        </div>\n";
    private String afterUseCaseTemplate =
            "      <div class=\"panel panel-default afterUseCase @@@useCase.after.issue@@@\">\n" +
                    "        <div class=\"panel-body\">\n" +
                    "@@@instructions@@@" +
                    "        </div>\n" +
                    "@@@exception@@@" +
                    "      </div>\n";
    private String useCaseContextInstructionsTemplate =
            "          <div>\n" +
                    "            <span>@@@instructions@@@</span>\n" +
                    "          </div>\n";
    private String relatedUseCasesTemplate =
            "      <div class=\"panel panel-default\">\n" +
                    "        <div class=\"panel-heading\">\n" +
                    "          <h3>Related uses cases</h3>\n" +
                    "        </div>\n" +
                    "        <div class=\"panel-body\">\n" +
                    "          <ul>\n" +
                    "@@@relatedUseCases@@@" +
                    "          </ul>\n" +
                    "        </div>\n" +
                    "      </div>\n";
    private String relatedUseCaseTemplate =
            "            <li class=\"relatedUseCase @@@issue@@@\">\n" +
                    "              <a href=\"@@@link@@@\"><span>@@@name@@@</span></a>@@@error@@@\n" +
                    "            </li>\n";
    private String parameterTemplate = "<i class=\"value\">@@@value@@@</i>";
    private DefaultConfiguration configuration;

    public HtmlReport(DefaultConfiguration configuration) {
        this.configuration = configuration;
        setResourcePath(HTML_DEPENDENCIES_FOLDER);
        setReportPath(configuration.getTargetFolder().path);
    }

    public String getUseCaseTemplate() {
        return useCaseTemplate;
    }

    public void setUseCaseTemplate(String useCaseTemplate) {
        this.useCaseTemplate = useCaseTemplate;
    }

    public String getCommentTemplate() {
        return commentTemplate;
    }

    public void setCommentTemplate(String commentTemplate) {
        this.commentTemplate = commentTemplate;
    }

    public String getBeforeUseCaseTemplate() {
        return beforeUseCaseTemplate;
    }

    public void setBeforeUseCaseTemplate(String beforeUseCaseTemplate) {
        this.beforeUseCaseTemplate = beforeUseCaseTemplate;
    }

    public String getScenarioTemplate() {
        return scenarioTemplate;
    }

    public void setScenarioTemplate(String scenarioTemplate) {
        this.scenarioTemplate = scenarioTemplate;
    }

    public String getStacktraceTemplate() {
        return stacktraceTemplate;
    }

    public void setStacktraceTemplate(String stacktraceTemplate) {
        this.stacktraceTemplate = stacktraceTemplate;
    }

    public String getBeforeScenarioTemplate() {
        return beforeScenarioTemplate;
    }

    public void setBeforeScenarioTemplate(String beforeScenarioTemplate) {
        this.beforeScenarioTemplate = beforeScenarioTemplate;
    }

    public String getScenarioInstructionTemplate() {
        return scenarioInstructionTemplate;
    }

    public void setScenarioInstructionTemplate(String scenarioInstructionTemplate) {
        this.scenarioInstructionTemplate = scenarioInstructionTemplate;
    }

    public String getAfterScenarioTemplate() {
        return afterScenarioTemplate;
    }

    public void setAfterScenarioTemplate(String afterScenarioTemplate) {
        this.afterScenarioTemplate = afterScenarioTemplate;
    }

    public String getDisplayedContextsTemplate() {
        return displayedContextsTemplate;
    }

    public void setDisplayedContextsTemplate(String displayedContextsTemplate) {
        this.displayedContextsTemplate = displayedContextsTemplate;
    }

    public String getDisplayedContextTemplate() {
        return displayedContextTemplate;
    }

    public void setDisplayedContextTemplate(String displayedContextTemplate) {
        this.displayedContextTemplate = displayedContextTemplate;
    }

    public String getAfterUseCaseTemplate() {
        return afterUseCaseTemplate;
    }

    public void setAfterUseCaseTemplate(String afterUseCaseTemplate) {
        this.afterUseCaseTemplate = afterUseCaseTemplate;
    }

    public String getUseCaseContextInstructionsTemplate() {
        return useCaseContextInstructionsTemplate;
    }

    public void setUseCaseContextInstructionsTemplate(String useCaseContextInstructionsTemplate) {
        this.useCaseContextInstructionsTemplate = useCaseContextInstructionsTemplate;
    }

    public String getRelatedUseCasesTemplate() {
        return relatedUseCasesTemplate;
    }

    public void setRelatedUseCasesTemplate(String relatedUseCasesTemplate) {
        this.relatedUseCasesTemplate = relatedUseCasesTemplate;
    }

    public String getRelatedUseCaseTemplate() {
        return relatedUseCaseTemplate;
    }

    public void setRelatedUseCaseTemplate(String relatedUseCaseTemplate) {
        this.relatedUseCaseTemplate = relatedUseCaseTemplate;
    }

    public String getParameterTemplate() {
        return parameterTemplate;
    }

    public void setParameterTemplate(String parameterTemplate) {
        this.parameterTemplate = parameterTemplate;
    }

    @Override
    public void setReportPath(String reportPath) {
        if (reportPath != null && !reportPath.equals(getReportPath())) {
            super.setReportPath(reportPath);
            configuration.setTargetPath(reportPath);
        }
    }

    @Override
    public void setResourcePath(String resourcePath) {
        if (resourcePath != null && !resourcePath.equals(getResourcePath())) {
            super.setResourcePath(resourcePath);
            htmlResources = new HtmlResources(configuration, resourcePath);
        }
    }

    public void report(UseCaseResult useCaseResult) throws IOException, IllegalAccessException {
        Class<?> classUnderTest = useCaseResult.useCase.classUnderTest;

        TemplateString useCaseReport = new TemplateString(useCaseTemplate)
                .replace("@@@name@@@", useCaseResult.useCase.getName())
                .replace("@@@include.css@@@", htmlResources.getIncludeCssDirectives(classUnderTest))
                .replace("@@@include.js@@@", htmlResources.getIncludeJsDirectives(classUnderTest))
                .replace("@@@useCase.issue@@@", htmlResources.convertIssue(useCaseResult.getIssue()))
                .replace("@@@useCase.comment@@@", getUseCaseComment(useCaseResult.useCase))
                .replace("@@@beforeUseCase@@@", getBeforeUseCase(useCaseResult))
                .replace("@@@scenarios@@@", getScenarios(useCaseResult))
                .replace("@@@afterUseCase@@@", getAfterUseCase(useCaseResult))
                .replace("@@@relatedUseCases@@@", getRelatedUseCases(useCaseResult));

        File htmlFile = configuration.getTargetFolder().createFileFromClass(classUnderTest, ".html");
        Writer html = new OutputStreamWriter(new FileOutputStream(htmlFile));
        html.write(useCaseReport.getText());
        html.close();
        System.out.println("Report wrote: " + htmlFile.getCanonicalPath());
    }

    private String getRelatedUseCases(UseCaseResult useCaseResult) {
        String relatedUseCases = "";
        if (!useCaseResult.subUseCaseResults.isEmpty()) {
            return new TemplateString(relatedUseCasesTemplate)
                    .replace("@@@relatedUseCases@@@", getRelatedUseCase(useCaseResult))
                    .getText();
        }
        return relatedUseCases;
    }

    private String getRelatedUseCase(UseCaseResult useCaseResult) {
        String relatedUseCase = "";
        for (UseCaseResult subUseCaseResult : useCaseResult.subUseCaseResults) {
            String error = createHtmlReportAndReturnErrorWhileCreating(relatedUseCase, subUseCaseResult);
            relatedUseCase += new TemplateString(relatedUseCaseTemplate)
                    .replace("@@@issue@@@", htmlResources.convertIssue(subUseCaseResult.getIssue()))
                    .replace("@@@link@@@", getRelativeUrl(subUseCaseResult.useCase, useCaseResult))
                    .replace("@@@name@@@", subUseCaseResult.useCase.getName())
                    .replace("@@@error@@@", error)
                    .getText();
        }
        return relatedUseCase;
    }

    private String createHtmlReportAndReturnErrorWhileCreating(String relatedUseCase, UseCaseResult subUseCaseResult) {
        try {
            report(subUseCaseResult);
            return "";
        } catch (Throwable t) {
            return getLinkError(relatedUseCase, t);
        }
    }

    private String getLinkError(String relatedUseCase, Throwable t) {
        relatedUseCase += "<div>" + t.getMessage() + "</div>";
        return relatedUseCase;
    }

    private String getScenarios(UseCaseResult useCaseResult) {
        String scenarioTxt = "";
        for (Scenario scenario : useCaseResult.useCase.scenarios) {
            for (ScenarioResult scenarioResult : useCaseResult.scenarioResults) {
                if (scenarioResult.scenario.equals(scenario)) {
                    scenarioTxt += getScenario(scenarioResult);
                }
            }
        }
        return scenarioTxt;
    }

    private String getScenario(ScenarioResult scenarioResult) {
        return new TemplateString(scenarioTemplate)
                .replace("@@@scenario.issue@@@", htmlResources.convertIssue(scenarioResult.issue))
                .replace("@@@scenario.name@@@", scenarioResult.scenario.getName())
                .replace("@@@scenario.comment@@@", getScenarioComment(scenarioResult))
                .replace("@@@scenario.before@@@", getBeforeScenario(scenarioResult))
                .replace("@@@scenario.instructions@@@", getScenarioInstructions(scenarioResult))
                .replace("@@@scenario.after@@@", getAfterScenario(scenarioResult))
                .replace("@@@scenario.context@@@", extractDisplayedContexts(scenarioResult))
                .replace("@@@scenario.exception@@@", getStackTrace(scenarioResult.failure))
                .getText();
    }

    private String getScenarioInstructions(ScenarioResult scenarioResult) {
        Issue lastIssue;
        if (scenarioResult.beforeScenarioFailed() || scenarioResult.issue == Issue.IGNORED) {
            lastIssue = Issue.IGNORED;
        } else {
            lastIssue = Issue.SUCCEEDED;
        }

        String result = "";
        for (MethodCall methodCall : scenarioResult.scenario.methodCalls) {
            Fixture fixture = scenarioResult.scenario.useCase.getFixtureByMethodName(methodCall.name);
            final Issue issue;
            if (lastIssue == Issue.SUCCEEDED && scenarioResult.failureOccurs(fixture, methodCall)) {
                issue = Issue.FAILED;
                lastIssue = Issue.IGNORED;
            } else {
                issue = lastIssue;
            }

            String instruction = getInstructionWithParameter(methodCall, scenarioResult.scenario.useCase);

            result += new TemplateString(scenarioInstructionTemplate)
                    .replace("@@@instruction.issue@@@", htmlResources.convertIssue(issue))
                    .replace("@@@instruction.text@@@", instruction)
                    .getText();
        }
        return result;
    }

    private String getBeforeScenario(ScenarioResult scenarioResult) {
        if (scenarioResult.scenario.useCase.beforeScenario != null) {
            String instructions = getContextInstructions(scenarioResult.scenario.useCase, scenarioResult.scenario.useCase.beforeScenario);
            return new TemplateString(beforeScenarioTemplate)
                    .replace("@@@scenario.before.instructions@@@", instructions)
                    .getText();
        }
        return "";
    }

    private String getAfterScenario(ScenarioResult scenarioResult) {
        if (scenarioResult.scenario.useCase.afterScenario != null) {
            String instructions = getContextInstructions(scenarioResult.scenario.useCase, scenarioResult.scenario.useCase.afterScenario);
            return new TemplateString(afterScenarioTemplate)
                    .replace("@@@scenario.after.instructions@@@", instructions)
                    .getText();
        }
        return "";
    }

    private String getScenarioComment(ScenarioResult scenarioResult) {
        String comment = "";
        if (scenarioResult.scenario.getComment() != null) {
            comment = new TemplateString(commentTemplate)
                    .replace("@@@comment@@@", scenarioResult.scenario.getComment())
                    .getText();
        }
        return comment;
    }

    private String extractDisplayedContexts(ScenarioResult scenarioResult) {
        List<String> values = scenarioResult.contextToDisplay;
        if (!values.isEmpty()) {
            String htmlText = "";
            for (String value : values) {
                htmlText += extractDisplayedContext(value);
            }
            return new TemplateString(displayedContextsTemplate)
                    .replace("@@@displayedContexts@@@", htmlText)
                    .getText();
        }
        return "";
    }

    private String getStackTrace(Throwable failure) {
        if (failure != null) {
            StringWriter stringWriter = new StringWriter();
            failure.printStackTrace(new PrintWriter(stringWriter));
            return new TemplateString(stacktraceTemplate)
                    .replace("@@@failure.className@@@", failure.getClass().getSimpleName())
                    .replace("@@@failure.message@@@", failure.getMessage())
                    .replace("@@@failure.stacktrace@@@", stringWriter.toString())
                    .getText();
        }
        return "";
    }

    private String extractDisplayedContext(String value) {
        return new TemplateString(displayedContextTemplate)
                .replace("@@@displayedContext@@@", value)
                .getText();
    }

    private String getBeforeUseCase(UseCaseResult useCaseResult) {
        UseCase useCase = useCaseResult.useCase;
        if (useCase.beforeUseCase != null) {
            return new TemplateString(beforeUseCaseTemplate)
                    .replace("@@@useCase.before.issue@@@", htmlResources.convertIssue(useCaseResult.beforeResult.issue))
                    .replace("@@@instructions@@@", getContextInstructions(useCase, useCase.beforeUseCase))
                    .replace("@@@exception@@@", getStackTrace(useCaseResult.beforeResult))
                    .getText();
        }else {
            return "";
        }
    }

    private String getAfterUseCase(UseCaseResult useCaseResult) throws IOException {
        UseCase useCase = useCaseResult.useCase;
        if (useCase.afterUseCase != null) {
            return new TemplateString(afterUseCaseTemplate)
                    .replace("@@@useCase.after.issue@@@", htmlResources.convertIssue(useCaseResult.afterResult.issue))
                    .replace("@@@instructions@@@", getContextInstructions(useCase, useCase.afterUseCase))
                    .replace("@@@exception@@@", getStackTrace(useCaseResult.afterResult))
                    .getText();
        }else{
            return "";
        }
    }

    private String getStackTrace(ContextResult contextResult) {
        if (contextResult.isSuccessful()) {
            return "";
        } else {
            return getStackTrace(contextResult.exception);
        }
    }

    private String getContextInstructions(UseCase useCase, ContextHandler context) {
        String instructions = "";
        for (MethodCall methodCall : context.methodCalls) {
            instructions += new TemplateString(useCaseContextInstructionsTemplate)
                    .replace("@@@instructions@@@", getInstructionWithParameter(methodCall, useCase))
                    .getText();
        }
        return instructions;
    }

    private String getInstructionWithParameter(MethodCall methodCall, UseCase useCase) {
        Fixture fixture = useCase.getFixtureByMethodName(methodCall.name);
        return getInstructionWithParameter(methodCall, fixture);
    }

    private String getInstructionWithParameter(MethodCall testFixture, Fixture fixture) {
        String instruction = fixture.getText();
        for (int index = 0; index < fixture.parametersName.size(); index++) {
            String name = fixture.parametersName.get(index);
            String value = Matcher.quoteReplacement(getParameter(testFixture.parameters.get(index)));
            instruction = instruction.replaceAll("\\$\\{" + name + "\\}", value).replaceAll("\\$\\{" + (index + 1) + "\\}", value);
        }
        return instruction;
    }

    private String getParameter(String value) {
        return new TemplateString(parameterTemplate)
                .replace("@@@value@@@", value)
                .getText();
    }

    private String getUseCaseComment(UseCase useCase) {
        String comment = "";
        if (useCase.haveComment()) {
            return new TemplateString(commentTemplate)
                    .replace("@@@comment@@@", useCase.getComment())
                    .getText();
        }
        return comment;
    }

    private String getRelativeUrl(UseCase subUseCase, UseCaseResult useCaseResult) {
        RelativeHtmlPathResolver pathResolver = new RelativeHtmlPathResolver();
        String source = pathResolver.getPathOf(useCaseResult.useCase.classUnderTest, ".html");
        String target = pathResolver.getPathOf(subUseCase.classUnderTest, ".html");
        return pathResolver.getRelativePathToFile(source, target);
    }

    public HtmlResources getHtmlResources() {
        return htmlResources;
    }
}