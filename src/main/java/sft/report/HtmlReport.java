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

    public String useCaseTemplate =
            "<html>\n" +
                    "  <head>\n" +
                    "    <title>@@@useCase.name@@@</title>\n" +
                    "@@@useCase.css@@@" +
                    "@@@useCase.js@@@" +
                    "  </head>\n" +
                    "  <body class=\"useCase @@@useCase.issue@@@\">\n" +
                    "    <div class=\"container\">\n" +
                    "      <div class=\"page-header\">\n" +
                    "        <div class=\"text-center\">\n" +
                    "          <h1><span class=\"useCaseName\">@@@useCase.name@@@</span></h1>\n" +
                    "        </div>\n" +
                    "@@@useCaseCommentTemplate@@@" +
                    "      </div>\n" +
                    "@@@beforeUseCaseTemplate@@@" +
                    "@@@scenarioTemplates@@@" +
                    "@@@afterUseCaseTemplate@@@" +
                    "@@@relatedUseCasesTemplates@@@" +
                    "    </div>\n" +
                    "  </body>\n" +
                    "</html>";
    public String useCaseCommentTemplate =
            "        <div class=\"comment\">\n" +
                    "@@@comment.text@@@" +
                    "        </div>\n";
    public String beforeUseCaseTemplate =
            "      <div class=\"panel panel-default beforeUseCase @@@beforeUseCase.issue@@@\">\n" +
                    "        <div class=\"panel-body\">\n" +
                    "@@@contextInstructionTemplates@@@" +
                    "        </div>\n" +
                    "@@@exceptionTemplate@@@" +
                    "      </div>\n";
    public String scenarioTemplate =
            "      <div class=\"scenario @@@scenario.issue@@@ panel panel-default\">\n" +
                    "        <div class=\"panel-heading\">\n" +
                    "          <h3><span class=\"scenarioName\">@@@scenario.name@@@</span></h3>\n" +
                    "        </div>\n" +
                    "@@@scenarioCommentTemplate@@@" +
                    "@@@beforeScenarioTemplate@@@" +
                    "        <div class=\"panel-body\">\n" +
                    "@@@scenarioInstructionTemplates@@@" +
                    "        </div>\n" +
                    "@@@afterScenarioTemplate@@@" +
                    "@@@displayedContextsTemplates@@@" +
                    "@@@exceptionTemplate@@@" +
                    "      </div>\n";
    public String scenarioCommentTemplate=
            "        <div class=\"comment\">\n" +
                    "@@@comment.text@@@" +
                    "        </div>\n";
    public String exceptionTemplate =
            "      <div class=\"panel-body\">\n" +
                    "        <div class=\"exception\">\n" +
                    "          <a onClick=\"$(this).next().toggle()\" >@@@failure.className@@@: @@@failure.message@@@</a>\n" +
                    "          <pre class=\"stacktrace pre-scrollable\" >@@@failure.stacktrace@@@</pre>\n" +
                    "        </div>\n" +
                    "      </div>\n";
    public String beforeScenarioTemplate =
            "        <div class=\"beforeScenario panel-body\">\n" +
                    "@@@contextInstructionTemplates@@@" +
                    "          <hr/>\n" +
                    "        </div>";
    public String scenarioInstructionTemplate =
            "          <div class=\"instruction @@@instruction.issue@@@\">\n" +
                    "            <span>@@@instruction.text@@@</span>" +
                    "          </div>\n";
    public String afterScenarioTemplate =
            "        <div class=\"afterScenario panel-body\">\n" +
                    "          <hr/>\n " +
                    "@@@contextInstructionTemplates@@@" +
                    "        </div>";
    public String displayedContextsTemplate =
            "        <div class=\"displayableContext panel-body\">\n" +
                    "@@@displayedContextTemplates@@@" +
                    "        </div>\n";
    public String displayedContextTemplate =
            "        <div>\n" +
                    "@@@displayedContext.text@@@" +
                    "        </div>\n";
    public String afterUseCaseTemplate =
            "      <div class=\"panel panel-default afterUseCase @@@afterUseCase.issue@@@\">\n" +
                    "        <div class=\"panel-body\">\n" +
                    "@@@contextInstructionTemplates@@@" +
                    "        </div>\n" +
                    "@@@exceptionTemplate@@@" +
                    "      </div>\n";
    public String contextInstructionTemplate =
            "          <div>\n" +
                    "            <span>@@@instruction.text@@@</span>\n" +
                    "          </div>\n";
    public String relatedUseCasesTemplate =
            "      <div class=\"panel panel-default\">\n" +
                    "        <div class=\"panel-heading\">\n" +
                    "          <h3>Related uses cases</h3>\n" +
                    "        </div>\n" +
                    "        <div class=\"panel-body\">\n" +
                    "          <ul>\n" +
                    "@@@relatedUseCaseTemplates@@@" +
                    "          </ul>\n" +
                    "        </div>\n" +
                    "      </div>\n";
    public String relatedUseCaseTemplate =
            "            <li class=\"relatedUseCase @@@relatedUseCase.issue@@@\">\n" +
                    "              <a href=\"@@@relatedUseCase.link@@@\"><span>@@@relatedUseCase.name@@@</span></a>@@@relatedUseCaseErrorTemplate@@@\n" +
                    "            </li>\n";

    public String relatedUseCaseErrorTemplate="<div>@@@error.message@@@</div>";

    public String parameterTemplate = "<i class=\"value\">@@@parameter.value@@@</i>";

    public String ignoredClass = "ignored";
    public String failedClass = "failed";
    public String successClass = "succeeded";

    private DefaultConfiguration configuration;

    public HtmlReport(DefaultConfiguration configuration) {
        this.configuration = configuration;
        setResourcePath(HTML_DEPENDENCIES_FOLDER);
        setReportPath(configuration.getTargetFolder().path);
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

        String useCaseReport = new TemplateString(useCaseTemplate)
                .replace("@@@useCase.name@@@", useCaseResult.useCase.getName())
                .replace("@@@useCase.css@@@", htmlResources.getIncludeCssDirectives(classUnderTest))
                .replace("@@@useCase.js@@@", htmlResources.getIncludeJsDirectives(classUnderTest))
                .replace("@@@useCase.issue@@@", htmlResources.convertIssue(useCaseResult.getIssue()))
                .replace("@@@useCaseCommentTemplate@@@", getUseCaseComment(useCaseResult.useCase))
                .replace("@@@beforeUseCaseTemplate@@@", getBeforeUseCase(useCaseResult))
                .replace("@@@scenarioTemplates@@@", getScenarios(useCaseResult))
                .replace("@@@afterUseCaseTemplate@@@", getAfterUseCase(useCaseResult))
                .replace("@@@relatedUseCasesTemplates@@@", getRelatedUseCases(useCaseResult))
                .getText();
        if(useCaseResult.useCase.useCaseDecorator != null ){
            useCaseReport=useCaseResult.useCase.useCaseDecorator.applyOnUseCase(useCaseReport);
        }

        File htmlFile = configuration.getTargetFolder().createFileFromClass(classUnderTest, ".html");
        Writer html = new OutputStreamWriter(new FileOutputStream(htmlFile));
        html.write(useCaseReport);
        html.close();
        System.out.println("Report wrote: " + htmlFile.getCanonicalPath());
    }

    private String getRelatedUseCases(UseCaseResult useCaseResult) {
        String relatedUseCases = "";
        if (!useCaseResult.subUseCaseResults.isEmpty()) {
            return new TemplateString(relatedUseCasesTemplate)
                    .replace("@@@relatedUseCaseTemplates@@@", getRelatedUseCase(useCaseResult))
                    .getText();
        }
        return relatedUseCases;
    }

    private String getRelatedUseCase(UseCaseResult useCaseResult) {
        String relatedUseCase = "";
        for (UseCaseResult subUseCaseResult : useCaseResult.subUseCaseResults) {
            String error = createHtmlReportAndReturnErrorWhileCreating(subUseCaseResult);
            relatedUseCase += new TemplateString(relatedUseCaseTemplate)
                    .replace("@@@relatedUseCase.issue@@@", htmlResources.convertIssue(subUseCaseResult.getIssue()))
                    .replace("@@@relatedUseCase.link@@@", getRelativeUrl(subUseCaseResult.useCase, useCaseResult))
                    .replace("@@@relatedUseCase.name@@@", subUseCaseResult.useCase.getName())
                    .replace("@@@relatedUseCaseErrorTemplate@@@", error)
                    .getText();
        }
        return relatedUseCase;
    }

    private String createHtmlReportAndReturnErrorWhileCreating(UseCaseResult subUseCaseResult) {
        try {
            report(subUseCaseResult);
            return "";
        } catch (Throwable t) {
            return getLinkError(t);
        }
    }

    private String getLinkError( Throwable t) {
        return new TemplateString(relatedUseCaseErrorTemplate).replace("@@@error.message@@@",t.getMessage()).getText();
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
        String scenarioHtml = new TemplateString(scenarioTemplate)
                .replace("@@@scenario.issue@@@", htmlResources.convertIssue(scenarioResult.issue))
                .replace("@@@scenario.name@@@", scenarioResult.scenario.getName())
                .replace("@@@scenarioCommentTemplate@@@", getScenarioComment(scenarioResult))
                .replace("@@@beforeScenarioTemplate@@@", getBeforeScenario(scenarioResult))
                .replace("@@@scenarioInstructionTemplates@@@", getScenarioInstructions(scenarioResult))
                .replace("@@@afterScenarioTemplate@@@", getAfterScenario(scenarioResult))
                .replace("@@@displayedContextsTemplates@@@", extractDisplayedContexts(scenarioResult))
                .replace("@@@exceptionTemplate@@@", getStackTrace(scenarioResult.failure))
                .getText();

        if(scenarioResult.scenario.decorator != null ){
            scenarioHtml = scenarioResult.scenario.decorator.applyOnScenario(scenarioHtml);

        }
        return scenarioHtml;
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

            String instructionHtml = new TemplateString(scenarioInstructionTemplate)
                    .replace("@@@instruction.issue@@@", htmlResources.convertIssue(issue))
                    .replace("@@@instruction.text@@@", instruction)
                    .getText();

            if(fixture.decorator != null ){
                instructionHtml = fixture.decorator.applyOnFixture(instructionHtml);
            }
            result += instructionHtml;
        }
        return result;
    }

    private String getBeforeScenario(ScenarioResult scenarioResult) {
        if (scenarioResult.scenario.useCase.beforeScenario != null) {
            String instructions = getContextInstructions(scenarioResult.scenario.useCase, scenarioResult.scenario.useCase.beforeScenario);
            return new TemplateString(beforeScenarioTemplate)
                    .replace("@@@contextInstructionTemplates@@@", instructions)
                    .getText();
        }
        return "";
    }

    private String getAfterScenario(ScenarioResult scenarioResult) {
        if (scenarioResult.scenario.useCase.afterScenario != null) {
            String instructions = getContextInstructions(scenarioResult.scenario.useCase, scenarioResult.scenario.useCase.afterScenario);
            return new TemplateString(afterScenarioTemplate)
                    .replace("@@@contextInstructionTemplates@@@", instructions)
                    .getText();
        }
        return "";
    }

    private String getScenarioComment(ScenarioResult scenarioResult) {
        String comment = "";
        if (scenarioResult.scenario.getComment() != null) {
            comment = new TemplateString(scenarioCommentTemplate)
                    .replace("@@@comment.text@@@", scenarioResult.scenario.getComment())
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
                    .replace("@@@displayedContextTemplates@@@", htmlText)
                    .getText();
        }
        return "";
    }

    private String getStackTrace(Throwable failure) {
        if (failure != null) {
            StringWriter stringWriter = new StringWriter();
            failure.printStackTrace(new PrintWriter(stringWriter));
            return new TemplateString(exceptionTemplate)
                    .replace("@@@failure.className@@@", failure.getClass().getSimpleName())
                    .replace("@@@failure.message@@@", failure.getMessage())
                    .replace("@@@failure.stacktrace@@@", stringWriter.toString())
                    .getText();
        }
        return "";
    }

    private String extractDisplayedContext(String value) {
        return new TemplateString(displayedContextTemplate)
                .replace("@@@displayedContext.text@@@", value)
                .getText();
    }

    private String getBeforeUseCase(UseCaseResult useCaseResult) {
        UseCase useCase = useCaseResult.useCase;
        if (useCase.beforeUseCase != null) {
            return new TemplateString(beforeUseCaseTemplate)
                    .replace("@@@beforeUseCase.issue@@@", htmlResources.convertIssue(useCaseResult.beforeResult.issue))
                    .replace("@@@contextInstructionTemplates@@@", getContextInstructions(useCase, useCase.beforeUseCase))
                    .replace("@@@exceptionTemplate@@@", getStackTrace(useCaseResult.beforeResult))
                    .getText();
        }else {
            return "";
        }
    }

    private String getAfterUseCase(UseCaseResult useCaseResult) throws IOException {
        UseCase useCase = useCaseResult.useCase;
        if (useCase.afterUseCase != null) {
            return new TemplateString(afterUseCaseTemplate)
                    .replace("@@@afterUseCase.issue@@@", htmlResources.convertIssue(useCaseResult.afterResult.issue))
                    .replace("@@@contextInstructionTemplates@@@", getContextInstructions(useCase, useCase.afterUseCase))
                    .replace("@@@exceptionTemplate@@@", getStackTrace(useCaseResult.afterResult))
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
            instructions += new TemplateString(contextInstructionTemplate)
                    .replace("@@@instruction.text@@@", getInstructionWithParameter(methodCall, useCase))
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
                .replace("@@@parameter.value@@@", value)
                .getText();
    }

    private String getUseCaseComment(UseCase useCase) {
        String comment = "";
        if (useCase.haveComment()) {
            return new TemplateString(useCaseCommentTemplate)
                    .replace("@@@comment.text@@@", useCase.getComment())
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

    public String getIssueConverter(Issue issue) {
        switch (issue){
            case IGNORED:
                return ignoredClass;
            case FAILED:
                return failedClass;
            default:
            case SUCCEEDED:
                return successClass;
        }
    }
}