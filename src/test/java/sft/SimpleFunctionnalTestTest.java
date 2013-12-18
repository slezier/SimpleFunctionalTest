package sft;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

public class SimpleFunctionnalTestTest {

    private SimpleFunctionalTest tested ;

    @Before
    public void setup() throws IllegalAccessException, InitializationError, InstantiationException {
        tested= new SimpleFunctionalTest(UseCase_SUT.class);
    }

    @Test
    public void test(){

    }

}
