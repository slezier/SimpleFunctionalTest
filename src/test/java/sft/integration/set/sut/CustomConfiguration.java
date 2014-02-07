package sft.integration.set.sut;

import sft.DefaultConfiguration;

public class CustomConfiguration extends DefaultConfiguration {

    public CustomConfiguration(){
        setResourcePath("classpath://sft-html-documentation/");
    }

}
