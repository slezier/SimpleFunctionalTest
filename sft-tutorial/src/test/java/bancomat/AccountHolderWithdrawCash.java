package bancomat;


import org.junit.Test;
import org.junit.runner.RunWith;
import sft.SimpleFunctionalTest;
import sft.Text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    private Account account;
    private Atm atm;

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

    @Test
    public void accountHasInsufficientFunds(){
        givenTheAccountBalanceIs(10);
        andTheCardIsValid();
        andTheMachineContainsEnoughMoney();

        whenTheAccountHolderRequests(20);

        thenTheAtmShouldNotDispenseAnyMoney();
        andTheAtmShouldDisplay("Insufficient funds");
        andTheAccountBalanceShouldBe(10);
        andTheCardShouldBeReturned();
    }

    @Test
    public void  cardHasBeenDisabled(){
        givenTheCardIsDisabled();
        whenTheAccountHolderRequests(20);
        thenTheAtmShouldRetainTheCard();
        andTheAtmShouldDisplay("The card has been retained");
    }



    private void givenTheCardIsDisabled() {
        account.addValidCreditCard("1234");
        account.declareCardLoss();
    }

    private void thenTheAtmShouldNotDispenseAnyMoney() {
        assertEquals(withdrawals,0);
    }

    private void andTheCardShouldBeReturned() {
        assertTrue("Card not returned", atm.returnCard());
    }

    private void thenTheAtmShouldRetainTheCard() {
        assertFalse("Card returned", atm.returnCard());
    }

    @Text("And the atm should displays \"${expectedDisplay}\"")
    private void andTheAtmShouldDisplay(String expectedDisplay) {
        assertEquals(atm.getDisplay(), expectedDisplay);
    }

    private void andTheMachineContainsEnoughMoney() {
        atm = bank.getAtm(1000);
    }

    private void andTheCardIsValid() {
        account.addValidCreditCard("1234");
    }

    @Text("And the account balance should be ${balance} $")
    private void andTheAccountBalanceShouldBe(int balance) {
        assertEquals(account.balance(), balance);
    }

    @Text("Then the atm should dispense  ${cash} $")
    private void thenTheAtmShouldDispense(int cash) {
        assertEquals(withdrawals, cash);
    }

    @Text("When the account holder requests ${amount} $")
    private void whenTheAccountHolderRequests(int amount) {
        atmSession = atm.authenticate(user);
        withdrawals = atmSession.withdraw(amount);
    }

    @Text("Given the account balance is ${initialAmount} $")
    private void givenTheAccountBalanceIs(int initialAmount) {
        bank = new Bank();
        user = new User();
        account = bank.createAccount(user, initialAmount);
    }
}