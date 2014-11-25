package sft.javalang;

import org.junit.Assert;
import org.junit.Test;

public class JavaToHumanTranslatorTest {

    @Test
    public void humanize_capitalizeFirstWord(){
        JavaToHumanTranslator tested = new JavaToHumanTranslator();

        Assert.assertEquals("First word should be capitalize",tested.humanize("first_word_should_be_capitalize"));
    }

    @Test
    public void humanize_replaceCamelCaseWithSpaceSeparatedNotCapitalizedWords(){
        JavaToHumanTranslator tested = new JavaToHumanTranslator();

        Assert.assertEquals("Camel case",tested.humanize("CamelCase"));
    }

    @Test
    public void humanize_replaceUnderscoresWithSpaceSeparatedNotCapitalizedWords(){
        JavaToHumanTranslator tested = new JavaToHumanTranslator();

        Assert.assertEquals("Underscores are replaced by space",tested.humanize("underscores_are_replaced_by_space"));
    }

    @Test
    public void humanize_withCamelCaseAndUnderscore(){
        JavaToHumanTranslator tested = new JavaToHumanTranslator();

        Assert.assertEquals("Camel case and underscore",tested.humanize("CamelCase_And_underscore"));
    }
}
