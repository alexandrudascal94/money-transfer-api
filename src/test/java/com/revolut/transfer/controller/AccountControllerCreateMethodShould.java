package com.revolut.transfer.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.revolut.transfer.App;
import com.revolut.transfer.di.TestModule;
import com.revolut.transfer.dto.AccountRequest;
import com.revolut.transfer.enumeration.Currency;

import io.javalin.Javalin;
import io.javalin.plugin.openapi.annotations.ContentType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.mapper.ObjectMapperType;

public class AccountControllerCreateMethodShould {

	private static final int PORT = 5000;

	private static Javalin app = Javalin.create();

	@BeforeClass
	public static void setUp() {
		App.start(app, PORT, Guice.createInjector(new TestModule()));

		RestAssured.port = PORT;
		RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON)
			.setAccept(ContentType.JSON)
			.build();

		RestAssured.config()
			.objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.GSON));
	}

	@AfterClass
	public static void cleanUp() {
		App.stop(app);
	}

	@Test
	public void return_HttpSuccess_when_DepositRequestIsValid() {
		given().body(new AccountRequest("test-savings", Currency.EUR))
			.when()
			.post("/account")
			.then()
			.statusCode(HttpStatus.CREATED_201);
	}

	@Test
	public void have_InitialBalanceZero_when_AccountRequestIsValid() {
		given().body(new AccountRequest("test-savings", Currency.EUR))
			.when()
			.post("/account")
			.then()
			.body("balance", equalTo("0.00"));
	}

	@Test
	public void have_Id_when_AccountRequestIsValid() {
		given().body(new AccountRequest("test-savings", Currency.EUR))
			.when()
			.post("/account")
			.then()
			.body("$", hasKey("id"))
			.body("id", notNullValue());
	}

	@Test
	public void have_requestedCurrency_when_AccountRequestIsValid() {
		given().body(new AccountRequest("test-savings", Currency.EUR))
			.when()
			.post("/account")
			.then()
			.body("currency", equalTo(Currency.EUR.toString()));
	}

	@Test
	public void have_requestedAccountName_when_AccountRequestIsValid() {
		String accountName = "test-savings";
		given().body(new AccountRequest(accountName, Currency.EUR))
			.when()
			.post("/account")
			.then()
			.body("name", equalTo(accountName));
	}

	@Test
	public void return_HttpBadRequest_when_AccountRequestHasNoName() {
		given().body(new AccountRequest(null, Currency.EUR))
			.when()
			.post("/account")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST_400);
	}

	@Test
	public void return_HttpBadRequest_when_AccountRequestHasNoCurrency() {
		given().body(new AccountRequest("test-savings", null))
			.when()
			.post("/account")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST_400);
	}

	@Test
	public void return_HttpBadRequest_when_AccountRequestHasInvalidCurrency() {
		String jsonBody = "{name: test-savings; currency: us}";
		given().body(jsonBody)
			.when()
			.post("/account")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST_400);
	}

	@Test
	public void return_RequestErrorResponse_when_AccountRequestIsInvalid() {
		String path = "/account";
		given().body(new AccountRequest("test-savings", null))
			.when()
			.post(path)
			.then()
			.statusCode(HttpStatus.BAD_REQUEST_400)
			.body("code", equalTo(HttpStatus.BAD_REQUEST_400))
			.body("message", notNullValue())
			.body("request", equalTo(path));
	}
}
