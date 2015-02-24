package bancomat;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import sft.*;
import sft.decorators.Group;
import sft.decorators.TableOfContent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/*
<b>As</b> an Account Holder<br/>
<b>I want to</b> withdraw cash from an ATM<br/>
<b>So that</b> I can get money when the bank is closed
*/
@RunWith(SimpleFunctionalTest.class)
@Decorate(decorator = TableOfContent.class)
public class AccountHolderWithdrawCash {

    public AccountHolderWithdrawCashAlternateCases accountHolderWithdrawCashAlternateCases = new AccountHolderWithdrawCashAlternateCases();
    @FixturesHelper
    private static BankHelper bankHelper = new BankHelper();
    @Displayable
    private String ticket;

    @BeforeClass
    public static void setupUseCase(){
        bankHelper.givenABank();
    }
    @Before
    public void setupScenario(){
        bankHelper.givenAClientOfThisBank();
    }

    @Test
    public void accountHasSufficientFunds() {
        bankHelper.givenTheAccountBalanceIs(100);
        bankHelper.andTheCardIsValid();
        bankHelper.andTheMachineContainsEnoughMoney();

        bankHelper.whenTheAccountHolderRequests(20);

        thenTheAtmShouldDispense(20);
        bankHelper.andTheAccountBalanceShouldBe(80);
        bankHelper.andTheCardShouldBeReturned();
    }

    @Decorate(decorator = Group.class,parameters = BankHelper.THEN)
    @Text("Then the atm should dispense  ${cash} $")
    private void thenTheAtmShouldDispense(int cash) {
        this.ticket= bankHelper.getHtmlTicket();
        assertEquals(bankHelper.withdrawals, cash);
    }
}