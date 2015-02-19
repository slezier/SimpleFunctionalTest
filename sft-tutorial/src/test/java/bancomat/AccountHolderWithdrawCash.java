package bancomat;


import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/*
<b>As</b> an Account Holder<br/>
<b>I want to</b> withdraw cash from an ATM<br/>
<b>So that</b> I can get money when the bank is closed
*/
@RunWith(SimpleFunctionalTest.class)
public class AccountHolderWithdrawCash {

    private SessionDab atmSession;
    private int withdrawals;
    private Bank bank;
    private User user;
    private Atm atm;
    private Account account;

    @Test
    public void accountHasSufficientFunds() {
        givenTheAccountBalanceIs(100);
        andTheCardIsValid();
        andTheMachineContainsEnoughMoney();

        whenTheAccountHolderRequests(20);

        thenTheAtmShouldDispense(20);
        andTheAccountBalanceShouldBe(80);
        andTheCardShouldBeReturned();
    }

    private void andTheCardShouldBeReturned() {
        assertTrue("Card not returned", atm.returnCard());
    }

    @Text("And the account balance should be ${actual} $")
    private void andTheAccountBalanceShouldBe(int actual) {
        assertEquals(account.balance(), actual);
    }

    private void andTheCardIsValid() {
        account.addValidCreditCard("1234");
    }

    @Text("Given the account balance is ${initialAmount} $")
    private void givenTheAccountBalanceIs(int initialAmount) {
        bank = new Bank();
        user = new User();
        account = bank.createAccount(user, initialAmount);
    }

    @Text("Then the atm should dispense ${actual} $")
    private void thenTheAtmShouldDispense(int actual) {
        assertEquals(withdrawals, actual);
    }

    @Text("When the account holder requests ${amount} $")
    private void whenTheAccountHolderRequests(int amount) {
        atmSession = atm.authenticate(user);
        withdrawals = atmSession.withdraw(amount);
    }

    private void andTheMachineContainsEnoughMoney() {
        atm = bank.getAtm(1000);
    }
}