package sft.integration.use.sut.decorators.subUseCase;

import org.junit.Test;
import sft.FixturesHelper;

public class SubUseCaseToc2 {
    @FixturesHelper
    private SftFixturesHelper sftFixturesHelper = new SftFixturesHelper();

    @Test
    public void scenario2_1(){
        sftFixturesHelper.success();
    }
    @Test
    public void scenario2_2(){
        sftFixturesHelper.success();
    }
    @Test
    public void scenario2_3(){
        sftFixturesHelper.success();
    }

    public SubUseCaseToc2a subUseCaseToc2a = new SubUseCaseToc2a();
    public SubUseCaseToc2b subUseCaseToc2b = new SubUseCaseToc2b();
    public SubUseCaseToc2c subUseCaseToc2c = new SubUseCaseToc2c();
}
