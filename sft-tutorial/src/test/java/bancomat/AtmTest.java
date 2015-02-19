package bancomat;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/*
As an Account Holder
I want to withdraw cash from an ATM
So that I can get money when the bank is closed
*/
public class AtmTest {

    private SessionDab atmSession;
    private int withdrawals;
    private Bank bank;
    private User user;
    private Atm atm;
    private Account account;

    @Test
    public void testWithdrawals() {
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

    private void andTheAccountBalanceShouldBe80Dollars() {
        assertEquals(account.balance(), 80);
    }

    private void andTheCardIsValid() {
        account.addValidCreditCard("1234");
    }

    private void givenTheAccountBalanceIs100Dollars() {
        bank = new Bank();
        user = new User();
        account = bank.createAccount(user, 100);
    }

    private void thenTheAtmShouldDispense20Dollars() {
        assertEquals(withdrawals, 20);
    }

    private void whenTheAccountHolderRequests20Dollars() {
        atmSession = atm.authenticate(user);
        withdrawals = atmSession.withdraw(20);
    }

    private void andTheMachineContainsEnoughMoney() {
        atm = bank.getAtm(1000);
    }

}