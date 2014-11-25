package sft.integration.use.sut.decorators.subUseCase;

import org.junit.Ignore;
import org.junit.Test;
import sft.FixturesHelper;

public class SubUseCaseToc3 {
    @FixturesHelper
    private SftFixturesHelper sftFixturesHelper = new SftFixturesHelper();

    @Test
    public void scenario3_1(){
        sftFixturesHelper.success();
    }

    @Test
    @Ignore
    public void scenario3_2(){
        sftFixturesHelper.success();
    }
    @Test
    public void scenario3_3(){
        sftFixturesHelper.failed();
    }
}
