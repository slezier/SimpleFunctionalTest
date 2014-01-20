package sft;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TextAnnotationImpactsTest {

    private UseCase tested;

    @Before
    public void setup() throws Exception {
        tested= new UseCase(UseCaseWithTextAnnotation_SUT.class);
    }

    @Test
    public void useCase_withTextAnnotation_displayThisName() {
        Assert.assertEquals("Use case name specified with @Text annotation", tested.getName());
        Assert.assertEquals(UseCaseWithTextAnnotation_SUT.class, tested.classUnderTest);
    }

    @Test
    public void useCase_convertPublicMethodsWithTestAnnotation_asScenarioSortedByName() {
        Assert.assertEquals(2, tested.scenarios.size());
        Assert.assertEquals("First scenario specified with @Text annotation", tested.scenarios.get(0).getName());
        Assert.assertEquals("Second scenario specified with @Text annotation", tested.scenarios.get(1).getName());
    }

    @Test
    public void useCase_convertPrivateAndProtectedMethodsWithTestAnnotation_asFixture() {
        Assert.assertEquals(1, tested.fixtures.size());
        Assert.assertEquals("Fixture specified with @Text annotation", tested.fixtures.get(0).getName());
    }


}
