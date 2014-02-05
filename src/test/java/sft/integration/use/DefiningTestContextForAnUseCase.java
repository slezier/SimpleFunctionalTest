package sft.integration.use;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Displayable;
import sft.SimpleFunctionalTest;
import sft.Text;
import sft.integration.fixtures.CssParser;
import sft.integration.fixtures.JUnitHelper;
import sft.integration.fixtures.SftResources;
import sft.integration.use.sut.ContextInAction;
import sft.integration.use.sut.ErrorOccursWhenRaisingAnUseCaseContext;
import sft.integration.use.sut.ErrorOccursWhenRaisingScenario;
import sft.integration.use.sut.ErrorOccursWhenTerminatingAnUseCaseContext;
import sft.integration.use.sut.ErrorOccursWhenTerminatingScenario;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static sft.integration.use.sut.ContextInAction.getCallSequence;

/*
Context could be defined and handle for the whole use case
 using JUnit annotation @BeforeClass and @AfterClass.
*/
@RunWith(SimpleFunctionalTest.class)
public class DefiningTestContextForAnUseCase {

    private JUnitHelper functionalTest;
    private Document html;
    private CssParser cssParser;

    @Displayable
    private SftResources sftResources;

    @Test
    public void definingContextForAnUseCase() throws IOException {
        youCanInstantiateAUseCaseContextSpecificInPublicStaticMethodAnnotatedWithBeforeClassAndTerminateItInPublicStaticMethodAnnotatedWithAfterClass();
        theContextInstantiationIsRunOnceBeforeAllScenariosAndTheContextFinalizationIsRunOnceAfterAllScenarios();
        finallyTheContextInstantiationIsDisplayedInAParagraphBeforeAllScenariosAndTheContextFinalizationIsDisplayedInAnotherParagraphBeforeAllScenarios();
    }


    @Test
    public void errorOccursWhenRaisingUseCaseContext() throws IOException {
        whenAnErrorOccursWhenRaisingAnUseCaseContext();
        theUseCaseIsSeenAsFailed();
        theFailedContextIsShowInRed();
        allScenariosAreIgnored();
        theUseCaseContextTerminationIsIgnoredAndShownInYellow();
        stackTraceIsDisplayedAtUseCaseStart();
    }

    @Test
    public void errorOccursWhenTerminatingUseCaseContext() throws IOException {
        whenAnErrorOccursWhenTerminatingAnUseCaseContext();
        theUseCaseIsSeenAsFailed();
        allScenariosAreRan();
        theTerminatingFailedContextIsShowInRed();
        stackTraceIsDisplayedAtUseCaseEnd();
    }

    private void stackTraceIsDisplayedAtUseCaseEnd() {
        Elements teardDown = html.select("*.afterUseCase > div");
        assertEquals(2,teardDown.size());
        assertNotNull(teardDown.get(1).select("*.exception"));
    }

    private void stackTraceIsDisplayedAtUseCaseStart() {
        Elements setup = html.select("*.beforeUseCase > div");
        assertEquals(2, setup.size());
        assertNotNull(setup.get(1).select("*.exception"));
    }

    private void theUseCaseIsSeenAsFailed() {
        assertTrue(html.select("*.useCase").get(0).hasClass("failed"));
    }

    private void theUseCaseContextTerminationIsIgnoredAndShownInYellow() {
        assertTrue(html.select("*.afterUseCase").hasClass("ignored"));
        assertEquals("rgb(255, 255, 163)", cssParser.get("*.afterUseCase.ignored").getStyle().getPropertyCSSValue("background-color").getCssText());
    }

    private void allScenariosAreIgnored() {
        assertTrue(html.select("*.scenario").hasClass("ignored"));
    }

    private void theFailedContextIsShowInRed() {
        assertTrue(html.select("*.beforeUseCase").hasClass("failed"));
        assertEquals("rgb(255, 194, 194)", cssParser.get("*.beforeUseCase.failed").getStyle().getPropertyCSSValue("background-color").getCssText());
    }

    private void theTerminatingFailedContextIsShowInRed() {
        assertTrue(html.select("*.afterUseCase").hasClass("failed"));
        assertEquals("rgb(255, 194, 194)", cssParser.get("*.afterUseCase.failed").getStyle().getPropertyCSSValue("background-color").getCssText());
    }

    private void whenAnErrorOccursWhenTerminatingAnUseCaseContext() throws IOException {
        getCallSequence().clear();
        functionalTest = new JUnitHelper(this.getClass(),ErrorOccursWhenTerminatingAnUseCaseContext.class, "target/sft-result/sft/integration/use/sut/ErrorOccursWhenTerminatingAnUseCaseContext.html");
        functionalTest.run();
        sftResources = functionalTest.displayResources();

        html = functionalTest.getHtmlReport();
    }

    private void allScenariosAreRan() {
        assertTrue(html.select("*.scenario").hasClass("succeeded"));
    }

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

    private void whenAnErrorOccursWhenRaisingAnUseCaseContext() throws IOException {
        functionalTest = new JUnitHelper(this.getClass(),ErrorOccursWhenRaisingAnUseCaseContext.class, "target/sft-result/sft/integration/use/sut/ErrorOccursWhenRaisingAnUseCaseContext.html");
        functionalTest.run();
        sftResources = functionalTest.displayResources();
        html = functionalTest.getHtmlReport();
        cssParser = new CssParser();
    }



}