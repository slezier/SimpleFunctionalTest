package sft.integration.set.sut;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;

@RunWith(SimpleFunctionalTest.class)
public class CommonUseCase {

    public SubUseCase1 subUseCase1 = new SubUseCase1();
    public SubUseCase2 subUseCase2 = new SubUseCase2();

    @Test
    public void scenario(){
        fixtureCall();
    }

    private void fixtureCall() {
        Assert.assertTrue(true);
    }
}
