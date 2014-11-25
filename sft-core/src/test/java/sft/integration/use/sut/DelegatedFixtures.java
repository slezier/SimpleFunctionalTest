package sft.integration.use.sut;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import sft.Displayable;
import sft.Text;


public class DelegatedFixtures {

    public void firstFixture() {
        Assert.assertTrue(true);
    }

    @Displayable
    public static String text = null;


    @BeforeClass
    public static void beforeClass(){
        text = "beforeClass ";
    }

    @Before
    public void before(){
        text += "before ";
    }

    @Text("Second fixture with parameter ${string} and ${integer}")
    public void secondFixture(String string, int integer) {
        Assert.assertTrue(true);
    }

    @After
    public void after(){
        text += "after ";
    }

    @AfterClass
    public static void afterClass(){
        text += "afterClass";
    }

}
