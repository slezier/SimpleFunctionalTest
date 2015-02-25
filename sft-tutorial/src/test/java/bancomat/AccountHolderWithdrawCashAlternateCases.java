package bancomat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sft.Decorate;
import sft.Displayable;
import sft.FixturesHelper;
import sft.Text;
import sft.decorators.Breadcrumb;
import sft.decorators.Group;
import sft.decorators.Table;
import sft.plugins.sequenceDiagramPlugin.SequenceDiagram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@Decorate(decorator = Breadcrumb.class)
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
        bankHelper.theAccountBalanceIs(10);
        bankHelper.andTheCardIsValid();
        bankHelper.andTheMachineContainsEnoughMoney();

        bankHelper.requestCash(20);

        doNotDispenseAnyMoney();
        displays("Insufficient funds");
        bankHelper.andTheAccountBalanceShouldBe(10);
        bankHelper.cardIsReturned();
    }

    @Test
    public void  cardHasBeenDisabled(){
        bankHelper.andTheMachineContainsEnoughMoney();
        theCardIsDisabled();
        bankHelper.requestCash(20);
        theAtmShouldRetainTheCard();
        displays("The card has been retained");
    }

    /*
    The client will withdraw 5 times 10$.    <br/>
    The fifth withdraw will failed.
     */
    @Test
    public void  maximumWithdraw(){
        bankHelper.theAccountBalanceIs(100);
        bankHelper.andTheCardIsValid();
        bankHelper.andTheMachineContainsEnoughMoney();
        theHolderCanWithdrawFiveTimeADay();

        whenTheAccountHolderRequestsThenTheAtmProvidesCash(10,10);
        whenTheAccountHolderRequestsThenTheAtmProvidesCash(10,10);
        whenTheAccountHolderRequestsThenTheAtmProvidesCash(10,10);
        whenTheAccountHolderRequestsThenTheAtmProvidesCash(10, 10);
        whenTheAccountHolderRequestsThenTheAtmProvidesCash(10, 10);
        whenTheAccountHolderRequestsThenTheAtmProvidesCash(10, 0);
    }

    @Decorate(decorator = Table.class,parameters = "withdraws and cash received per visit")
    private void whenTheAccountHolderRequestsThenTheAtmProvidesCash(int amount, int cash) {
        bankHelper.requestCash(amount);
        assertEquals(cash,bankHelper.withdrawals);
        this.ticket= bankHelper.getHtmlTicket();
    }

    private void theHolderCanWithdrawFiveTimeADay() {
        bankHelper.account.maximumWithdrawsPerDay(5);
    }

    @Decorate(decorator = Group.class,parameters = BankHelper.GIVEN)
    private void theCardIsDisabled() {
        bankHelper.theAccountBalanceIs(0);
        bankHelper.account.addValidCreditCard("1234");
        bankHelper.account.declareCardLoss();
    }

    @Decorate(decorator = SequenceDiagram.class,parameters = "atm -> account_holder")
    @Text("displays \"${expectedDisplay}\"")
    private void displays(String expectedDisplay) {
        this.ticket= bankHelper.getHtmlTicket();
        assertEquals(bankHelper.atm.getDisplay(), expectedDisplay);
    }

    @Decorate(decorator = SequenceDiagram.class,parameters = "atm -> account_holder")
    private void doNotDispenseAnyMoney() {
        assertEquals(bankHelper.withdrawals,0);
    }

    @Decorate(decorator = SequenceDiagram.class,parameters = "atm -> atm")
    private void theAtmShouldRetainTheCard() {
        assertFalse("Card returned", bankHelper.atm.returnCard());
    }

}
