# Parameterize your fixture
Add ${myParameterName} to displayed value used when called.

    ...
    @Text("Given the account balance is ${initialAmount} $")
    private void givenTheAccountBalanceIs(int initialAmount) {
        ...

![Fixture with parameter](./images/FixtureWithParameter.png "Fixture with parameter")

[Back](./README.md)