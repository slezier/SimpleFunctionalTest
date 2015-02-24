package bancomat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sft.Displayable;
import sft.FixturesHelper;
import sft.Text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AccountHolderWithdrawCashAlternateCases {

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
    public void accountHasInsufficientFunds(){
        bankHelper.givenTheAccountBalanceIs(10);
        bankHelper.andTheCardIsValid();
        bankHelper.andTheMachineContainsEnoughMoney();

        bankHelper.whenTheAccountHolderRequests(20);

        thenTheAtmShouldNotDispenseAnyMoney();
        andTheAtmShouldDisplay("Insufficient funds");
        bankHelper.andTheAccountBalanceShouldBe(10);
        bankHelper.andTheCardShouldBeReturned();
    }

    @Test
    public void  cardHasBeenDisabled(){
        givenTheCardIsDisabled();
        bankHelper.whenTheAccountHolderRequests(20);
        thenTheAtmShouldRetainTheCard();
        andTheAtmShouldDisplay("The card has been retained");
    }

    private void givenTheCardIsDisabled() {
        bankHelper.givenTheAccountBalanceIs(0);
        bankHelper.account.addValidCreditCard("1234");
        bankHelper.account.declareCardLoss();
    }

    @Text("And the atm should displays \"${expectedDisplay}\"")
    private void andTheAtmShouldDisplay(String expectedDisplay) {
        this.ticket= bankHelper.getHtmlTicket();
        assertEquals(bankHelper.atm.getDisplay(), expectedDisplay);
    }

    private void thenTheAtmShouldNotDispenseAnyMoney() {
        assertEquals(bankHelper.withdrawals, 0);
    }

    private void thenTheAtmShouldRetainTheCard() {
        assertFalse("Card returned", bankHelper.atm.returnCard());
    }
}
