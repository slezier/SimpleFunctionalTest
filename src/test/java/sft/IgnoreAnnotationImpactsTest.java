package sft;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@Ignore
public class IgnoreAnnotationImpactsTest {


    @Test
    public void scenarioWithIgnoreAnnotation_scenarioShouldBeIgnored() throws InstantiationException, IllegalAccessException, IOException {
        UseCase useCase = new UseCase(UseCaseIgnored_SUT.class);

        assertEquals(1, useCase.scenarios.size());
        assertTrue(useCase.scenarios.get(0).shouldBeIgnored());
    }

    @Test
    public void useCaseWithIgnoreAnnotation_useCaseShouldBeIgnored() throws InstantiationException, IllegalAccessException, IOException {
        UseCase useCase = new UseCase(UseCaseIgnored_SUT.class);

        assertTrue(useCase.shouldBeIgnored());
    }

}
