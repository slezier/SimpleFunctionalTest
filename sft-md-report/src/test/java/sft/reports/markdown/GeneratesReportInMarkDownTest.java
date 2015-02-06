package sft.reports.markdown;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Using;
import sft.integration.SftDocumentationConfiguration;
import sft.integration.fixtures.TestFileSystem;

import java.io.FileNotFoundException;
import java.util.List;

@RunWith(SimpleFunctionalTest.class)
@Using(SftDocumentationConfiguration.class)
public class GeneratesReportInMarkDownTest {


    private static final TestFileSystem TEST_FILE_SYSTEM = new TestFileSystem("sft-md-report/");
    private int lineIndex = 0;
    private JUnitMdHelper jUnitMdHelper = new JUnitMdHelper();
    private List<String> successfulUseCase;
    private List<String> failedUseCase;
    private List<String> ignoredUseCase;

    @Test
    public void scenario() throws Exception {
        whenAddingMdGeneratorToTheSftConfiguration();
        aMdReportIsGeneratedForEachUseCase();

        theUseCaseTitleIsDisplayedAsAMarkdownHeader1();
        commentOnUseCaseAreDisplayedFirstAsMarkdownBlockQuote();
        useCaseContextInstanciationIsDisplayedAsMarkdownParagraphs();

        theScenariosTitlesAreDisplayedAsAMarkdownHeader3();
        scenarioCommentsAreDisplayedAsMarkdownBlockQuote();
        scenarioContextInstanciationIsDisplayedAsMarkdownParagraph();
        fixtureCallsAreDisplayedAsMarkdownParagraph();
        fixtureParametersAreDisplayedAsMarkdownEmphasis();
        scenarioContextFinalisationIsDisplayedAsMarkdownParagraph();

        useCaseContextFinalisationIsDisplayedAsMarkdownParagraphs();

        subUseCaseIsDisplayedAsMarkdownLink();

        succeededUseCaseHaveNoSpecificMark();
        succeededScenarioHaveNoSpecificMark();
        succeededFixtureCallHaveNoSpecificMark();
        succeededSubUseCaseHaveNoSpecificMark();

        failedUseCaseAreMarkedWithARedCrossAfterTheTitle();
        failedScenarioAreMarkedWithARedCrossAfterTheTitle();
        failedFixtureCallAreMarkedWithARedCross();
        errorStackTraceAreDisplayedAsMarkdownCode();
        failedSubUseCaseAreMarkedWithARedCross();

        ignoredUseCaseAreMarkedWithAYellowInterrogationMarkAfterTheTitle();
        ignoredScenarioAreMarkedWithAYellowInterrogationMarkAfterTheTitle();
        ignoredFixtureCallAreMarkedWithAYellowInterrogationMark();
        ignoredSubUseCaseAreMarkedWithAYellowInterrogationMark();


    }


    private void failedUseCaseAreMarkedWithARedCrossAfterTheTitle() throws Exception {
        failedUseCase = jUnitMdHelper.readTextFile(TEST_FILE_SYSTEM.createFilePathFromClassAndEnsureItExists(SubUseCaseFailed.class, "md"));
        Assert.assertEquals("# Sub use case failed ![](../../../sft-md-default/failed_24.png)",failedUseCase.get(0));
    }
    private void failedScenarioAreMarkedWithARedCrossAfterTheTitle() {
        Assert.assertEquals("### Failed test ![](../../../sft-md-default/failed_16.png)",failedUseCase.get(1));
    }
    private void failedFixtureCallAreMarkedWithARedCross() {
        Assert.assertEquals("Throw error ![](../../../sft-md-default/failed_16.png)",failedUseCase.get(2));
    }
    private void errorStackTraceAreDisplayedAsMarkdownCode() {
        Assert.assertEquals("~~~",failedUseCase.get(4));
        Assert.assertEquals("java.lang.RuntimeException: Boom",failedUseCase.get(5));
        Assert.assertEquals("\tat sft.reports.markdown.SubUseCaseFailed.throwError(SubUseCaseFailed.java:13)",failedUseCase.get(6));
        Assert.assertEquals("\tat sft.reports.markdown.SubUseCaseFailed.failedTest(SubUseCaseFailed.java:9)",failedUseCase.get(7));
        Assert.assertEquals("~~~",failedUseCase.get(failedUseCase.size()-1));
    }
    private void failedSubUseCaseAreMarkedWithARedCross() {
        Assert.assertEquals("[Sub use case failed](SubUseCaseFailed.md) ![](../../../sft-md-default/failed_16.png)", jUnitMdHelper.text.get(32).trim());
    }

