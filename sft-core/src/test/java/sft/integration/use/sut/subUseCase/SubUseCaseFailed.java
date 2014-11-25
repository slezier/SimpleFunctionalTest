package sft.integration.use.sut.subUseCase;

import org.junit.Assert;
import org.junit.Test;

public class SubUseCaseFailed {

    @Test
    public void simpleFailedScenario(){
        simpleFailedAction();
    }

    private void simpleFailedAction() {
        Assert.assertTrue(false);
    }

}
