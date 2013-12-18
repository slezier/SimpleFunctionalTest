package sft.integration.use.sut;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;

@RunWith(SimpleFunctionalTest.class)
public class YourFirstFunctionalTest {

    @Test
    public void publicMethod(){
        fixtureCall();
    }

    private void fixtureCall() {
        Assert.assertTrue(true);
    }
}
