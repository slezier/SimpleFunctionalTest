package bancomat;

import sft.Decorate;
import sft.Text;
import sft.decorators.Group;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BankHelper {

    public final static String GIVEN="Given";
    public final static String WHEN="When";
    public final static String THEN="Then";


    private Bank bank;
    private User user;
    public Account account;
    public Atm atm;
    private SessionDab atmSession;
    public int withdrawals;


    @Decorate(decorator = Group.class,parameters = GIVEN)
    @Text("The account balance is ${initialAmount} $")
    public void givenTheAccountBalanceIs(int initialAmount) {
        account = bank.createAccount(user, initialAmount);
    }

    public void givenAClientOfThisBank() {
        user = new User();
    }

    public void givenABank() {
        bank = new Bank();
    }

    @Decorate(decorator = Group.class,parameters = GIVEN)
    public void andTheCardIsValid() {
        account.addValidCreditCard("1234");
    }

    @Decorate(decorator = Group.class,parameters = GIVEN)
    public void andTheMachineContainsEnoughMoney() {
        atm = bank.getAtm(1000);
    }

    @Decorate(decorator = Group.class,parameters = WHEN)
    @Text("The account holder requests ${amount} $")
    public void whenTheAccountHolderRequests(int amount) {
        atmSession = atm.authenticate(user);
        withdrawals = atmSession.withdraw(amount);
    }

    @Decorate(decorator = Group.class,parameters = THEN)
    @Text("And the account balance should be ${balance} $")
    public void andTheAccountBalanceShouldBe(int balance) {
        assertEquals(account.balance(), balance);
    }

    @Decorate(decorator = Group.class,parameters = THEN)
    public void andTheCardShouldBeReturned() {
        assertTrue("Card not returned", atm.returnCard());
    }

    public String getHtmlTicket() {
        return "ticket:<pre width='25em'>"+atm.ticket()+ "</pre>";
    }
}
