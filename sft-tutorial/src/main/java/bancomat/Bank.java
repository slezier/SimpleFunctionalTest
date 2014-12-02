package bancomat;

import java.util.HashMap;
import java.util.Map;

public class Bank {

    private final Map<User,Account> accounts  = new HashMap<User,Account>();

    public Account createAccount(User user, int initialAmount) {
        final Account account = new Account(initialAmount);
        accounts.put(user,account);
        return account;
    }

    public Atm getAtm(int intialFund) {
        return new Atm(intialFund,this);
    }

    public Account getAccount(User user) {
        return accounts.get(user);
    }
}
