Md Report generator
=====================

This plugin allow to generate test result in MarkDown 

# 1 - Install

Add in your pom.xml the dependency to SequenceDiagramPlugin :

pom.xml:

	<project>
		...
		<dependencies> 
			...
		        <dependency>
           			 <groupId>com.github.slezier</groupId>
	            		<artifactId>sft-md-report</artifactId>
        	    		<version>1.10</version>
				<scope>test</scope>
        		</dependency>
		</dependencies>
	</project>

# 2 - Configure

Add Custom configuration using this plugin:

_src/test/java/bancomat/CustomConfiguration.java_:

	...
	public class CustomConfiguration extends DefaultConfiguration {
		public CustomConfiguration() {
			getReport().addDecorator(SequenceDiagram.class, HtmlSequenceDiagram.class);
		}
	}

# 3 - Use

Add decorate annotation using SequenceDiagram decorator and parameters indicating roles and arrow to displays.

i.e.
  @Decorate(decorator = SequenceDiagram.class,parameters = "atm -> account_holder")
  
will add a sequence diagram with two roles : atm and account_holder, this fixture call will add an arrow from atm to account_holder  


example
_src/test/java/bancomat/AccountHolderWithdrawCash.java_:

    ...
    @Using(CustomConfiguration.class)
    public class AccountHolderWithdrawCash {

       ....

       @Test
       public void accountHasSufficientFunds() {
           bankHelper.theAccountBalanceIs(100);
           bankHelper.andTheCardIsValid();
           bankHelper.andTheMachineContainsEnoughMoney();

           bankHelper.requestCash(20);

           dispenseCash(20);
           bankHelper.andTheAccountBalanceShouldBe(80);
           bankHelper.cardIsReturned();
       }

       @Decorate(decorator = SequenceDiagram.class,parameters = "atm --> account_holder")
       @Text("dispenses ${cash} $")
       private void dispenseCash(int cash) {
           this.ticket= bankHelper.getHtmlTicket();
           assertEquals(bankHelper.withdrawals, cash);
       }
       ...
    }

_src/test/java/bancomat/AccountHolderWithdrawCash.java_:

    ...
    public class BankHelper {

        ...
    
        @Decorate(decorator = SequenceDiagram.class,parameters = "account_holder -> atm")
        @Text("requests ${amount} $")
        public void requestCash(int amount) {
            atmSession = atm.authenticate(user);
            withdrawals = atmSession.withdraw(amount);
        }

        @Decorate(decorator = SequenceDiagram.class,parameters = "atm -> atm")
        @Text("account balance is ${balance} $")
        public void andTheAccountBalanceShouldBe(int balance) {
            assertEquals(account.balance(), balance);
        }

        @Decorate(decorator = SequenceDiagram.class,parameters = "atm -> account_holder")
        public void cardIsReturned() {
            assertTrue("Card not returned", atm.returnCard());
        }

        ...
    }


# 4 - Enjoy

![Group fixtures](./images/step7.png "SequenceDiagram decorator") 