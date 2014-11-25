package sft.integration.use.sut;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Text;

@RunWith(SimpleFunctionalTest.class)
@Text("Use case name specified in @Text")
public class HumanizationUsingTextAnnotation {

    @Test
    @Text("Scenario name specified in @Text")
    public void scenario1(){
        fixture1();
    }

    @Text("Fixture name specified in @Text")
    private void fixture1() {
        Assert.assertTrue(true);
    }
}
