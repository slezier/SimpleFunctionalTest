package sft.integration.set.sut.subUseCase;


import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class SubUseCaseIgnored {

    @Test
    @Ignore
    public void simpleIgnoredScenario(){
        simpleSuccededAction();
    }

    private void simpleSuccededAction() {
        Assert.assertTrue(true);
    }
}
