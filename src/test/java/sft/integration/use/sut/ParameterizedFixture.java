package sft.integration.use.sut;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Text;

@RunWith(SimpleFunctionalTest.class)
public class ParameterizedFixture {

    @Test
    public void usingParameterizedFixture() {
        isGreaterThan(2, 1);
        isGreaterThan(500, 38);
        isGreaterThan(1, 2);
        isGreaterThan(2, 1);
        isLowerThan(2, 5);
        theNameStartWith("Amanda", 'A');
        withAs('A', "Achtung");
    }

    @Text("The name ${1} start with ${2}")
    private void theNameStartWith(String name, char startWith) {
        Assert.assertTrue(name.charAt(0) == startWith);
    }

    @Text("With ${1} as ${2}")
    private void withAs(char character, String word) {
    }

    @Text("${1} is greater than ${2}")
    private void isGreaterThan(int first, int second) {
        Assert.assertTrue(first > second);
    }

    @Text("${first} is lower than ${second}")
    private void isLowerThan(int first, int second) {
        Assert.assertTrue(first > second);
    }
}
