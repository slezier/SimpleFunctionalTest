package sft.integration.use.sut.decorators;

import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.SimpleFunctionalTest;
import sft.decorators.Group;

@RunWith(SimpleFunctionalTest.class)
public class FixtureGroupDecoratorSample {

    private static final String GIVEN = "Given";
    private static final String WHEN = "When";
    private static final String THEN = "Then";

    @Test
    public void fixtureWithGroup(){
        anElement();
        anotherElement();
        iAskSomething();
        andRequiredAnotherAction();
        somethingHappens();
        theContextChange();
    }

    @Decorate(decorator = Group.class,parameters = GIVEN)
    private void anElement() {
    }

    @Decorate(decorator = Group.class,parameters = GIVEN)
    private void anotherElement() {
    }

    @Decorate(decorator = Group.class,parameters = WHEN)
    private void iAskSomething() {
    }

    @Decorate(decorator = Group.class,parameters = WHEN)
    private void andRequiredAnotherAction() {
    }

    @Decorate(decorator = Group.class,parameters = THEN)
    private void somethingHappens() {
    }

    @Decorate(decorator = Group.class,parameters = THEN)
    private void theContextChange() {
    }

}
