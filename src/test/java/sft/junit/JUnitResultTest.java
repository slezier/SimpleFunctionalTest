package sft.junit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import sft.integration.set.sut.CommonUseCase;

public class JUnitResultTest {

    @Test
    public void toto() {
        Result run = new JUnitCore().run(CommonUseCase.class);

        Assert.assertEquals(0, run.getFailureCount());
        Assert.assertEquals(0, run.getIgnoreCount());
        Assert.assertEquals(3, run.getRunCount());
    }
}
