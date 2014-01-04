package sft.integration.use;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.integration.fixtures.CssParser;
import sft.integration.fixtures.FileSystem;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.use.sut.FunctionalTestIgnored;
import sft.integration.use.sut.WhenFunctionalTestFailed;
import sft.integration.use.sut.YourFirstFunctionalTest;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


/*
    <div>
        To become a use case, your class needs to use the junit annotation @RunWith(SimpleFunctionalTest.class)<br/>
        Then all public methods of this class annotated with the junit &nbsp; @Test are considered as scenario.<br/>
        And all private or protected methods of this class are considered as fixture methods.
    </div>
    <div>
        Class, methods are humanized in use case, scenario and fixture name.
    </div>
    <div>
        Use case has cross/check/interrogation mark depending of test issue failed/succeeded/ignored.
    </div>
    <div>
        Scenario has pink/green/yellow background with cross/check/interrogation mark depending of test issue failed/succeeded/ignored.
    </div>
    <ul>
        <li>Succeeded fixture call are displayed with green check mark: &nbsp; &nbsp;<img src="../../../success_16.png"/></li>
        <li>Failed fixture call are displayed with red cross mark: &nbsp; &nbsp;<img src="../../../failed_16.png"/></li>
        <li>Ignored fixture are displayed with yellow interrogation mark: &nbsp; &nbsp;<img src="../../../ignored_16.png"/></li>
    </ul>
*/
@RunWith(SimpleFunctionalTest.class)
@Text("Common usage: writing Use Cases")
public class CommonUsage {

    private static final String FIRST_HTML_REPORT_PATH = "target/sft-result/sft/integration/use/sut/YourFirstFunctionalTest.html";
    private CssParser sftCss;

    private JUnitHelper functionalTest;
    private Document html;

    @Test
    public void yourFirstFunctionalTest() throws Exception {
        createASimpleJUnitTestClassAndAddJUnitAnnotationRunWithSimpleFunctionnalTest();
        whenInvokingJUnit();

        thenTheFullyQualifiedClassNameIsConvertedIntoPahToHtmlFile();

        theFunctionalTestResultIsDisplayInHtml();

        classNameIsHumanizedAsUseCaseTitle();
        testMethodIsHumanizedAsScenarioSection();
        innerMethodCallIsHumanizedAsFixtureCall();

        successfulUseCaseScenarioAndFixtureCallAreDisplayedWithGreenCheckMark();
        successfulScenarioSectionAreAlsoDisplayWithGreenBackground();
    }


    @Test
    public void whenFunctionalTestFailed() throws Exception {
        withAJUnitTestClassExpectingFailure();
        whenInvokingJUnit();

        thenHtmlFilePresentFailures();

        failedUseCaseScenarioAreDisplayedWithRedCrossMark();
        fixtureCallsAreGreenUntilFailedFixturesThenYellow();
        failedScenarioSectionAreAlsoDisplayWithRedBackgroundAndWithErrorStackTrace();
    }

    @Test
    public void ignoredAnnotationOnScenarioLevel() throws Exception {
        withAJUnitTestClassIgnored();

        whenInvokingJUnit();

        thenHtmlFilePresentUseCaseIgnored();


        ignoredUseCaseScenarioAndFixtureCallAreDisplayedWithYellowInterrogationMark();
        ignoredScenarioSectionAreAlsoDisplayWithYellowBackground();
    }

    private void ignoredUseCaseScenarioAndFixtureCallAreDisplayedWithYellowInterrogationMark() throws IOException {
        Assert.assertTrue("Use case without class 'ignored'", html.select("body").hasClass("ignored"));
        Elements scenarios = html.select("div.scenario");

        Assert.assertTrue("Scenario without class 'ignored'", scenarios.get(0).hasClass("ignored"));
        Assert.assertTrue("Instruction without class 'ignored'", scenarios.get(0).select("div.instruction").get(0).hasClass("ignored"));

        Assert.assertEquals("url(ignored_32.png)", sftCss.get("*.useCase.ignored *.useCaseName").getStyle().getPropertyCSSValue("background-image").getCssText());
        Assert.assertEquals("url(ignored_24.png)", sftCss.get("*.scenario.ignored *.scenarioName").getStyle().getPropertyCSSValue("background-image").getCssText());
        Assert.assertEquals("url(ignored_16.png)", sftCss.get("*.instruction.ignored span").getStyle().getPropertyCSSValue("background-image").getCssText());

        FileSystem.filesExists("target/sft-result/ignored_16.png", "target/sft-result/ignored_24.png", "target/sft-result/ignored_32.png");
    }

