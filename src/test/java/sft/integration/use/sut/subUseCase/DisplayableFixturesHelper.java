package sft.integration.use.sut.subUseCase;

import sft.Displayable;

public class DisplayableFixturesHelper {
    @Displayable
    public String display = null;

    public void makeDisplayable() {
        display = "It is displayed";
    }
}
