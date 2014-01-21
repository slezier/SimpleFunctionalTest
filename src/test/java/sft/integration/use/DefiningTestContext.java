package sft.integration.use;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Displayable;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.ContextInAction;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static sft.integration.use.sut.ContextInAction.getCallSequence;

/*
Context could be defined and handle for the whole use case or for each scenario.<br/>
It's using JUnit annotation @BeforeClass, @Before, @After and @AfterClass.
*/
@RunWith(SimpleFunctionalTest.class)
public class DefiningTestContext {

    private JUnitHelper functionalTest;
    private Document html;

    @Displayable
    private SftResources sftResources;

    @Test
    public void definingContextForAnUseCase() throws IOException {
        youCanInstantiateAUseCaseContextSpecificInPublicStaticMethodAnnotatedWithBeforeClassAndTerminateItInPublicStaticMethodAnnotatedWithAfterClass();
        theContextInstantiationIsRunOnceBeforeAllScenariosAndTheContextFinalizationIsRunOnceAfterAllScenarios();
        finallyTheContextInstantiationIsDisplayedInAParagraphBeforeAllScenariosAndTheContextFinalizationIsDisplayedInAnotherParagraphBeforeAllScenarios();
    }


    @Test
    public void definingContextForAScenario() throws IOException {
        youCanInstantiateAScenarioContextSpecificInPublicMethodAnnotatedWithBeforeAndTerminateItInPublicMethodAnnotatedWithAfter();
        theContextInstantiationIsRunBeforeEachScenarioAndTheContextFinalizationIsRunAfterEachScenario();
        finallyTheContextInstantiationIsDisplayedInFrontOfEachScenarioAndTheContextFinalizationIsDisplayedBehindEachScenario();
    }

//    @Test
//    public void errorOccursWhenRaisingUseCaseContext() throws IOException {
//        whenAnErrorOccursWhenRaisingAnUseCaseContext();
//        theUseCaseIsSeenAsFailed();
//        theFailedContextIsShowInRed();
//        allScenariosAreIgnored();
//        theUseCaseContextTerminationIsIgnored();
//    }
//
//    @Test
//    public void errorOccursWhenTerminatingUseCaseContext(){
//        whenAnErrorOccursWhenTerminatingAnUseCaseContext();
//        theUseCaseIsSeenAsFailed();
//        allScenariosAreRan();
//        theFailedContextIsShowInRed();
//    }
//
//    @Test
//    public void errorOccursWhenRaisingAScenarioContext(){
//        whenAnErrorOccursWhenRaisingAnScenarioContext();
//        theFailedScenarioIsShowInRed();
//    }

    @Text("You can instantiate a use case context specific using public static method annotated with BeforeClass and terminate it in public static method annotated with AfterClass.")
    private void youCanInstantiateAUseCaseContextSpecificInPublicStaticMethodAnnotatedWithBeforeClassAndTerminateItInPublicStaticMethodAnnotatedWithAfterClass() throws IOException {
        getCallSequence().clear();
        functionalTest = new JUnitHelper(this.getClass(),ContextInAction.class, "target/sft-result/sft/integration/use/sut/ContextInAction.html");
        functionalTest.run();
        sftResources = functionalTest.displayResources();

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

    private void finallyTheContextInstantiationIsDisplayedInAParagraphBeforeAllScenariosAndTheContextFinalizationIsDisplayedInAnotherParagraphBeforeAllScenarios() {
        Elements useCaseElements = html.select("*.useCase *.container > div");
        assertTrue(useCaseElements.get(0).hasClass("page-header"));
        assertTrue(useCaseElements.get(1).hasClass("beforeUseCase"));
        assertTrue(useCaseElements.get(2).hasClass("scenario"));
        assertTrue(useCaseElements.get(3).hasClass("scenario"));
        assertTrue(useCaseElements.get(4).hasClass("afterUseCase"));
    }
    private void finallyTheContextInstantiationIsDisplayedInFrontOfEachScenarioAndTheContextFinalizationIsDisplayedBehindEachScenario() {
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

    @Text("You can instantiate a scenario context specific using public method annotated with Before and terminate it in public method annotated with After</a>.")
    private void youCanInstantiateAScenarioContextSpecificInPublicMethodAnnotatedWithBeforeAndTerminateItInPublicMethodAnnotatedWithAfter() throws IOException {
        youCanInstantiateAUseCaseContextSpecificInPublicStaticMethodAnnotatedWithBeforeClassAndTerminateItInPublicStaticMethodAnnotatedWithAfterClass();
    }

    private void whenAnErrorOccursWhenRaisingAnUseCaseContext() throws IOException {
        functionalTest = new JUnitHelper(this.getClass(),ErrorOccursWhenRaisingAnUseCaseContext.class, "target/sft-result/sft/integration/use/sut/ContextInAction.html");
        functionalTest.run();
        sftResources = functionalTest.displayResources();
        html = functionalTest.getHtmlReport();
    }



}
