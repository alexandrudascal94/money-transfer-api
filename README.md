# Revolut Money Transfer REST API
REST API for create, deposit, withrow money and transfers between accounts.

## Technologies used
* Java 8
* Javalin
* Google Guice
* JPA, Hibernate
* H2 database
* Junit 4, Mockito
* REST-assured

## API documentation
REST endpoints

| Http method | Endpoint                                        | Sample request                                           | Description                                                    |
|-------------|-------------------------------------------------|---------------------------------------------------|----------------------------------------------------------------|
| POST        | /account                                        | {   "name": "account name",   "currency": "USD" } | Create new account                         |
| POST        | /account/{Id}/deposit                    | {   "amount": "10.01",   "currency": "USD"  }    | Deposit money to account found by id |
| POST        | /account/{id}/withdraw                   | {   "amount": "1000.00",   "currency": "EUR"  }    | Withdraw money from account found by id     |
| GET         | /account/{id}                    |                                                   | Get account details                                         |
| POST        | /account/{fromId}/transfer/{toId} | {   "amount": "120.00",   "currency": "GPB" }     | Transfer money from account fromId to account toId       |

When account is created, account will be return in the response. User can see all the details and use the id for other operations.

User can deposits / withdraw / transfer with different currencies than accounts currencies.
If the account is in USD, the operation in EUR  will be converted to USD, according to exchange rate EUR -> USD 

For testing purposes, application supports only 3 currecies
* USD 
* EUR 
* GPB
