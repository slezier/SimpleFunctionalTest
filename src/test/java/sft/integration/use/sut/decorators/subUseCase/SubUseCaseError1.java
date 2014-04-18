package sft.integration.use.sut.decorators.subUseCase;

import org.junit.Test;
import sft.FixturesHelper;

public class SubUseCaseError1 {
    @FixturesHelper
    private SftFixturesHelper sftFixturesHelper = new SftFixturesHelper();

    @Test
    public void scenario(){
        sftFixturesHelper.success();
    }
}
