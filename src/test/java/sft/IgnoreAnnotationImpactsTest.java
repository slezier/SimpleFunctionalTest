package sft;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class IgnoreAnnotationImpactsTest {


    @Test
    public void scenarioWithIgnoreAnnotation_scenarioShouldBeIgnored() throws InstantiationException, IllegalAccessException {
        UseCase useCase = new UseCase(UseCaseIgnored_SUT.class);

        assertEquals(1, useCase.scenarios.size());
        assertTrue(useCase.scenarios.get(0).shouldBeIgnored());
    }

    @Test
    public void useCaseWithIgnoreAnnotation_useCaseShouldBeIgnored() throws InstantiationException, IllegalAccessException {
        UseCase useCase = new UseCase(UseCaseIgnored_SUT.class);

        assertTrue(useCase.shouldBeIgnored());
    }

}
