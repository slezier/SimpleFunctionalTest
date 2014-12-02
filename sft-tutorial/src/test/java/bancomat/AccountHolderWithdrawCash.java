package bancomat;


import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/*
As an Account Holder  <br/>
I want to withdraw cash from an ATM <br/>
So that I can get money when the bank is closed
*/
@RunWith(SimpleFunctionalTest.class)
public class AccountHolderWithdrawCash {

    private SessionDab atmSession;
    private int withdrawals;
    private Bank bank;
    private User user;
    private Account account;
    private Atm atm;

    @Test
    public void accountHasSufficientFunds() {
        givenTheAccountBalanceIs100Dollars();
        andTheCardIsValid();
        andTheMachineContainsEnoughMoney();

        whenTheAccountHolderRequests20Dollars();

        thenTheAtmShouldDispense20Dollars();
        andTheAccountBalanceShouldBe80Dollars();
        andTheCardShouldBeReturned();
    }

    private void andTheCardShouldBeReturned() {
        assertTrue("Card not returned", atm.returnCard());
    }

    @Text("And the account balance should be 80$")
    private void andTheAccountBalanceShouldBe80Dollars() {
        assertEquals(account.balance(), 80);
    }

    @Text("Then the atm should dispense 20$")
    private void thenTheAtmShouldDispense20Dollars() {
        assertEquals(withdrawals, 20);
    }

    @Text("When the account holder requests 20$")
    private void whenTheAccountHolderRequests20Dollars() {
        atmSession = atm.authenticate(user);
        withdrawals = atmSession.withdraw(20);
    }

    private void andTheMachineContainsEnoughMoney() {
        atm = bank.getAtm(1000);
    }

    private void andTheCardIsValid() {
        account.addValidCreditCard("1234");
    }

    @Text("Given the account balance is 100$")
    private void givenTheAccountBalanceIs100Dollars() {
        bank = new Bank();
        user = new User();
        account = bank.createAccount(user, 100);
    }

}
