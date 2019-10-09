package com.revolut.transfer.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.revolut.transfer.App;
import com.revolut.transfer.di.TestModule;
import com.revolut.transfer.dto.AccountRequest;
import com.revolut.transfer.dto.OperationRequest;
import com.revolut.transfer.enumeration.Currency;

import io.javalin.Javalin;
import io.javalin.plugin.openapi.annotations.ContentType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.mapper.ObjectMapperType;

public class AccountControllerTransferMethodShould {
	private static final int PORT = 5003;

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

		setInitialData();
	}

	@AfterClass
	public static void cleanUp() {
		App.stop(app);
	}
	
	@Test
	public void return_HttpNotFound_when_FromIdNotExist() {
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post("/account/10/transfer/2")
			.then()
			.statusCode(HttpStatus.NOT_FOUND_404);
	}
	
	@Test
	public void return_HttpNotFound_when_ToIdNotExist() {
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post("/account/1/transfer/20")
			.then()
			.statusCode(HttpStatus.NOT_FOUND_404);
	}
	
	@Test
	public void return_RequestErrorMessage_when_FromIdNotExist() {
		String path = "/account/10/transfer/2";
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post(path)
			.then()
			.body("code", equalTo(HttpStatus.NOT_FOUND_404))
			.body("message", notNullValue())
			.body("request", equalTo(path));
	}
	
	@Test
	public void return_RequestErrorMessage_when_ToIdNotExist() {
		String path = "/account/1/transfer/20";
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post(path)
			.then()
			.body("code", equalTo(HttpStatus.NOT_FOUND_404))
			.body("message", notNullValue())
			.body("request", equalTo(path));
	}
	
	@Test
	public void return_HttBadRequest_when_InvalidFromIdt() {
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post("/account/_10/transfer/2")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST_400);
	}
	
	@Test
	public void return_HttpBadRequest_when_InvalidToId() {
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post("/account/1/transfer/20+")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST_400);
	}
	
	@Test
	public void return_RequestErrorResponse_when_InvalidFromIdt() {
		String path = "/account/_10/transfer/2";
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post(path)
			.then()
			.body("code", equalTo(HttpStatus.BAD_REQUEST_400))
			.body("message", notNullValue())
			.body("request", equalTo(path));
	}
	
	@Test
	public void return_RequestErrorResponse_when_InvalidToId() {
		String path = "/account/1/transfer/20+";
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post(path)
			.then()
			.body("code", equalTo(HttpStatus.BAD_REQUEST_400))
			.body("message", notNullValue())
			.body("request", equalTo(path));
	}
	
	@Test
	public void return_TransferResponse_when_TransferIsValid() {
		
		String path = "/account/1/transfer/2";
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post(path)
			.then()
			.body("amount", equalTo("10.00"))
			.body("fromAccount", equalTo(1))
			.body("toAccount", equalTo(2))
			.body("currency", equalTo(Currency.EUR.toString()))
			.body("message", notNullValue());
	}
	
	@Test
	public void return_HttpOk_when_TransferIsValid() {	
		String path = "/account/1/transfer/2";
		given().body(new OperationRequest(Currency.EUR, "10.00"))
			.when()
			.post(path)
			.then()
			.statusCode(HttpStatus.OK_200);
	}

	private static void setInitialData() {
		given().body(new AccountRequest("test-savings", Currency.EUR))
			.when()
			.post("/account");

		given().body(new OperationRequest(Currency.EUR, "100.00"))
			.when()
			.post("/account/1/deposit/");

		given().body(new AccountRequest("test-current", Currency.USD))
			.when()
			.post("/account");

		given().body(new OperationRequest(Currency.EUR, "100.00"))
			.when()
			.post("/account/2/deposit/");
	}
}
