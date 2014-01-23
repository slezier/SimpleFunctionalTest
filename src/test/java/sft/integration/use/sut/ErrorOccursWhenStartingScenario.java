package sft.integration.use.sut;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(sft.SimpleFunctionalTest.class)
public class ErrorOccursWhenStartingScenario {

    @Before
    public void setup(){
        anErrorOccurs();
    }

    @Test
    public void scenario(){
        doNothing();
    }

    @After
    public void teardown(){
        doNothing();
    }

    private void anErrorOccurs() {
        throw new RuntimeException("Boom");
    }

    private void doNothing() {
        Assert.assertTrue(true);
    }
}
