package sft.integration.use.sut;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;

@RunWith(SimpleFunctionalTest.class)
public class Humanization_underscore_class_name_humanized {
    @Test
    public void underscore_scenario_name_humanized(){
        underscore_fixture_name_humanized();
    }

    private void underscore_fixture_name_humanized() {
        Assert.assertTrue(true);
    }
}
