package sft.integration.use.sut.decorators;

import org.junit.runner.RunWith;
import sft.Decorate;
import sft.SimpleFunctionalTest;
import sft.decorators.TableOfContent;
import sft.integration.use.sut.decorators.subUseCase.SubUseCaseToc1;
import sft.integration.use.sut.decorators.subUseCase.SubUseCaseToc2;
import sft.integration.use.sut.decorators.subUseCase.SubUseCaseToc3;

@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = TableOfContent.class)
public class TocDecoratorSample {
    public SubUseCaseToc1 subUseCaseToc1 = new SubUseCaseToc1();
    public SubUseCaseToc2 subUseCaseToc2 = new SubUseCaseToc2();
    public SubUseCaseToc3 subUseCaseToc3 = new SubUseCaseToc3();
}