    private void ignoredUseCaseAreMarkedWithAYellowInterrogationMarkAfterTheTitle() throws Exception {
        ignoredUseCase = jUnitMdHelper.readTextFile(TEST_FILE_SYSTEM.createFilePathFromClassAndEnsureItExists(SubUseCaseIgnored.class, "md"));
        Assert.assertEquals("# Sub use case ignored ![](../../../sft-md-default/ignored_24.png)",ignoredUseCase.get(0));
    }
    private void ignoredScenarioAreMarkedWithAYellowInterrogationMarkAfterTheTitle() {
        Assert.assertEquals("### Ignored test ![](../../../sft-md-default/ignored_16.png)",ignoredUseCase.get(1));
    }
    private void ignoredFixtureCallAreMarkedWithAYellowInterrogationMark() {
        Assert.assertEquals("Do nothing ![](../../../sft-md-default/ignored_16.png)",ignoredUseCase.get(2));
    }
    private void ignoredSubUseCaseAreMarkedWithAYellowInterrogationMark() {
        Assert.assertEquals("[Sub use case ignored](SubUseCaseIgnored.md) ![](../../../sft-md-default/ignored_16.png)", jUnitMdHelper.text.get(34).trim());
    }

    private void succeededUseCaseHaveNoSpecificMark() throws Exception {
        successfulUseCase = jUnitMdHelper.readTextFile(TEST_FILE_SYSTEM.createFilePathFromClassAndEnsureItExists(SubUseCaseSuccessful.class, "md"));
        Assert.assertEquals("# Sub use case successful",successfulUseCase.get(0));
    }
    private void succeededScenarioHaveNoSpecificMark() {
        Assert.assertEquals("### Successful test",successfulUseCase.get(1));
    }
    private void succeededFixtureCallHaveNoSpecificMark() {
        Assert.assertEquals("Do nothing",successfulUseCase.get(2));
    }
    private void succeededSubUseCaseHaveNoSpecificMark() {
        Assert.assertEquals("[Sub use case successful](SubUseCaseSuccessful.md)", jUnitMdHelper.text.get(30).trim());
    }

    private void subUseCaseIsDisplayedAsMarkdownLink() {
        Assert.assertEquals("[Sub use case successful](SubUseCaseSuccessful.md)", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals("", jUnitMdHelper.text.get(lineIndex++).trim());
        String expected = "[Sub use case failed](SubUseCaseFailed.md)";
        Assert.assertEquals(expected, jUnitMdHelper.text.get(lineIndex++).trim().substring(0, expected.length()));
        Assert.assertEquals("", jUnitMdHelper.text.get(lineIndex++).trim());
        expected = "[Sub use case ignored](SubUseCaseIgnored.md)";
        Assert.assertEquals(expected, jUnitMdHelper.text.get(lineIndex++).trim().substring(0, expected.length()));
        Assert.assertEquals("", jUnitMdHelper.text.get(lineIndex++).trim());
    }

    private void useCaseContextFinalisationIsDisplayedAsMarkdownParagraphs() {
        Assert.assertEquals("Tear down use case", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals("", jUnitMdHelper.text.get(lineIndex++).trim());
    }

    private void scenarioContextFinalisationIsDisplayedAsMarkdownParagraph() {
        Assert.assertEquals("Tear down scenario", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals("", jUnitMdHelper.text.get(lineIndex++).trim());
    }

    private void scenarioContextInstanciationIsDisplayedAsMarkdownParagraph() {
        Assert.assertEquals("Set up scenario", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals("", jUnitMdHelper.text.get(lineIndex++).trim());
    }

    private void useCaseContextInstanciationIsDisplayedAsMarkdownParagraphs() {
        Assert.assertEquals("Set up use case", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals("", jUnitMdHelper.text.get(lineIndex++).trim());
    }

    private void fixtureParametersAreDisplayedAsMarkdownEmphasis() {
        Assert.assertEquals("Do nothing with parameters *parameter1* and *2*", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals("", jUnitMdHelper.text.get(lineIndex++).trim());

    }

    private void fixtureCallsAreDisplayedAsMarkdownParagraph() {
        Assert.assertEquals("Do nothing", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals("", jUnitMdHelper.text.get(lineIndex++).trim());

    }

    private void scenarioCommentsAreDisplayedAsMarkdownBlockQuote() {
        Assert.assertEquals(">", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals(">", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals(">    Scenario Comment line 1", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals(">", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals(">    Scenario Comment line 2", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals(">", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals(">", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals("", jUnitMdHelper.text.get(lineIndex++).trim());
    }

    private void theScenariosTitlesAreDisplayedAsAMarkdownHeader3() {
        Assert.assertEquals("### First scenario", jUnitMdHelper.text.get(lineIndex++));
    }

    private void commentOnUseCaseAreDisplayedFirstAsMarkdownBlockQuote() {
        Assert.assertEquals(">", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals(">", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals(">Use case Comment line 1", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals(">", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals(">Use case Comment line 2", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals(">", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals(">", jUnitMdHelper.text.get(lineIndex++).trim());
        Assert.assertEquals("", jUnitMdHelper.text.get(lineIndex++));
    }

    private void theUseCaseTitleIsDisplayedAsAMarkdownHeader1() {
        String expected = "# Markdown sample";
        Assert.assertEquals(expected, jUnitMdHelper.text.get(lineIndex++).substring(0,expected.length()));
    }


    private void whenAddingMdGeneratorToTheSftConfiguration() throws Exception {
        jUnitMdHelper.run(this.getClass(), MarkdownSample.class);
    }

    private void aMdReportIsGeneratedForEachUseCase() {
        TEST_FILE_SYSTEM.createFilePathFromClassAndEnsureItExists(MarkdownSample.class, "md");
    }
}
