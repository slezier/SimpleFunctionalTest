package sft.integration.use.sut.decorators.subUseCase;

import org.junit.Test;
import sft.FixturesHelper;

public class SubUseCaseToc1 {
    @FixturesHelper
    private SftFixturesHelper sftFixturesHelper = new SftFixturesHelper();

    @Test
    public void scenario1_1(){
        sftFixturesHelper.success();
    }
    @Test
    public void scenario1_2(){
        sftFixturesHelper.success();
    }
    @Test
    public void scenario1_3(){
        sftFixturesHelper.success();
    }
}
