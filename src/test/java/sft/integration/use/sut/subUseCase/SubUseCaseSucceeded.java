package sft.integration.use.sut.subUseCase;


import org.junit.Assert;
import org.junit.Test;

public class SubUseCaseSucceeded {

    @Test
    public void simpleSuccededScenario(){
        simpleSuccededAction();
    }

    private void simpleSuccededAction() {
        Assert.assertTrue(true);
    }
}
