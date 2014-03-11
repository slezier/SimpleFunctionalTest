package sft.integration.use.sut.decorators;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.Decorate;
import sft.SimpleFunctionalTest;
import sft.decorators.Table;

@RunWith(SimpleFunctionalTest.class)
public class TableDecoratorSample {

    @Test
    public void table(){
        addition(1,1,2);
        addition(2,3,5);
        addition(8,2,9);
        addition(2,3,5);
    }

    @Decorate(decorator = Table.class, parameters = "Addition sample")
    private void addition(int first, int second, int sum){
        Assert.assertEquals(sum,first+second);

    }
}
