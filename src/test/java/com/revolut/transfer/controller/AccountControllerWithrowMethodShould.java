package com.revolut.transfer.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.revolut.transfer.App;
import com.revolut.transfer.di.TestModule;
import com.revolut.transfer.dto.AccountRequest;
import com.revolut.transfer.dto.AccountResponse;
import com.revolut.transfer.dto.OperationRequest;
import com.revolut.transfer.enumeration.Currency;
import com.revolut.transfer.formater.MoneyParser;

import io.javalin.Javalin;
import io.javalin.plugin.openapi.annotations.ContentType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.mapper.ObjectMapperType;

public class AccountControllerWithrowMethodShould {

	private static final int PORT = 5004;

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

		seInitialData();
	}

	@AfterClass
	public static void cleanUp() {
		App.stop(app);
	}

	@Test
	public void return_HttpBadRequest_when_InvalidAccountId() {
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post("/account/-1s/withrow")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST_400);
	}

	@Test
	public void return_HttpNotFound_when_IdNotExixt() {
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post("/account/10/withrow")
			.then()
			.statusCode(HttpStatus.NOT_FOUND_404);
	}

	@Test
	public void return_RequestErrorResponse_when_InvalidAccountId() {
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post("/account/-1s/withrow")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST_400);
	}

	@Test
	public void return_RequestErrorResponse_when_IdNotExit() {
		String path = "/account/10/withrow";
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post(path)
			.then()
			.body("code", equalTo(HttpStatus.NOT_FOUND_404))
			.body("message", notNullValue())
			.body("request", equalTo(path));
	}

	@Test
	public void return_HttOk_when_areEnoughMoney() {
		given().body(new OperationRequest(Currency.EUR, "200.00"))
			.when()
			.post("/account/1/deposit");

		String path = "/account/1/withrow";
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post(path)
			.then()
			.statusCode(HttpStatus.OK_200);
	}

	@Test
	public void return_CorretcBalance_when_areEnoughMoney() {
		String balance = given().body(new OperationRequest(Currency.EUR, "200.00"))
			.when()
			.post("/account/1/deposit")
			.getBody()
			.as(AccountResponse.class).balance;

		String path = "/account/1/withrow";
		BigDecimal expectedResul = MoneyParser.parse(balance)
			.subtract(MoneyParser.parse("10.00"));

		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post(path)
			.then()
			.body("balance", equalTo(expectedResul.toString()));
	}

	private static void seInitialData() {
		given().body(new AccountRequest("test-savings", Currency.EUR))
			.when()
			.post("/account");

		given().body(new AccountRequest("test-current", Currency.USD))
			.when()
			.post("/account");
	}
}
