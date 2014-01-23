package sft.integration.use.sut;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(sft.SimpleFunctionalTest.class)
public class ErrorOccursWhenTerminatingScenario {

    @Before
    public void setup(){
        doNothing();
    }

    @Test
    public void scenario(){
        doNothing();
    }

    @After
    public void teardown(){
        anErrorOccurs();
    }

    private void anErrorOccurs() {
        throw new RuntimeException("Boom");
    }

    private void doNothing() {
        Assert.assertTrue(true);
    }
}
