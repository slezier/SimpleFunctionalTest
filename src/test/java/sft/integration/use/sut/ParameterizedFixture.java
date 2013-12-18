package sft.integration.use.sut;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Parameter;
import sft.SimpleFunctionalTest;
import sft.Text;

@RunWith(SimpleFunctionalTest.class)
public class ParameterizedFixture {

    @Test
    public void usingParameterizedFixture(){
        isGreaterThan(2, 1);
        isGreaterThan(500, 38);
        isGreaterThan(1, 2);
        isGreaterThan(2, 1);
        isLowerThan(2,5);
    }

    @Text("${1} is greater than ${2}")
    private void isGreaterThan(int first, int second) {
        Assert.assertTrue(first > second);
    }

    @Text("${first} is lower than ${second}")
    private void isLowerThan(@Parameter("first")int first, @Parameter("second")int second) {
        Assert.assertTrue(first>second);
    }

}
