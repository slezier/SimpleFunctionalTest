package sft.integration.use.sut;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Displayable;
import sft.FixturesHelper;
import sft.SimpleFunctionalTest;
import sft.integration.use.sut.subUseCase.HelperGeneratingDisplayableWhenHandlingContext;

@RunWith(SimpleFunctionalTest.class)
public class DisplayContextGeneratingDuringContextEnding {

    @FixturesHelper private HelperGeneratingDisplayableWhenHandlingContext helper= new HelperGeneratingDisplayableWhenHandlingContext();

    @Displayable private String beforeScenario;
    @Displayable private String duringScenario;
    @Displayable private String afterScenario;

    @Before
    public void beforeScenario(){
        beforeScenario = "before scenario";
    }

    @After
    public void afterScenario(){
        afterScenario = "after scenario";
    }

    @Test
    public void scenarioOk(){
        doStuff();
    }

    @Test
    public void scenarioKo(){
        doStuff();
        boom();
    }

    private void boom() {
        throw new RuntimeException("Boom");
    }

    private void doStuff() {
        duringScenario = "during scenario";
    }
}