    private void ignoredScenarioSectionAreAlsoDisplayWithYellowBackground() {
        Assert.assertTrue("Scenario without class 'succeeded'", html.select("div.scenario").hasClass("ignored"));
        Assert.assertEquals("rgb(255, 255, 163)", sftCss.get("*.scenario.ignored").getStyle().getPropertyCSSValue("background-color").getCssText());
    }

    @Text("With a JUnit <a href=\"../../../../../src/test/java/sft/integration/use/sut/FunctionalTestIgnored.java\">test class</a> annotated with @Ignore")
    private void withAJUnitTestClassIgnored() {
        functionalTest = new JUnitHelper(FunctionalTestIgnored.class, "target/sft-result/sft/integration/use/sut/FunctionalTestIgnored.html");
    }


    @Text("Create a simple JUnit <a href=\"../../../../../src/test/java/sft/integration/use/sut/YourFirstFunctionalTest.java\">test class</a> and add JUnit annotation @RunWith(SimpleFunctionalTest.class)")
    private void createASimpleJUnitTestClassAndAddJUnitAnnotationRunWithSimpleFunctionnalTest() {
        functionalTest = new JUnitHelper(YourFirstFunctionalTest.class, FIRST_HTML_REPORT_PATH);
    }

    @Text("With a JUnit <a href=\"../../../../../src/test/java/sft/integration/use/sut/WhenFunctionalTestFailed.java\">test class</a> expecting failure")
    private void withAJUnitTestClassExpectingFailure() {
        functionalTest = new JUnitHelper(WhenFunctionalTestFailed.class, "target/sft-result/sft/integration/use/sut/WhenFunctionalTestFailed.html");
    }

    @Text("When invoking JUnit")
    private void whenInvokingJUnit() {
        functionalTest.run();
        sftCss= new CssParser();
    }

    @Text("Then the fully qualified class name is converted into path to <a href=\"../../../../../target/sft-result/sft/integration/use/sut/YourFirstFunctionalTest.html\">html</a> file")
    private void thenTheFullyQualifiedClassNameIsConvertedIntoPahToHtmlFile() throws IOException {
        FileSystem.filesExists(FIRST_HTML_REPORT_PATH);
    }

    @Text("Then the <a href=\"../../../../../target/sft-result/sft/integration/use/sut/WhenFunctionalTestFailed.html\">html</a> file present failures")
    private void thenHtmlFilePresentFailures() throws Exception {
        html = functionalTest.getHtmlReport();
    }

    @Text("Then the <a href=\"../../../../../target/sft-result/sft/integration/use/sut/FunctionalTestIgnored.html\">html</a> file present use case ignored")
    private void thenHtmlFilePresentUseCaseIgnored() throws Exception {
        html = functionalTest.getHtmlReport();
    }

    private void theFunctionalTestResultIsDisplayInHtml() throws Exception {
        html = functionalTest.getHtmlReport();
    }

    @Text("Successful use case, scenario and fixture call are displayed with ending green check mark.")
    private void successfulUseCaseScenarioAndFixtureCallAreDisplayedWithGreenCheckMark() throws IOException {
        Assert.assertTrue("Use case without class 'succeeded'", html.select("*.useCase").get(0).hasClass("succeeded"));
        Assert.assertTrue("Scenario without class 'succeeded'", html.select("*.scenario").get(0).hasClass("succeeded"));
        Assert.assertTrue("Fixture without class 'succeeded'", html.select("*.instruction").get(0).hasClass("succeeded"));

        Assert.assertEquals("url(success_32.png)", sftCss.get("*.useCase.succeeded *.useCaseName").getStyle().getPropertyCSSValue("background-image").getCssText());
        Assert.assertEquals("url(success_24.png)", sftCss.get("*.scenario.succeeded *.scenarioName").getStyle().getPropertyCSSValue("background-image").getCssText());
        Assert.assertEquals("url(success_16.png)", sftCss.get("*.instruction.succeeded span").getStyle().getPropertyCSSValue("background-image").getCssText());

        FileSystem.filesExists("target/sft-result/success_16.png", "target/sft-result/success_24.png", "target/sft-result/success_32.png");
    }

