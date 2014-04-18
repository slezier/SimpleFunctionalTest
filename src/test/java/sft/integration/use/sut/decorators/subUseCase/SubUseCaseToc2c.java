package sft.integration.use.sut.decorators.subUseCase;

import org.junit.Test;
import sft.FixturesHelper;

public class SubUseCaseToc2c {
    @FixturesHelper
    private SftFixturesHelper sftFixturesHelper = new SftFixturesHelper();

    @Test
    public void scenario2c_1(){
        sftFixturesHelper.failed();
    }
}
