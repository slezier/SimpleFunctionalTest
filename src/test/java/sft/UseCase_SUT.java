package sft;

import org.junit.Test;

public class UseCase_SUT {

    public SubUseCase1_SUT subUseCase1 = new SubUseCase1_SUT();
    public SubUseCase2_SUT subUseCase2 ;

    @Test
    public void publicMethodWithTestAnnotation1(){}
    @Test
    public void publicMethodWithTestAnnotation3Disorderly(){}
    @Test
    public void publicMethodWithTestAnnotation2(){}

    @Test
    protected void protectedMethodWithTestAnnotation(){}
    @Test
    private void privateMethodWithTestAnnotation(){}

    public void publicMethodWithoutTestAnnotation(){}
    protected void protectedMethodWithoutTestAnnotation(){}
    private void privateMethodWithoutTestAnnotation(){}


}
