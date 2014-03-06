package sft.integration.use.sut.decorators.subUseCase;

import org.junit.Test;

public class SubUseCaseToc2c {

    @Test
    public void scenario2c_1(){
        failed();
    }

    private void failed() {
        throw new RuntimeException();
    }

}
