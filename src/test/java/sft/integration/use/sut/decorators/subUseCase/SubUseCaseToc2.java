package sft.integration.use.sut.decorators.subUseCase;

import org.junit.Test;

public class SubUseCaseToc2 {

    @Test
    public void scenario2_1(){
        success();
    }
    @Test
    public void scenario2_2(){
        success();
    }
    @Test
    public void scenario2_3(){
        success();
    }

    private void success() {
    }


    public SubUseCaseToc2a subUseCaseToc2a = new SubUseCaseToc2a();
    public SubUseCaseToc2b subUseCaseToc2b = new SubUseCaseToc2b();
    public SubUseCaseToc2c subUseCaseToc2c = new SubUseCaseToc2c();
}
