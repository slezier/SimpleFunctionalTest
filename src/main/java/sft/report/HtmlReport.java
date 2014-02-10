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
import sft.Report;
import sft.Scenario;
import sft.UseCase;
import sft.environment.FileSystem;
import sft.result.ContextResult;
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
    private HtmlResources htmlResources = new HtmlResources();
    private final FileSystem fileSystem = new FileSystem();
    private TemplateString useCaseTemplate = new TemplateString(
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
                    "</html>");
    private TemplateString commentTemplate = new TemplateString(
            "        <div class=\"comment\">\n" +
                    "@@@comment@@@" +
                    "        </div>\n");
    private TemplateString beforeUseCaseTemplate = new TemplateString(
            "      <div class=\"panel panel-default beforeUseCase @@@useCase.before.issue@@@\">\n" +
                    "        <div class=\"panel-body\">\n" +
                    "@@@instructions@@@" +
                    "        </div>\n" +
                    "@@@exception@@@" +
                    "      </div>\n");
    private TemplateString scenarioTemplate = new TemplateString(
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
                    "      </div>\n");
    private TemplateString stacktrace = new TemplateString(
            "      <div class=\"panel-body\">\n" +
                    "        <div class=\"exception\">\n" +
                    "          <a onClick=\"$(this).next().toggle()\" >@@@failure.className@@@: @@@failure.message@@@</a>\n" +
                    "          <pre class=\"stacktrace pre-scrollable\" >@@@failure.stacktrace@@@</pre>\n" +
                    "        </div>\n" +
                    "      </div>\n");
    private TemplateString beforeScenarioTemplate = new TemplateString(
            "        <div class=\"beforeScenario panel-body\">\n" +
                    "@@@scenario.before.instructions@@@" +
                    "          <hr/>\n" +
                    "        </div>");
    private TemplateString scenarioInstructionTemplate = new TemplateString(
            "          <div class=\"instruction @@@instruction.issue@@@\">\n" +
                    "            <span>@@@instruction.text@@@</span>" +
                    "          </div>\n");
    private TemplateString afterScenarioTemplate = new TemplateString(
            "        <div class=\"afterScenario panel-body\">\n" +
                    "          <hr/>\n " +
                    "@@@scenario.after.instructions@@@" +
                    "        </div>");
    private TemplateString displayedContexts = new TemplateString(
            "        <div class=\"displayableContext panel-body\">\n" +
                    "@@@displayedContexts@@@" +
                    "        </div>\n");
    private TemplateString displayedContext = new TemplateString(
            "        <div>\n" +
                    "@@@displayedContext@@@" +
                    "        </div>\n");
    private TemplateString afterUseCaseTemplate = new TemplateString(
            "      <div class=\"panel panel-default afterUseCase @@@useCase.after.issue@@@\">\n" +
                    "        <div class=\"panel-body\">\n" +
                    "@@@instructions@@@" +
                    "        </div>\n" +
                    "@@@exception@@@" +
                    "      </div>\n");
    private TemplateString useCaseContextInstructionsTemplate = new TemplateString(
            "          <div>\n" +
                    "            <span>@@@instructions@@@</span>\n" +
                    "          </div>\n");
    private TemplateString relatedUseCasesTemplate = new TemplateString(
            "      <div class=\"panel panel-default\">\n" +
                    "        <div class=\"panel-heading\">\n" +
                    "          <h3>Related uses cases</h3>\n" +
                    "        </div>\n" +
                    "        <div class=\"panel-body\">\n" +
                    "          <ul>\n" +
                    "@@@relatedUseCases@@@" +
                    "          </ul>\n" +
                    "        </div>\n" +
                    "      </div>\n");
    private TemplateString relatedUseCaseTemplate = new TemplateString(
            "            <li class=\"relatedUseCase @@@issue@@@\">\n" +
                    "              <a href=\"@@@link@@@\"><span>@@@name@@@</span></a>@@@error@@@\n" +
                    "            </li>\n");
    private TemplateString parameterTemplate = new TemplateString("<i class=\"value\">@@@value@@@</i>");

    @Override
    public void setResourcePath(String resourcePath){
        if(resourcePath != null && ! resourcePath.equals(getResourcePath())){
            super.setResourcePath(resourcePath);
            htmlResources = new HtmlResources(resourcePath);
        }
    }

    public void report(UseCaseResult useCaseResult) throws IOException, IllegalAccessException {
        htmlResources.ensureIsCreated();

        UseCase useCase = useCaseResult.useCase;
        Class<?> classUnderTest = useCase.classUnderTest;

        File htmlFile = fileSystem.targetFolder.createFileFromClass(classUnderTest, ".html");

        Writer html = new OutputStreamWriter(new FileOutputStream(htmlFile));

        TemplateString enCours = useCaseTemplate.replace("@@@name@@@", useCase.getName());
        enCours = enCours.replace("@@@include.css@@@", htmlResources.getIncludeCssDirectives(useCase.classUnderTest));
        enCours = enCours.replace("@@@include.js@@@", htmlResources.getIncludeJsDirectives(useCase.classUnderTest));
        enCours = enCours.replace("@@@useCase.issue@@@", htmlResources.convertIssue(useCaseResult.getIssue()));
        enCours = enCours.replace("@@@useCase.comment@@@", getUseCaseComment(useCase));
        enCours = enCours.replace("@@@beforeUseCase@@@", getBeforeUseCase(useCase, useCaseResult));
        enCours = enCours.replace("@@@scenarios@@@", getScenarios(useCase, useCaseResult));
        enCours = enCours.replace("@@@afterUseCase@@@", getAfterUseCase(useCase, useCaseResult));
        enCours = enCours.replace("@@@relatedUseCases@@@", getRelatedUseCases(useCaseResult));

        html.write(enCours.getText());
        html.close();
        System.out.println("Report wrote: " + htmlFile.getCanonicalPath());
    }

    private String getRelatedUseCases(UseCaseResult useCaseResult) {
        String relatedUseCases = "";
        if (!useCaseResult.subUseCaseResults.isEmpty()) {
            return relatedUseCasesTemplate.replace("@@@relatedUseCases@@@", getRelatedUseCase(useCaseResult)).getText();
        }
        return relatedUseCases;
    }

    private String getRelatedUseCase(UseCaseResult useCaseResult) {
        String relatedUseCase = "";
        for (UseCaseResult subUseCaseResult : useCaseResult.subUseCaseResults) {
            String error = createHtmlReportAndReturnErrorWhileCreating(relatedUseCase, subUseCaseResult);
            relatedUseCase += relatedUseCaseTemplate.replace("@@@issue@@@", htmlResources.convertIssue(subUseCaseResult.getIssue()))
                    .replace("@@@link@@@", getRelativeUrl(subUseCaseResult.useCase, useCaseResult)).replace("@@@name@@@", subUseCaseResult.useCase.getName())
                    .replace("@@@error@@@", error).getText();
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

    private String getScenarios(UseCase useCase, UseCaseResult useCaseResult) {
        String scenarioTxt = "";
        for (Scenario scenario : useCase.scenarios) {
            for (ScenarioResult scenarioResult : useCaseResult.scenarioResults) {
                if (scenarioResult.scenario.equals(scenario)) {
                    scenarioTxt += getScenario(scenarioResult);
                }
            }
        }
        return scenarioTxt;
    }

    private String getScenario(ScenarioResult scenarioResult) {
        return scenarioTemplate
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
        for (MethodCall testFixture : scenarioResult.scenario.methodCalls) {
            Fixture fixture = scenarioResult.scenario.useCase.getFixtureByMethodName(testFixture.name);
            final Issue issue;
            if (lastIssue == Issue.SUCCEEDED && scenarioResult.failureOccurs(fixture, testFixture)) {
                issue = Issue.FAILED;
                lastIssue = Issue.IGNORED;
            } else {
                issue = lastIssue;
            }


            String instruction = getInstructionWithParameter(testFixture, fixture);

            result += scenarioInstructionTemplate.replace("@@@instruction.issue@@@", htmlResources.convertIssue(issue))
                    .replace("@@@instruction.text@@@", instruction)
                    .getText();
        }
        return result;
    }

    private String getBeforeScenario(ScenarioResult scenarioResult) {
        if (scenarioResult.scenario.useCase.beforeScenario != null) {
            String instructions = getContextInstructions(scenarioResult.scenario.useCase, scenarioResult.scenario.useCase.beforeScenario);
            return beforeScenarioTemplate.replace("@@@scenario.before.instructions@@@", instructions).getText();
        }
        return "";
    }

    private String getAfterScenario(ScenarioResult scenarioResult) {
        if (scenarioResult.scenario.useCase.afterScenario != null) {
            String instructions = getContextInstructions(scenarioResult.scenario.useCase, scenarioResult.scenario.useCase.afterScenario);
            return afterScenarioTemplate.replace("@@@scenario.after.instructions@@@", instructions).getText();
        }
        return "";
    }

    private String getScenarioComment(ScenarioResult scenarioResult) {
        String comment = "";
        if (scenarioResult.scenario.getComment() != null) {
            comment = commentTemplate.replace("@@@comment@@@", scenarioResult.scenario.getComment()).getText();
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
            return displayedContexts.replace("@@@displayedContexts@@@", htmlText).getText();
        }
        return "";
    }

    private String getStackTrace(Throwable failure) {
        if (failure != null) {
            StringWriter stringWriter = new StringWriter();
            failure.printStackTrace(new PrintWriter(stringWriter));
            return stacktrace.replace("@@@failure.className@@@", failure.getClass().getSimpleName())
                    .replace("@@@failure.message@@@", failure.getMessage())
                    .replace("@@@failure.stacktrace@@@", stringWriter.toString()).getText();
        }
        return "";
    }

    private String extractDisplayedContext(String value) {
        return displayedContext.replace("@@@displayedContext@@@", value).getText();
    }

    private String getBeforeUseCase(UseCase useCase, UseCaseResult useCaseResult) {
        String text = "";
        if (useCase.beforeUseCase != null) {
            TemplateString before = beforeUseCaseTemplate.replace("@@@useCase.before.issue@@@", htmlResources.convertIssue(useCaseResult.beforeResult.issue));

            before = before.replace("@@@instructions@@@", getContextInstructions(useCase, useCase.beforeUseCase));
            before = before.replace("@@@exception@@@", getStackTrace(useCaseResult.beforeResult));

            text = before.getText();
        }
        return text;
    }

    private String getAfterUseCase(UseCase useCase, UseCaseResult useCaseResult) throws IOException {
        String text = "";
        if (useCase.afterUseCase != null) {
            TemplateString before = afterUseCaseTemplate.replace("@@@useCase.after.issue@@@", htmlResources.convertIssue(useCaseResult.afterResult.issue));

            before = before.replace("@@@instructions@@@", getContextInstructions(useCase, useCase.afterUseCase));
            before = before.replace("@@@exception@@@", getStackTrace(useCaseResult.afterResult));

            text = before.getText();
        }
        return text;
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
            Fixture fixture = useCase.getFixtureByMethodName(methodCall.name);
            instructions += useCaseContextInstructionsTemplate.replace("@@@instructions@@@", getInstructionWithParameter(methodCall, fixture)).getText();
        }
        return instructions;
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
        return parameterTemplate.replace("@@@value@@@", value).getText();
    }

    private String getUseCaseComment(UseCase useCase) {
        String comment = "";
        if (useCase.haveComment()) {
            return commentTemplate.replace("@@@comment@@@", useCase.getComment()).getText();
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