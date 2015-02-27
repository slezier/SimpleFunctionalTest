package sft.integration.use.sut;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Text;

@RunWith(SimpleFunctionalTest.class)
public class ParameterizedFixture {

    private enum  Param{ enumeratedItem }
    private Object object;
    @Test
    public void usingParameterizedFixture() {
        isGreaterThan(2, 1);
        isGreaterThan(500, 38);
        isGreaterThan(1, 2);
        isGreaterThan(2, -1);
        isLowerThan(2, 5);
        theNameStartWith("Amanda", 'A');
        withAs('A', "Achtung");
        enumIsAccepted(Param.enumeratedItem);
        fieldNameIsAccepted(object);
        moreComplexExpression(1 + 2 % 3);

    }

    @Text("The complex expression ${result} is accepted")
    private void moreComplexExpression(int result) {
    }

    @Text("The object ${object} is accepted")
    private void fieldNameIsAccepted(Object object) {

    }

    @Text("The enum ${enumeratedItem} is accepted")
    private void enumIsAccepted(Param enumeratedItem) {

    }

    @Text("The name ${name} start with ${startWith}")
    private void theNameStartWith(String name, char startWith) {
        Assert.assertTrue(name.charAt(0) == startWith);
    }

    @Text("With ${character} as ${word}")
    private void withAs(char character, String word) {
        Assert.assertEquals(word.toCharArray()[0],character);
    }

    @Text("${first} is greater than ${second}")
    private void isGreaterThan(int first, int second) {
        Assert.assertTrue(first > second);
    }

    @Text("${first} is lower than ${second}")
    private void isLowerThan(int first, int second) {
        Assert.assertTrue(first > second);
    }
}
