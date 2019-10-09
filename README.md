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


### POST /account  sample

request:
```json
{
	"currency": "USD",
	"name":"saving"
}
```

response:
```json
{
    "id": 2,
    "name": "saving",
    "currency": "USD",
    "balance": "0.00"
}
```

### POST /account/{id}/deposit   sample

request:
```json
{
	"currency": "EUR",
	"amount": "110.54"
}
```

response:
```json
{
    "id": 1,
    "name": "savings",
    "currency": "USD",
    "balance": "121.59"
}
```

### POST /account/{id}/withdraw  sample

request:
```json
{
	"currency": "EUR",
	"amount": "10.02"
}
```

response:
```json
{
    "id": 1,
    "name": "savings",
    "currency": "USD",
    "balance": "110.57"
}
```

### GET /account/{id}  sample

response:
```json
{
    "id": 1,
    "name": "savings",
    "currency": "USD",
    "balance": "110.57"
}
```

### POST /account/{fromId}/transfer/{toId}

request:
```json
{
	"currency": "EUR",
	"amount": 10.00
}
```

Sample response:
```json
{
    "message": "Transfer successeful",
    "fromAccount": 1,
    "toAccount": 2,
    "amount": "10.00",
    "currency": "EUR"
}
```

## Tests
Application code is covered with unit tests.
There are:
* API testing with REST Assured
* Unit tests with Junit 4
* Integration test with database

Each method has own test class

## Build and run
Application can be built with command
```
mvn clean package
```

Then, run the application with 
```
java -jar target\revolut-transfer.jar

```

