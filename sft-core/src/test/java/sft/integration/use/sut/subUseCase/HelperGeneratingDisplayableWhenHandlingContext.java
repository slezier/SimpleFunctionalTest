package sft.integration.use.sut.subUseCase;

import org.junit.After;
import org.junit.Before;
import sft.Displayable;

public class HelperGeneratingDisplayableWhenHandlingContext {
    @Displayable public String beforeScenario;
    @Displayable public String afterScenario;
    @Before
    public void beforeScenario(){
         beforeScenario = "helper: before scenario";
    }
    @After
    public void afterScenario(){
        afterScenario = "helper: after scenario";
    }
}
