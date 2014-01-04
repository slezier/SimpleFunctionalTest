package sft.integration.use;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.integration.fixtures.JUnitHelper;

import java.io.IOException;

/*
    Literal specifications can be wrote within java multi-line comments to document use case or scenario.
 */
@RunWith(SimpleFunctionalTest.class)
@Text("Add literal specifications in your use case: comment usage")
public class CommentUsage {

    private JUnitHelper functionalTest;

    @Test
    public void commentUseCase() throws IOException {
        useCaseExplanationCouldBeWriteInAMultilinesCommentBeforeClassDeclaration();
        itIsDisplayedInTheReportAfterTheUseCaseTitle();
    }

    @Text("Use case explanation could be  write in a java <a href=\"../../../../../src/test/java/sft/integration/use/sut/CommentUsage.java\">multi-lines comment</a> " +
            "beforeUseCase class declaration.")
    private void useCaseExplanationCouldBeWriteInAMultilinesCommentBeforeClassDeclaration() {
        runCommentUsageTest();
    }

    private void runCommentUsageTest() {
        functionalTest = new JUnitHelper(sft.integration.use.sut.CommentUsage.class, "target/sft-result/sft/integration/use/sut/CommentUsage.html");
        functionalTest.run();
    }

    @Text("It is displayed in the <a href=\"../../../../../target/sft-result/sft/integration/use/sut/CommentUsage.html\">report</a> afterUseCase the use case title.")
    private void itIsDisplayedInTheReportAfterTheUseCaseTitle() throws IOException {
        Assert.assertEquals("use case comment", functionalTest.getHtmlReport().select("*.comment").get(0).text());
    }

    @Test
    public void commentScenario() throws IOException {
        scenarioExplanationCouldBeWriteInAMultilinesCommentBeforeMethodDeclaration();
        itIsDisplayedInTheReportAfterTheScenarioTitleAndBeforeSteps();
    }

    @Text("Scenario explanation could be  write in a java <a href=\"../../../../../src/test/java/sft/integration/use/sut/CommentUsage.java\">multi-lines comment</a>" +
            " beforeUseCase method declaration.")
    private void scenarioExplanationCouldBeWriteInAMultilinesCommentBeforeMethodDeclaration() {
        runCommentUsageTest();
    }

    @Text("It is displayed in the <a href=\"../../../../../target/sft-result/sft/integration/use/sut/CommentUsage.html\">report</a> " +
            "afterUseCase the scenario title and beforeUseCase scenario steps.")
    private void itIsDisplayedInTheReportAfterTheScenarioTitleAndBeforeSteps() throws IOException {
        Assert.assertEquals("scenario comment", functionalTest.getHtmlReport().select("*.scenario *.comment").text());
    }

}
