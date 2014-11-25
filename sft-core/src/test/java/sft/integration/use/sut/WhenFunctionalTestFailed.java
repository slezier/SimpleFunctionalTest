package sft.integration.use.sut;

import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;

import static org.junit.Assert.assertTrue;

@RunWith(SimpleFunctionalTest.class)
public class WhenFunctionalTestFailed {

    @Test
    public void unsuccessfulScenarioAreDisplayedInRedWithCrossMark(){
        successfulCondition ();
        conditionThatFailed();
        successfulCondition();
    }

    @Test
    public void scenarioThrowingExceptionAreDisplayedInRedWithCrossMark(){
        successfulCondition ();
        conditionThatThrowsException();
        successfulCondition();
    }

    private void successfulCondition() {
        assertTrue(true);
    }

    private void conditionThatThrowsException() {
        throw new RuntimeException("Boom");
    }

    private void conditionThatFailed() {
        assertTrue("Condition failed",false);
    }
}
