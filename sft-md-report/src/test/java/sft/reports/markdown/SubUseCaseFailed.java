package sft.reports.markdown;

import org.junit.Test;

public class SubUseCaseFailed {

    @Test
    public void failedTest() {
        throwError();
    }

    private void throwError() {
        throw  new RuntimeException("Boom");
    }
}
