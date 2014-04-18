package sft.integration.use.sut.decorators.subUseCase;

import org.junit.Assert;

public class SftFixturesHelper {


    public void success() {
        Assert.assertTrue(true);
    }

    public void failed() {
        throw new RuntimeException();
    }

    public void doStuff() {
        success();
    }
}
