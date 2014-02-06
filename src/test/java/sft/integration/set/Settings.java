package sft.integration.set;

import org.junit.Ignore;

/*
  All settings of SimpleFunctionalTest are hold by a class DefaultConfiguration.
  To modify settings you have to create an inherited class from DefaultConfiguration and change setting in the constructor.
  Then you can inject configuration by annotated UseCase java class with: @Using( MyOwnConfiguration.class ).
  All related useCases will use this configuration.
 */
@Ignore
public class Settings {



}
