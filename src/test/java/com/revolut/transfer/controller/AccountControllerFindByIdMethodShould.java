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
import com.revolut.transfer.enumeration.Currency;

import io.javalin.Javalin;
import io.javalin.plugin.openapi.annotations.ContentType;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.mapper.ObjectMapperType;

public class AccountControllerFindByIdMethodShould {

	private static final int PORT = 5002;

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
	public void return_HttpOk_when_Found() {
		given().when()
			.get("/account/1")
			.then()
			.statusCode(HttpStatus.OK_200);
	}
	
	@Test
	public void return_HttpNotFound_when_AccountNotFound() {
		given().when()
			.get("/account/100")
			.then()
			.statusCode(HttpStatus.NOT_FOUND_404);
	}
	
	@Test
	public void return_requestErrorResponse_when_AccountNotFound() {
		String path = "/account/100";
		given().when()
			.get(path)
			.then()
			.body("code", equalTo(HttpStatus.NOT_FOUND_404))
			.body("message", notNullValue())
			.body("request", equalTo(path));
	}
	
	@Test
	public void return_HttpBadRequest_when_InvalidId() {
		given().when()
			.get("/account/a100_")
			.then()
			.statusCode(HttpStatus.BAD_REQUEST_400);
	}
	
	@Test
	public void return_requestErrorResponse_when_InvalidId() {
		String path = "/account/a100_";
		given().when()
			.get(path)
			.then()
			.body("code", equalTo(HttpStatus.BAD_REQUEST_400))
			.body("message", notNullValue())
			.body("request", equalTo(path));
	}

	private static void setInitialData() {
		given().body(new AccountRequest("test-savings", Currency.EUR))
			.when()
			.post("/account");

		given().body(new AccountRequest("test-current", Currency.USD))
			.when()
			.post("/account");
	}
}
