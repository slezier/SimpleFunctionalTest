package sft;

import org.junit.Test;

@Text("Use case name specified with @Text annotation")
public class UseCaseWithTextAnnotation_SUT {

    @Text("Sub use case with name specified with @Text annotation")
    public SubUseCase1_SUT subUseCase1 = new SubUseCase1_SUT();

    @Test
    @Text("First scenario specified with @Text annotation")
    public void publicMethodWithTestAnnotation1(){}

    @Text("Second scenario specified with @Text annotation")
    @Test
    public void publicMethodWithTestAnnotation2(){}


    @Text("Fixture specified with @Text annotation")
    protected void protectedMethodWithoutTestAnnotation(){}


}
