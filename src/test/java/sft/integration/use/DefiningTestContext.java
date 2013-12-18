package sft.integration.use;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.use.sut.ContextInAction;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static sft.integration.use.sut.ContextInAction.getCallSequence;

/*
Context could be defined and handle for the whole use case or for each scenario.<br/>
It's using JUnit annotation @BeforeClass, @Before, @After and @AfterClass.


    <div>
        <table class="table">
            <caption>Cheat sheet</caption>
            <tr>
                <th>Java/JUnit item</th>
                <th>Functional test item</th>
                <th>HTML item</th>
            <tr/>
            <tr>
                <td><a href="DefiningTestContext.html">@BeforeClass annotated public static method</a></td>
                <td>Starting context of use case</td>
                <td>HTML paragraph beforeUseCase first scenario paragraph</td>
            <tr/>
            <tr>
                <td><a href="DefiningTestContext.html">@AfterClass annotated public static method</a></td>
                <td>Handling ending context of use case</td>
                <td>HTML paragraph afterUseCase last scenario paragraph</td>
            <tr/>
            <tr>
                <td><a href="DefiningTestContext.html">@Before annotated public method</a></td>
                <td>Starting context of scenario</td>
                <td>HTML first part of scenario paragraph</td>
            <tr/>
            <tr>
                <td><a href="DefiningTestContext.html">@After annotated public method</a></td>
                <td>Handling ending context of scenario</td>
                <td>HTML last part of scenario paragraph</td>
            <tr/>
        </table>
    </div>

*/
@RunWith(SimpleFunctionalTest.class)
public class DefiningTestContext {

    private JUnitHelper functionalTest;
    private Document html;

    @Test
    public void definingContextForAnUseCase() throws IOException {
        youCanInstantiateAUseCaseContextSpecificInPublicStaticMethodAnnotatedWithBeforeClassAndTerminateItInPublicStaticMethodAnnotatedWithAfterClass();
        theContextInstantiationIsRunOnceBeforeAllScenariosAndTheContextFinalizationIsRunOnceAfterAllScenarios();
        finallyTheContextInstantiationIsDisplayedInAParagraphBeforeAllScenariosAndTheContextFinalizationIsDisplayedInAnotherParagraphBeforeAllScenarios();
    }
    @Text("You can instantiate a use case context specific <a href=\"../../../../../src/test/java/sft/integration/use/sut/ContextInAction.java\">using public static method annotated with &at;BeforeClass and terminate it in public static method annotated with &at;AfterClass</a>.")
    private void youCanInstantiateAUseCaseContextSpecificInPublicStaticMethodAnnotatedWithBeforeClassAndTerminateItInPublicStaticMethodAnnotatedWithAfterClass() throws IOException {
        getCallSequence().clear();
        functionalTest = new JUnitHelper(ContextInAction.class, "target/sft-result/sft/integration/use/sut/ContextInAction.html");
        functionalTest.run();

        html = functionalTest.getHtmlReport();
    }

    private void theContextInstantiationIsRunOnceBeforeAllScenariosAndTheContextFinalizationIsRunOnceAfterAllScenarios() {
        assertEquals("useCaseInitialization", getCallSequence().get(0));
        assertEquals("scenarioInitialization", getCallSequence().get(1));
        assertEquals("firstScenario", getCallSequence().get(2));
        assertEquals("scenarioFinalization", getCallSequence().get(3));
        assertEquals("scenarioInitialization", getCallSequence().get(4));
        assertEquals("secondScenario", getCallSequence().get(5));
        assertEquals("scenarioFinalization", getCallSequence().get(6));
        assertEquals("useCaseFinalization", getCallSequence().get(7));
    }

    @Text("Finally the context instantiation is displayed <a href=\"../../../../../target/sft-result/sft/integration/use/sut/ContextInAction.html\">in a paragraph beforeUseCase all scenarios and the context finalization is displayed in another paragraph beforeUseCase all scenarios</a>")
    private void finallyTheContextInstantiationIsDisplayedInAParagraphBeforeAllScenariosAndTheContextFinalizationIsDisplayedInAnotherParagraphBeforeAllScenarios() {
        Elements useCaseElements = html.select("*.useCase *.container > div");
        assertTrue(useCaseElements.get(0).hasClass("page-header"));
        assertTrue(useCaseElements.get(1).hasClass("beforeUseCase"));
        assertTrue(useCaseElements.get(2).hasClass("scenario"));
        assertTrue(useCaseElements.get(3).hasClass("scenario"));
        assertTrue(useCaseElements.get(4).hasClass("afterUseCase"));
    }
    @Test
    public void definingContextForAScenario() throws IOException {
        youCanInstantiateAScenarioContextSpecificInPublicMethodAnnotatedWithBeforeAndTerminateItInPublicMethodAnnotatedWithAfter();
        theContextInstantiationIsRunBeforeEachScenarioAndTheContextFinalizationIsRunAfterEachScenario();
        finallyTheContextInstantiationIsDisplayedBeforeEachScenarioAndTheContextFinalizationIsDisplayedAfterEachScenario();
    }

    @Text("Finally the context instantiation is displayed <a href=\"../../../../../target/sft-result/sft/integration/use/sut/ContextInAction.html\">before each scenarios and the context finalization is displayed in another paragraph beforeUseCase all scenarios</a>")
    private void finallyTheContextInstantiationIsDisplayedBeforeEachScenarioAndTheContextFinalizationIsDisplayedAfterEachScenario() {
        Elements scenarioElements = html.select("*.useCase *.container *.scenario");
        assertTrue(scenarioElements.get(0).select("> div").get(1).hasClass("beforeScenario"));
        assertTrue(scenarioElements.get(0).select("> div").get(3).hasClass("afterScenario"));
        assertTrue(scenarioElements.get(1).select("> div").get(1).hasClass("beforeScenario"));
        assertTrue(scenarioElements.get(1).select("> div").get(3).hasClass("afterScenario"));
    }

    private void theContextInstantiationIsRunBeforeEachScenarioAndTheContextFinalizationIsRunAfterEachScenario() {
        assertEquals("useCaseInitialization", getCallSequence().get(0));
        assertEquals("scenarioInitialization", getCallSequence().get(1));
        assertEquals("firstScenario", getCallSequence().get(2));
        assertEquals("scenarioFinalization", getCallSequence().get(3));
        assertEquals("scenarioInitialization", getCallSequence().get(4));
        assertEquals("secondScenario", getCallSequence().get(5));
        assertEquals("scenarioFinalization", getCallSequence().get(6));
        assertEquals("useCaseFinalization", getCallSequence().get(7));
    }

    @Text("You can instantiate a scenario context specific <a href=\"../../../../../src/test/java/sft/integration/use/sut/ContextInAction.java\">using public method annotated with &at;Before and terminate it in public method annotated with &at;After</a>.")
    private void youCanInstantiateAScenarioContextSpecificInPublicMethodAnnotatedWithBeforeAndTerminateItInPublicMethodAnnotatedWithAfter() throws IOException {
        youCanInstantiateAUseCaseContextSpecificInPublicStaticMethodAnnotatedWithBeforeClassAndTerminateItInPublicStaticMethodAnnotatedWithAfterClass();
    }


}
