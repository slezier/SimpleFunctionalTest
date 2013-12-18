package sft;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.powermock.api.easymock.PowerMock.createMock;

public class ScenarioTest {

    private boolean methodRan ;

    @Before
    public void setup(){
        methodRan = false;
    }

    @Test
    public void getName_humanizeMethodName() throws NoSuchMethodException {

        Method method = this.getClass().getMethod("aMethodToHumanize");

        UseCase dummyUseCase= createMock(UseCase.class);
        Scenario tested = new Scenario(dummyUseCase,method);

        Assert.assertEquals("A method to humanize", tested.getName());
    }


    @Test
    @Ignore
    public void run_executeMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = this.getClass().getMethod("aMethodSpyingExecution");

        Scenario scenario = new Scenario(null,method);

        scenario.run();

        Assert.assertTrue(methodRan);
    }


    public void aMethodToHumanize(){}

    public void aMethodSpyingExecution(){
        methodRan = true;
    }


}