    @Text("Successful use case and scenario are displayed with ending red cross mark.")
    private void failedUseCaseScenarioAreDisplayedWithRedCrossMark() throws IOException {
        Assert.assertTrue("Use case without class 'failed'", html.select("body").hasClass("failed"));
        Assert.assertTrue("Scenario without class 'failed'", html.select("div.scenario").hasClass("failed"));

        Assert.assertEquals("url(failed_32.png)", sftCss.get("*.useCase.failed *.useCaseName").getStyle().getPropertyCSSValue("background-image").getCssText());
        Assert.assertEquals("url(failed_24.png)", sftCss.get("*.scenario.failed *.scenarioName").getStyle().getPropertyCSSValue("background-image").getCssText());

        FileSystem.filesExists("target/sft-result/failed_24.png", "target/sft-result/failed_32.png");
    }

    @Text("Successful fixture calls are displayed with green check mark. Until the failed fixture call displayed with red cross mark. Then the others ignored fixture calls are displayed with yellow interrogation mark.")
    private void fixtureCallsAreGreenUntilFailedFixturesThenYellow() throws IOException {
        Assert.assertTrue("Use case without class 'failed'", html.select("body").hasClass("failed"));
        Elements scenarios = html.select("div.scenario");

        Assert.assertTrue("Scenario without class 'failed'", scenarios.get(0).hasClass("failed"));
        Assert.assertTrue("Instruction without class 'succeeded'", scenarios.get(0).select("*.instruction").get(0).hasClass("succeeded"));
        Assert.assertTrue("Instruction without class 'failed'", scenarios.get(0).select("*.instruction").get(1).hasClass("failed"));
        Assert.assertTrue("Instruction without class 'ignored'", scenarios.get(0).select("*.instruction").get(2).hasClass("ignored"));

        Assert.assertTrue("Scenario without class 'failed'", scenarios.get(1).hasClass("failed"));
        Assert.assertTrue("Instruction without class 'succeeded'", scenarios.get(1).select("*.instruction").get(0).hasClass("succeeded"));
        Assert.assertTrue("Instruction without class 'failed'", scenarios.get(1).select("*.instruction").get(1).hasClass("failed"));
        Assert.assertTrue("Instruction without class 'ignored'", scenarios.get(1).select("*.instruction").get(2).hasClass("ignored"));

        Assert.assertEquals("url(failed_32.png)", sftCss.get("*.useCase.failed *.useCaseName").getStyle().getPropertyCSSValue("background-image").getCssText());
        Assert.assertEquals("url(failed_24.png)", sftCss.get("*.scenario.failed *.scenarioName").getStyle().getPropertyCSSValue("background-image").getCssText());
        Assert.assertEquals("url(failed_16.png)", sftCss.get("*.instruction.failed span").getStyle().getPropertyCSSValue("background-image").getCssText());

        FileSystem.filesExists("target/sft-result/failed_16.png", "target/sft-result/failed_24.png", "target/sft-result/failed_32.png");
    }

    private void successfulScenarioSectionAreAlsoDisplayWithGreenBackground() {
        Assert.assertTrue("Scenario without class 'succeeded'", html.select("div.scenario").hasClass("succeeded"));
        Assert.assertEquals("rgb(224, 255, 209)", sftCss.get("*.scenario.succeeded").getStyle().getPropertyCSSValue("background-color").getCssText());
    }


    private void failedScenarioSectionAreAlsoDisplayWithRedBackgroundAndWithErrorStackTrace() {
        Elements scenarios = html.select("div.scenario");
        expectScenarioFailed(scenarios.get(0), "AssertionFailedError: Condition failed");
        expectScenarioFailed(scenarios.get(1), "RuntimeException: Boom");

        Assert.assertEquals("rgb(255, 194, 194)", sftCss.get("*.scenario.failed").getStyle().getPropertyCSSValue("background-color").getCssText());
    }

    private void expectScenarioFailed(Element scenario, String error) {
        Assert.assertTrue("Scenario without class 'failed'", scenario.hasClass("failed"));

        Element exception = scenario.select("div.exception").first();
        Assert.assertEquals(error, exception.select("a").text());

        Element stackTrace = exception.select("pre").first();
        Assert.assertTrue("StackTrace is missing", stackTrace.hasClass("stacktrace"));
        Assert.assertFalse("StackTrace is empty", stackTrace.text().trim().isEmpty());
    }

    private void classNameIsHumanizedAsUseCaseTitle() {
        assertThat(html.title(), is("Your first functional test"));
        assertThat(html.select("html body h1 span").text(), is("Your first functional test"));
    }

    private void testMethodIsHumanizedAsScenarioSection() {
        assertThat(html.select("*.scenario *.scenarioName").text(), is("Public method"));
    }

    private void innerMethodCallIsHumanizedAsFixtureCall() {
        assertThat(html.select("*.scenario *.instruction span").text(), is("Fixture call"));
    }

}
