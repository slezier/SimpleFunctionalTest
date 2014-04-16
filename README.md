# SimpleFunctionalTest

A JUnit extension to easily adopt functional testing and acceptance testing

## 1 step: make your test readable

In JUnit test refactor your test in order to make it more readable by non java native speaker.

* Add comment before class or test method
* Use expressive names (camelCase or underscore) for your class, test methods and other methods
* In your test ONLY use method calls

A 'more' readable JUnit test for a non native java speaker:

	...
	/*
	As an Account Holder
	I want to withdraw cash from an ATM
	So that I can get money when the bank is closed
	*/
	public class AccountHolderWithdrawCash {
		...
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

		private void givenTheAccountBalanceIs100Dollars(){
		    ...
		}
        ...
	}

## 2 step: add SimpleFunctionalTest
In your pom file insert dependencies to SimpleFunctionalTest

	<project>
		...
		<dependencies>
			...
			<dependency>
				<groupId>com.github.slezier</groupId>
				<artifactId>SimpleFunctionalTest</artifactId>
				<version>1.4</version>
				<scope>test</scope>
			</dependency>
		</dependencies>
	</project>

and specify SimpleFuntionalTest as JUnit runner:

	...
	@RunWith(SimpleFunctionalTest.class)
	public class AccountHolderWithdrawCash {
		...

## 3 step: enjoy
Run the test.

Open the html file generated:

![A simple acceptance test using SFT](./images/SimpleUseCase.png "A simple acceptance test using SFT")

## Other fixtures

[Use weirds characters or specify the way is displayed](./Text.md "Use weirds characters or specify the way is displayed").

[Parameterize your fixture](./ParameterizeFixture.md "Parameterize your fixture").

[Links use cases together](./LinksUseCases.md "Links use cases together").

[Manage test context](./ManageTestContext.md "Manage test context").

[Decorate](./Decorate.md "Decorate"): Table of content, breadcrumb, group fixtures, fixture displayed as table.

## Deeper
[How to use SimpleFunctionalTest](http://htmlpreview.github.io/?http://github.com/slezier/SimpleFunctionalTest/blob/master/target/sft-result/sft/integration/HowToUseSimpleFunctionalTest.html)

Available release:

- 1.5: Bug fix [jar](search.maven.org/remotecontent?filepath=com/github/slezier/SimpleFunctionalTest/1.5/SimpleFunctionalTest-1.5.jar) [doc](http://htmlpreview.github.io/?http://github.com/slezier/SimpleFunctionalTest/blob/SimpleFunctionalTest-1.5/target/sft-result/sft/integration/HowToUseSimpleFunctionalTest.html)
- 1.4: Bug fix [jar](search.maven.org/remotecontent?filepath=com/github/slezier/SimpleFunctionalTest/1.4/SimpleFunctionalTest-1.4.jar) [doc](http://htmlpreview.github.io/?http://github.com/slezier/SimpleFunctionalTest/blob/SimpleFunctionalTest-1.4/target/sft-result/sft/integration/HowToUseSimpleFunctionalTest.html)
- 1.3: Decorators and setting [jar](search.maven.org/remotecontent?filepath=com/github/slezier/SimpleFunctionalTest/1.3/SimpleFunctionalTest-1.3.jar) [doc](http://htmlpreview.github.io/?http://github.com/slezier/SimpleFunctionalTest/blob/SimpleFunctionalTest-1.3/target/sft-result/sft/integration/HowToUseSimpleFunctionalTest.html)
- 1.2: Using test context [jar](search.maven.org/remotecontent?filepath=com/github/slezier/SimpleFunctionalTest/1.2/SimpleFunctionalTest-1.2.jar) [doc](http://htmlpreview.github.io/?http://github.com/slezier/SimpleFunctionalTest/blob/SimpleFunctionalTest-1.2/target/sft-result/sft/integration/HowToUseSimpleFunctionalTest.html)
- 1.1: Basic testing: UseCase Scenario Fixture SubUseCase and FixtureHelper [jar](search.maven.org/remotecontent?filepath=com/github/slezier/SimpleFunctionalTest/1.1/SimpleFunctionalTest-1.1.jar) [doc](http://htmlpreview.github.io/?http://github.com/slezier/SimpleFunctionalTest/blob/SimpleFunctionalTest-1.1/target/sft-result/sft/integration/HowToUseSimpleFunctionalTest.html)


