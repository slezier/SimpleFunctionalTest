package sft.integration.use.sut;

import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.integration.use.sut.subUseCase.SubUseCaseFailed;
import sft.integration.use.sut.subUseCase.SubUseCaseIgnored;
import sft.integration.use.sut.subUseCase.SubUseCaseSucceeded;

@RunWith(SimpleFunctionalTest.class)
public class UseCaseLinks {
    public SubUseCaseSucceeded thisSubUseCaseWillBeSuccessfullyCalled = new SubUseCaseSucceeded();

    public SubUseCaseFailed thisSubUseCaseWillBeUnsuccessfullyCalled = new SubUseCaseFailed();

    public SubUseCaseIgnored thisSubUseCaseWillBeIgnored = new SubUseCaseIgnored();


}
