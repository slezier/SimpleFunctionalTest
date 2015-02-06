package sft.reports.markdown.decorators;

import sft.ContextHandler;
import sft.DefaultConfiguration;
import sft.FixtureCall;
import sft.UseCase;
import sft.decorators.DecoratorReportImplementation;
import sft.report.RelativePathResolver;
import sft.reports.markdown.MarkdownReport;
import sft.result.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class MdNullDecorator implements DecoratorReportImplementation {
    private final DefaultConfiguration configuration;
    private final RelativePathResolver pathResolver = new RelativePathResolver();

    public MdNullDecorator(DefaultConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String applyOnUseCase(UseCaseResult useCaseResult, String... parameters) {
        String result = writeUseCaseName(useCaseResult.useCase, useCaseResult.getIssue());
        result += writeComment(useCaseResult.useCase.getComment());
        result += writeContext(useCaseResult.useCase.beforeUseCase);
        result += applyOnScenarios(useCaseResult.scenarioResults);
        result += writeContext(useCaseResult.useCase.afterUseCase);
        result += applyOnSubUseCases(useCaseResult.subUseCaseResults);
        return result;
    }

    private String writeContext(ContextHandler beforeUseCase) {
        String result = "";
        if(beforeUseCase != null && beforeUseCase.fixtureCalls !=null){
            for (FixtureCall fixtureCall : beforeUseCase.fixtureCalls) {
                result += applyOnFixtureCall(Issue.SUCCEEDED, fixtureCall);
            }
        }
        return result;
    }

    private String writeComment(String comment) {
        if( comment == null ){
            return "";
        }
        return  ">" + comment.replaceAll("\\n","\n>\n>") +"\n\n";
    }

    private String writeUseCaseName(UseCase useCase, Issue issue) {
        return "# " + useCase.getName()+ getIssueMarkdownImage(issue, useCase.classUnderTest, Size.BIG) +"\n";
    }

    private String getIssueMarkdownImage(Issue issue, Class<?> classUnderTest, Size size) {
        String pathOf = pathResolver.getPathOf(classUnderTest, "md");
        String relativePathToFile = pathResolver.getRelativePathToFile(pathOf, MarkdownReport.MD_DEPENDENCIES_FOLDER);

        switch (issue){
            case IGNORED:
                return " ![]("+relativePathToFile+"/ignored_"+ size.pixels+".png)";
            case FAILED:
                return " ![]("+relativePathToFile+"/failed_"+ size.pixels+".png)";
            case SUCCEEDED:
            default:
                return "";
        }
    }

    @Override
    public String applyOnFixtures(List<FixtureCallResult> fixtureCallResults, String... parameters) {
        String result = "";
        for (FixtureCallResult fixtureCallResult : fixtureCallResults) {
            result += applyOnFixtureCall(fixtureCallResult.issue,fixtureCallResult.fixtureCall);
        }
        return result;
    }

    private String applyOnFixtureCall(Issue issue, FixtureCall testFixture) {
        String instruction = testFixture.fixture.getText();
        for (Map.Entry<String, String> parameter : testFixture.getParameters().entrySet()) {
            String value = Matcher.quoteReplacement("*" + parameter.getValue() + "*");
            instruction = instruction.replaceAll("\\$\\{" + parameter.getKey() + "\\}", value);
        }
        return  instruction + getIssueMarkdownImage( issue, testFixture.useCase.classUnderTest,Size.SMALL)+"\n\n";
    }

    @Override
    public String applyOnSubUseCases(List<SubUseCaseResult> useCaseResult, String... parameters) {
        String result = "";
        for (SubUseCaseResult subUseCaseResult : useCaseResult) {
            String relativePathToSubUseCaseMdFile = pathResolver.getRelativePathAsFile(subUseCaseResult.subUseCase.parentUseCase.classUnderTest, subUseCaseResult.useCaseResult.useCase.classUnderTest, ".md");
            result += "["+ subUseCaseResult.subUseCase.subUseCase.getName()+"]("+relativePathToSubUseCaseMdFile+")"+getIssueMarkdownImage(subUseCaseResult.useCaseResult.getIssue(),subUseCaseResult.subUseCase.parentUseCase.classUnderTest,Size.SMALL)+"\n\n";
        }
        return result;
    }

    @Override
    public String applyOnScenarios(List<ScenarioResult> scenarioResults, String... parameters) {
        String result = "";
        for (ScenarioResult scenarioResult : scenarioResults) {
            result += "### " + scenarioResult.scenario.getName()+ getIssueMarkdownImage(scenarioResult.issue,scenarioResult.scenario.useCase.classUnderTest,Size.SMALL)+"\n";
            result += writeComment(scenarioResult.scenario.getComment());
            result += writeContext(scenarioResult.scenario.useCase.beforeScenario);
            result += applyOnFixtures(scenarioResult.fixtureCallResults);
            result += writeContext(scenarioResult.scenario.useCase.afterScenario);
            if(scenarioResult.issue == Issue.FAILED){
                result +="~~~\n";
                StringWriter stringWriter = new StringWriter();
                scenarioResult.failure.printStackTrace(new PrintWriter(stringWriter));
                result += stringWriter.toString();
                result +="~~~\n";
            }
        }
        return result;
    }

    enum Size{ SMALL(16), BIG(24);
        final int pixels;

        Size(int pixels) {
            this.pixels = pixels;
        }
    }
}
