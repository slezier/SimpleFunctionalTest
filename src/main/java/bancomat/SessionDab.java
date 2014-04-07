package bancomat;

import org.omg.SendingContext.RunTime;

public class SessionDab {

    private final Account account;
    private final Atm atm;

    public SessionDab(Account account, Atm atm) {
        this.account = account;
        this.atm = atm;
    }

    public int withdraw(int amount) {
        int cash ;
        cash = 0;
        if( account.cardIsLost()){
            atm.eatTheCard();
            atm.displaysAndWriteTicket("The card has been retained", withdrawTicketStart(amount) + " forbidden\n\nTo get your card back, ask the office");
        }else if( account.balance() <= amount ){
            atm.displaysAndWriteTicket("Insufficient funds", withdrawTicketStart(amount) + " forbidden: insufficient funds\n\nActual balance: "+account.balance());
        }else if(! account.canWithdraw()){
            atm.displaysAndWriteTicket("Maximum withdraws reach", withdrawTicketStart(amount) + " forbidden: maximum withdraws reached\n\n Actual balance: " + account.balance());
        }else{
//            cash = amount;
            account.withdraw(amount);
            atm.displaysAndWriteTicket("Bye", withdrawTicketStart(amount) + " ok\n\nActual balance: " + account.balance());
        }
        atm.ejectCard();
        return cash;
    }

    private String withdrawTicketStart(int amount) {
        return "Account " + this.account.hashCode() + "\n\nwithdraw "+amount+ "$";
    }
}
