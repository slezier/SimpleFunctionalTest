package sft.integration.use.sut;

import org.junit.Assert;
import sft.Text;


public class DelegatedFixtures {

    public void firstFixture() {
        Assert.assertTrue(true);
    }

    @Text("Second fixture with parameter ${string} and ${2}")
    public void secondFixture(String string, int integer) {
        Assert.assertTrue(true);
    }

}
