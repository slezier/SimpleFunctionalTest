package sft.integration.set.sut;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Using;


@RunWith(SimpleFunctionalTest.class)
@Using(CustomConfiguration.class)
public class UseCaseWithSpecificConfiguration {

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
