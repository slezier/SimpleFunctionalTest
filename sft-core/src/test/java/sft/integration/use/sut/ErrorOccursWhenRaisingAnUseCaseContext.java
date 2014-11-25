package sft.integration.use.sut;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(sft.SimpleFunctionalTest.class)
public class ErrorOccursWhenRaisingAnUseCaseContext {

    @BeforeClass
    public static void setup(){
        anErrorOccurs();
    }

    @Test
    public void scenario(){
        doNothing();
    }

    @AfterClass
    public static void teardown(){
        doNothing();
    }

    private static void anErrorOccurs() {
        throw new RuntimeException("Boom");
    }

    private static void doNothing() {
        Assert.assertTrue(true);
    }
}
