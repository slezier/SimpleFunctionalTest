package sft.integration.use.sut.decorators.subUseCase;

import org.junit.Ignore;
import org.junit.Test;

public class SubUseCaseToc3 {

    @Test
    public void scenario3_1(){
        success();
    }

    @Test
    @Ignore
    public void scenario3_2(){
        success();
    }
    @Test
    public void scenario3_3(){
        failed();
    }

    private void failed() {
        throw new RuntimeException("Boom");
    }

    private void success() {
    }
}
