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

import sft.*;
import sft.decorators.*;
import sft.environment.TargetFolder;
import sft.report.decorators.*;
import sft.result.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


public class HtmlReport extends Report {

    public static final String HTML_DEPENDENCIES_FOLDER = "sft-html-default";
    private final RelativePathResolver pathResolver = new RelativePathResolver();
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
    public String scenarioCommentTemplate =
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
            "          @@@instruction.emptyLines@@@" +
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

    public String relatedUseCasesTitleTemplate =
            "        <div class=\"panel-heading\">\n" +
                    "          <h3>@@@relatedUseCasesTitle@@@</h3>\n" +
                    "        </div>\n";
    public String relatedUseCasesTemplate =
            "      <div class=\"panel panel-default relatedUseCases\">\n" +
                    "@@@relatedUseCasesTitleTemplates@@@" +
                    "        <div class=\"panel-body\">\n" +
                    "          <ul>\n" +
                    "@@@relatedUseCaseTemplates@@@" +
                    "          </ul>\n" +
                    "        </div>\n" +
                    "      </div>\n";
    public String relatedUseCaseTemplate =
            "            <li class=\"relatedUseCase @@@relatedUseCase.issue@@@\">\n" +
                    "              <a href=\"@@@relatedUseCase.link@@@\"><span>@@@relatedUseCase.name@@@</span></a>\n" +
                    "            </li>\n";
    public String parameterTemplate = "<i class=\"value\">@@@parameter.value@@@</i>";
    public String ignoredClass = "ignored";
    public String failedClass = "failed";
    public String successClass = "succeeded";
    private HtmlResources htmlResources;
    private Map<Class<? extends Decorator>, DecoratorReportImplementation>  decorators= new HashMap<Class<? extends Decorator>, DecoratorReportImplementation>();


    private TargetFolder reportFolder;

    private static final String TARGET_SFT_RESULT = "target/sft-result/";


    public HtmlReport(DefaultConfiguration configuration) {
        super(configuration);
        reportFolder = configuration.getProjectFolder().getTargetFolder(TARGET_SFT_RESULT);
        setResourcePath(HTML_DEPENDENCIES_FOLDER);
        decorators.put(Style.class,new HtmlStyle(configuration));
        decorators.put(Breadcrumb.class,new HtmlBreadcrumb(configuration));
        decorators.put(Group.class,new HtmlGroup(configuration));
        decorators.put(Table.class,new HtmlTable(configuration));
        decorators.put(TableOfContent.class,new HtmlTableOfContent(configuration));
        decorators.put(Synthesis.class,new HtmlSynthesis(configuration));
        decorators.put(NullDecorator.class,new HtmlNullDecorator(configuration));
    }


    @Override
    public void addDecorator(Class<? extends Decorator> decoratorClass, DecoratorReportImplementation decoratorImplementation){
        decorators.put(decoratorClass, decoratorImplementation);
    }

    public void setReportPath(String reportPath) {
        if( ! reportPath.equals( reportFolder.path)){
            reportFolder = new TargetFolder(reportFolder.path,reportPath);
        }
    }

    @Override
    public DecoratorReportImplementation getDecoratorImplementation(Decorator decorator) {
        if( decorators.containsKey(decorator.getClass())){
            return decorators.get(decorator.getClass());
        }
        System.out.println("Decorator " + decorator.getClass().getCanonicalName() + " not Managed by " + this.getClass().getCanonicalName() + " using default decorator");
        return new HtmlNullDecorator(configuration);
    }


    public void setResourcePath(String resourcePath) {
        htmlResources = new HtmlResources(configuration, this,resourcePath);
    }

    @Override
    public void report(UseCaseResult useCaseResult) throws Exception {
        final Decorator decorator = useCaseResult.useCase.useCaseDecorator;
        String useCaseReport = getDecoratorImplementation(decorator).applyOnUseCase(useCaseResult, decorator.parameters);

        File htmlFile = reportFolder.createFileFromClass(useCaseResult.useCase.classUnderTest, ".html");
        Writer html = new OutputStreamWriter(new FileOutputStream(htmlFile));
        html.write(useCaseReport);
        html.close();
        System.out.println("Report wrote: " + htmlFile.getCanonicalPath());
    }

