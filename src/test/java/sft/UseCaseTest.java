package sft;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class UseCaseTest {

    private UseCase tested;

    @Before
    public void setup() throws Exception {
        tested= new UseCase(UseCase_SUT.class);
    }

    @Test
    public void useCase_name_isHumanized() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        assertEquals("Use case sut", tested.getName());
        assertEquals(UseCase_SUT.class, tested.classUnderTest);
    }

    @Test
    public void useCase_convertPublicMethodsWithTestAnnotation_asScenarioSortedByName() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        assertEquals(3, tested.scenarios.size());
        assertEquals("Public method with test annotation 1", tested.scenarios.get(0).getName());
        assertEquals("Public method with test annotation 2", tested.scenarios.get(1).getName());
        assertEquals("Public method with test annotation 3 disorderly", tested.scenarios.get(2).getName());
    }

    @Test
    public void useCase_convertPrivateAndProtectedMethods_asFixture() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        Collections.sort(tested.fixtures, new FixtureComparator());
        assertEquals(4, tested.fixtures.size());
        assertEquals("Private method with test annotation", tested.fixtures.get(0).getName());
        assertEquals("Private method without test annotation", tested.fixtures.get(1).getName());
        assertEquals("Protected method with test annotation", tested.fixtures.get(2).getName());
        assertEquals("Protected method without test annotation", tested.fixtures.get(3).getName());
    }

    @Test
    public void useCase_convertPublicField_asSubUseCase() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        assertEquals(2, tested.subUseCases.size());
        assertEquals("Sub use case 1 sut", tested.subUseCases.get(0).getName());
        assertEquals("Sub use case 2 sut", tested.subUseCases.get(1).getName());
    }

    @Test
    public void getFixtureByMethodName_withExistingMethod_returnFixture(){
        Collections.sort(tested.fixtures, new FixtureComparator());
        Fixture actual = tested.getFixtureByMethodName("protectedMethodWithTestAnnotation");
        assertEquals(tested.fixtures.get(2),actual);
    }
    @Test
    public void getFixtureByMethodName_withNotExistingMethod_throwException(){
        Collections.sort(tested.fixtures, new FixtureComparator());
        try {
            tested.getFixtureByMethodName("notExistingFixture");
            Assert.fail("Expecting RuntimeException");
        }catch (RuntimeException e){
            assertEquals("No fixture found matching the private or protected method notExistingFixture in class sft.UseCase_SUT(use case: Use case sut)",e.getMessage());
        }
    }

    private static class FixtureComparator implements Comparator<Fixture> {
        @Override
        public int compare(Fixture o1, Fixture o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
