package bancomat;


import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class AtmTest {

    private SessionDab atmSession;
    private int withdrawals;

    @Test
    @Ignore
    public void testWithdrawals() {
        Bank bank = new Bank();
        User user = new User();
        Account account = bank.createAccount(user, 100);
        account.addValidCreditCard("1234");
        Atm atm = bank.getAtm(1000);

        atmSession = atm.authenticate(user);
        withdrawals = atmSession.withdraw(20);

        assertEquals(withdrawals, 20);
        assertEquals(account.balance(), 80);
        assertTrue("Card not returned", atm.returnCard());
    }
}