    public String applyOnUseCase(UseCaseResult useCaseResult) {
        Class<?> classUnderTest = useCaseResult.useCase.classUnderTest;
        try {
            return new TemplateString(useCaseTemplate)
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String applyOnScenario(ScenarioResult scenarioResult) {
        return new TemplateString(scenarioTemplate)
                .replace("@@@scenario.issue@@@", htmlResources.convertIssue(scenarioResult.issue))
                .replace("@@@scenario.name@@@", scenarioResult.scenario.getName())
                .replace("@@@scenarioCommentTemplate@@@", getScenarioComment(scenarioResult))
                .replace("@@@beforeScenarioTemplate@@@", getBeforeScenario(scenarioResult))
                .replace("@@@scenarioInstructionTemplates@@@", getScenarioInstructions(scenarioResult))
                .replace("@@@afterScenarioTemplate@@@", getAfterScenario(scenarioResult))
                .replace("@@@displayedContextsTemplates@@@", extractDisplayedContexts(scenarioResult))
                .replace("@@@exceptionTemplate@@@", getStackTrace(scenarioResult.failure))
                .getText();
    }

    public String generateFixtureCall(FixtureCallResult fixtureCallResult) {
        return new TemplateString(scenarioInstructionTemplate)
                .replace("@@@instruction.emptyLines@@@", generateEmptyLines(fixtureCallResult.fixtureCall.emptyLine))
                .replace("@@@instruction.issue@@@", htmlResources.convertIssue(fixtureCallResult.issue))
                .replace("@@@instruction.text@@@", generateInstructionWithParameter(fixtureCallResult.fixtureCall))
                .getText();
    }

    private String generateEmptyLines(int emptyLine) {
        String result = "";
        for (int i = 0; i < emptyLine; i++) {
            result += "<br/>";
        }
        return result;
    }

    private String getRelatedUseCases(UseCaseResult useCaseResult) {
        if (!useCaseResult.subUseCaseResults.isEmpty()) {
            return getRelatedUseCase(useCaseResult);
        }
        return "";
    }

    private String getRelatedUseCase(UseCaseResult useCaseResult) {
        String result = "";
        Decorator decorator = null;
        ArrayList<SubUseCaseResult> subUseCaseResults = new ArrayList<SubUseCaseResult>();
        for (SubUseCaseResult subUseCaseResult : useCaseResult.subUseCaseResults) {
            if (decorator == null) {
                decorator = subUseCaseResult.subUseCase.decorator;
            } else if (!decorator.comply(subUseCaseResult.subUseCase.decorator)) {
                result += getDecoratorImplementation(decorator).applyOnSubUseCases(subUseCaseResults, decorator.parameters);
                decorator = subUseCaseResult.subUseCase.decorator;
                subUseCaseResults = new ArrayList<SubUseCaseResult>();
            }
            subUseCaseResults.add(subUseCaseResult);
        }
        if (!subUseCaseResults.isEmpty()) {
            result += getDecoratorImplementation(decorator).applyOnSubUseCases(subUseCaseResults, decorator.parameters);
        }
        return result;
    }

    public String applyOnSubUseCases(String title, List<SubUseCaseResult> subUseCaseResults) {
        String relatedUseCase = "";
        for (SubUseCaseResult subUseCaseResult : subUseCaseResults) {
            relatedUseCase += new TemplateString(relatedUseCaseTemplate)
                    .replace("@@@relatedUseCase.issue@@@", htmlResources.convertIssue(subUseCaseResult.useCaseResult.getIssue()))
                    .replace("@@@relatedUseCase.link@@@", pathResolver.getRelativePathAsFile(subUseCaseResult.subUseCase.parentUseCase.classUnderTest, subUseCaseResult.useCaseResult.useCase.classUnderTest, ".html"))
                    .replace("@@@relatedUseCase.name@@@", subUseCaseResult.useCaseResult.useCase.getName())
                    .getText();
        }

        final TemplateString replace = new TemplateString(relatedUseCasesTemplate)
                .replace("@@@relatedUseCaseTemplates@@@", relatedUseCase);

        if (title != null) {
            return replace.replace("@@@relatedUseCasesTitleTemplates@@@", new TemplateString(relatedUseCasesTitleTemplate).replace("@@@relatedUseCasesTitle@@@", title).getText()).getText();
        } else {
            return replace.replace("@@@relatedUseCasesTitleTemplates@@@", "").getText();
        }
    }

    private String getScenarios(UseCaseResult useCaseResult) {
        String result = "";
        Decorator decorator = new NullDecorator(configuration);

        final ArrayList<ScenarioResult> scenarioResults= new ArrayList<ScenarioResult>();

        for( ScenarioResult scenarioResult : useCaseResult.scenarioResults ){
            final Scenario scenario = scenarioResult.scenario;
            if(decorator != null && !decorator.comply(scenario.decorator)) {
                result += getDecoratorImplementation(decorator).applyOnScenarios(scenarioResults, decorator.parameters);
                scenarioResults.clear();
            }
            decorator = scenario.decorator;
            scenarioResults.add(scenarioResult);
        }
        result += getDecoratorImplementation(decorator).applyOnScenarios(scenarioResults, decorator.parameters);
        return result;
    }

    private String getScenarioInstructions(ScenarioResult scenarioResult) {
        String result = "";
        Decorator decorator = null;

        final ArrayList<FixtureCallResult> fixtureCallResults = new ArrayList<FixtureCallResult>();

        for (FixtureCallResult fixtureCallResult : scenarioResult.fixtureCallResults) {
            final Fixture fixture = fixtureCallResult.fixtureCall.fixture;
            if (decorator != null && !decorator.comply(fixture.decorator)) {
                result += getDecoratorImplementation(decorator).applyOnFixtures(fixtureCallResults, decorator.parameters);
                fixtureCallResults.clear();
            }
            decorator = fixture.decorator;
            fixtureCallResults.add(fixtureCallResult);
        }
        result += getDecoratorImplementation(decorator).applyOnFixtures(fixtureCallResults, decorator.parameters);
        return result;
    }


    private String getBeforeScenario(ScenarioResult scenarioResult) {
        if (scenarioResult.scenario.useCase.beforeScenario != null) {
            String instructions = getContextInstructions(scenarioResult.scenario.useCase.beforeScenario);
            return new TemplateString(beforeScenarioTemplate)
                    .replace("@@@contextInstructionTemplates@@@", instructions)
                    .getText();
        }
        return "";
    }

    private String getAfterScenario(ScenarioResult scenarioResult) {
        if (scenarioResult.scenario.useCase.afterScenario != null) {
            String instructions = getContextInstructions(scenarioResult.scenario.useCase.afterScenario);
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
                    .replace("@@@contextInstructionTemplates@@@", getContextInstructions(useCase.beforeUseCase))
                    .replace("@@@exceptionTemplate@@@", getStackTrace(useCaseResult.beforeResult))
                    .getText();
        } else {
            return "";
        }
    }

    private String getAfterUseCase(UseCaseResult useCaseResult) throws IOException {
        UseCase useCase = useCaseResult.useCase;
        if (useCase.afterUseCase != null) {
            return new TemplateString(afterUseCaseTemplate)
                    .replace("@@@afterUseCase.issue@@@", htmlResources.convertIssue(useCaseResult.afterResult.issue))
                    .replace("@@@contextInstructionTemplates@@@", getContextInstructions(useCase.afterUseCase))
                    .replace("@@@exceptionTemplate@@@", getStackTrace(useCaseResult.afterResult))
                    .getText();
        } else {
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

    private String getContextInstructions(ContextHandler context) {
        String instructions = "";
        for (FixtureCall fixtureCall : context.fixtureCalls) {
            instructions += new TemplateString(contextInstructionTemplate)
                    .replace("@@@instruction.text@@@", generateInstructionWithParameter(fixtureCall))
                    .getText();
        }
        return instructions;
    }

    public String generateInstructionWithParameter(FixtureCall testFixture) {
        String instruction = testFixture.fixture.getText();
        for (Map.Entry<String, String> parameter : testFixture.getParameters().entrySet()) {
            String value = Matcher.quoteReplacement(getParameter(parameter.getValue()));
            instruction = instruction.replaceAll("\\$\\{" + parameter.getKey() + "\\}", value);
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

    public HtmlResources getHtmlResources() {
        return htmlResources;
    }

    public String getIssueConverter(Issue issue) {
        switch (issue) {
            case IGNORED:
                return ignoredClass;
            case FAILED:
                return failedClass;
            default:
            case SUCCEEDED:
                return successClass;
        }
    }

    public String applyOnFixtures(List<FixtureCallResult> fixtureCallResults) {
        String result = "";
        for (FixtureCallResult fixture : fixtureCallResults) {
            result += generateFixtureCall(fixture);
        }
        return result;
    }

    public String applyOnScenarios(List<ScenarioResult> scenarioResults) {
        String scenarioTxt = "";
        for (ScenarioResult scenarioResult : scenarioResults) {
            scenarioTxt += applyOnScenario(scenarioResult);
        }
        return scenarioTxt;

    }

    public void setReportFolder(String targetPath) {
        reportFolder = configuration.getProjectFolder().getTargetFolder(targetPath);
    }

    public TargetFolder getReportFolder() {
        return reportFolder;
    }
}