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
Exposed REST endpoints

| Http method | Endpoint                                 | Request                                           | Description                                                                                                                                                  
| POST        | /account                                 | {   "name": "account name",   "currency": "USD" } | Create new account with balance 0.00  |                        
| POST        | /account/{id}/deposit                    | {   "amount": "150.67",   "currency": "USD"  }    | Deposit money |
| POST        | /account/{id}/withdraw                   | {   "amount": "100.00",   "currency": "EUR"  }    | Withdraw request |
| GET         | /account/{id}                            |                                                   | Get account overview |
| POST        | /account/{fromId}/transfer/{toId} | {   "amount": "200.00",   "currency": "USD" }     | Transfer money request from account fromId to account toId       |

When account is created, account will be return in the response. User can see all the details and use the id for other operations.

User can deposits / withdraw / transfer with different currencies than accounts currencies.
If the account is in USD, the operation in EUR  will be converted to USD, according to exchange rate EUR -> USD 

For testing purposes, application supports only 3 currecies
* USD 
* EUR 
* GPB
