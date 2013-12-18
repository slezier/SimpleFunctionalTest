package sft.integration.use.sut;

import org.junit.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;

@RunWith(SimpleFunctionalTest.class )
public class FunctionalTestIgnored {

    @Ignore
    @Test
    public void successfullScenario(){
        fixtureCall();
    }

    private void fixtureCall() {
        Assert.assertTrue(true);
    }
}
