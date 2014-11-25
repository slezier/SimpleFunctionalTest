package sft.integration.use;

import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.Displayable;
import sft.FixturesHelper;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.decorators.Breadcrumb;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.ContextInAction;
import sft.integration.use.sut.ErrorOccursWhenRaisingScenario;
import sft.integration.use.sut.ErrorOccursWhenTerminatingScenario;

import java.io.IOException;

import static org.junit.Assert.*;
import static sft.integration.use.sut.ContextInAction.getCallSequence;

/*
Context could be defined and handle for each scenario
 using JUnit annotation @Before and @After
*/
@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = Breadcrumb.class)
public class DefiningTestContextForAScenario {

    @FixturesHelper
    private JUnitHelper jUnitHelper = new JUnitHelper();

    @Test
    public void definingContextForAScenario() throws IOException {
        youCanInstantiateAScenarioContextSpecificInPublicMethodAnnotatedWithBeforeAndTerminateItInPublicMethodAnnotatedWithAfter();
        theContextInstantiationIsRunBeforeEachScenarioAndTheContextFinalizationIsRunAfterEachScenario();
        finallyTheContextInstantiationIsDisplayedInFrontOfEachScenarioAndTheContextFinalizationIsDisplayedBehindEachScenario();
    }

    @Test
    public void errorOccursWhenRaisingAScenarioContext() throws IOException {
        whenAnErrorOccursWhenRaisingAnScenarioContext();
        theUseCaseIsSeenAsFailed();
        theFailedScenarioIsShowInRed();
        allScenarioStepsAreIgnored();
        stackTraceIsDisplayedAtScenarioEnd();
    }

    @Test
    public void errorOccursWhenTerminatingAScenarioContext() throws IOException {
        whenAnErrorOccursWhenTerminatingAnScenarioContext();
        theUseCaseIsSeenAsFailed();
        theFailedScenarioIsShowInRed();
        allScenarioStepsAreRan();
        stackTraceIsDisplayedAtScenarioEnd();
    }

    private void stackTraceIsDisplayedAtScenarioEnd() {
        Elements scenarioPart = jUnitHelper.html.select("*.scenario *.panel-body");
        assertEquals(4,scenarioPart.size());
        assertTrue(scenarioPart.get(0).hasClass("beforeScenario"));
        assertFalse(scenarioPart.get(1).select("*.instruction").isEmpty());
        assertTrue(scenarioPart.get(2).hasClass("afterScenario"));
        assertNotNull("Expecting exception div", scenarioPart.get(3).select("*.exception >div"));
    }

    private void allScenarioStepsAreRan() {
        assertTrue(jUnitHelper.html.select("*.instruction").hasClass("succeeded"));
    }

    private void allScenarioStepsAreIgnored() {
        assertTrue(jUnitHelper.html.select("*.instruction").hasClass("ignored"));
    }
    private void theUseCaseIsSeenAsFailed() {
        assertTrue(jUnitHelper.html.select("*.useCase").get(0).hasClass("failed"));
    }

    private void theFailedScenarioIsShowInRed() {
        assertTrue(jUnitHelper.html.select("*.scenario").hasClass("failed"));
    }

    private void whenAnErrorOccursWhenRaisingAnScenarioContext() throws IOException {
        getCallSequence().clear();
        jUnitHelper.run(this.getClass(),ErrorOccursWhenRaisingScenario.class);
    }

    private void whenAnErrorOccursWhenTerminatingAnScenarioContext() throws IOException {
        getCallSequence().clear();
        jUnitHelper.run(this.getClass(),ErrorOccursWhenTerminatingScenario.class);
    }

    @Text("You can instantiate a use case context specific using public static method annotated with BeforeClass and terminate it in public static method annotated with AfterClass.")
    private void youCanInstantiateAUseCaseContextSpecificInPublicStaticMethodAnnotatedWithBeforeClassAndTerminateItInPublicStaticMethodAnnotatedWithAfterClass() throws IOException {
        getCallSequence().clear();
        jUnitHelper.run(this.getClass(),ContextInAction.class);
    }

    private void finallyTheContextInstantiationIsDisplayedInFrontOfEachScenarioAndTheContextFinalizationIsDisplayedBehindEachScenario() {
        Elements scenarioElements = jUnitHelper.html.select("*.useCase *.container *.scenario");
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
}
