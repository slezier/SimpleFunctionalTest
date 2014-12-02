package bancomat;

public class Account {


    private int balance;
    private boolean cardIsValid = false;
    private int maximumWithdraws = 20;

    public Account(int initialAmount) {
        balance = initialAmount;
    }

    public int balance(){
        return balance;
    }

    public void addValidCreditCard(String pin) {
        cardIsValid=true;
    }

    public void withdraw(int amount) {
        maximumWithdraws--;
        this.balance -=amount;

    }

    public boolean cardIsLost() {
        return ! cardIsValid;
    }

    public void declareCardLoss() {
        cardIsValid=false;
    }

    public void maximumWithdrawsPerDay(int maximumWithdraws) {
        this.maximumWithdraws= maximumWithdraws;
    }

    public boolean canWithdraw() {
        return maximumWithdraws>0;
    }
}
