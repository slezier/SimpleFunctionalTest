package sft.integration.use.sut;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;

@RunWith(SimpleFunctionalTest.class)
public class HumanizationCamelCaseClassNameHumanized {
    @Test
    public void camelCaseScenarioNameHumanized(){
        camelCaseFixtureNameHumanized();
    }

    private void camelCaseFixtureNameHumanized() {
        Assert.assertTrue(true);
    }
}
