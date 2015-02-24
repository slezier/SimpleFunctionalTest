package bancomat;

import sft.Text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BankHelper {

    private Bank bank;
    private User user;
    public Account account;
    public Atm atm;
    private SessionDab atmSession;
    public int withdrawals;

    @Text("Given the account balance is ${initialAmount} $")
    public void givenTheAccountBalanceIs(int initialAmount) {
        bank = new Bank();
        user = new User();
        account = bank.createAccount(user, initialAmount);
    }

    public void andTheCardIsValid() {
        account.addValidCreditCard("1234");
    }

    public void andTheMachineContainsEnoughMoney() {
        atm = bank.getAtm(1000);
    }

    @Text("When the account holder requests ${amount} $")
    public void whenTheAccountHolderRequests(int amount) {
        atmSession = atm.authenticate(user);
        withdrawals = atmSession.withdraw(amount);
    }

    @Text("And the account balance should be ${balance} $")
    public void andTheAccountBalanceShouldBe(int balance) {
        assertEquals(account.balance(), balance);
    }

    public void andTheCardShouldBeReturned() {
        assertTrue("Card not returned", atm.returnCard());
    }
}
