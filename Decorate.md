# Decorate
## Add table of content
On class level add @Decorate(decorator = TableOfContent.class)

	...
	@Decorate(decorator = TableOfContent.class)
	public class AccountHolderWithdrawCash {
	    ...
        public  AccountHolderWithdrawCashAlternateCases alternateCases = new AccountHolderWithdrawCashAlternateCases();
        ...
	}

![Decorate with a table of content](./images/DecoratorTOC.png "Decorate with a table of content")

## Add breadcrumb
On class level add @Decorate(decorator = Breadcrumb.class)

	@Decorate(decorator = Breadcrumb.class)
	public class AccountHolderWithdrawCashAlternateCases {

![Decorate with a breadcrumb](./images/DecoratorBreadcrumb.png "Decorate with a breadcrumb")

## Group fixtures
For each fixture to group add @Decorate(decorator = Group.class,parameters = "Group title")

	...
	public class AccountHolderWithdrawCash {
		...
    	@Test
    	public void accountHasSufficientFunds() {
        	bankHelper.theAccountBalanceIs(100);
        	bankHelper.andTheCardIsValid();
        	bankHelper.andTheMachineContainsEnoughMoney();
        	...
        }
        ...
    ...

	public class BankHelper {
		...
	    @Decorate(decorator = Group.class,parameters = GIVEN)
	    @Text("The account balance is ${initialAmount} $")
	    public void theAccountBalanceIs(int initialAmount) {
	        ...
	    }
	    ...
	    @Decorate(decorator = Group.class,parameters = GIVEN)
	    public void andTheCardIsValid() {
	    	...
	    }
	    ...
	    @Decorate(decorator = Group.class,parameters = GIVEN)
	    public void andTheMachineContainsEnoughMoney() {
	        ...
	    }
	    ...
	}

![Decorate group fixtures](./images/DecoratorGroupFixtures.png "Decorate group fixtures")

## Display fixtures as table
On fixture level add  @Decorate(decorator = Table.class,parameters = "Table title")


	public class AccountHolderWithdrawCashAlternateCases {
		...
		@Test
	    public void  maximumWithdraw(){
	        ...
	        whenTheAccountHolderRequestsThenTheAtmProvidesCash(10,10);
	        whenTheAccountHolderRequestsThenTheAtmProvidesCash(10,10);
	        whenTheAccountHolderRequestsThenTheAtmProvidesCash(10,10);
	        whenTheAccountHolderRequestsThenTheAtmProvidesCash(10,10);
	        whenTheAccountHolderRequestsThenTheAtmProvidesCash(10,10);
	        whenTheAccountHolderRequestsThenTheAtmProvidesCash(10, 0);
	    }
	    ...
    	@Decorate(decorator = Table.class,parameters = "withdraws and cash received per visit")
    	private void whenTheAccountHolderRequestsThenTheAtmProvidesCash(int amount, int cash) {
    		...
    	}
    	...
    }

![Display fixture as table](./images/DecoratorTable.png "Display fixture as table")

[Back](./README.md)
