package com.revolut.transfer;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.revolut.transfer.controller.AccountController;
import com.revolut.transfer.di.PersistenceInitializer;
import com.revolut.transfer.di.ProductionModule;
import com.revolut.transfer.dto.RequestErrorRespose;
import com.revolut.transfer.exception.AccountDepositException;
import com.revolut.transfer.exception.AccountNotCreatedException;
import com.revolut.transfer.exception.AccountNotFoundException;
import com.revolut.transfer.exception.AccountOverdraftException;
import com.revolut.transfer.exception.ExchangeRateNotFoundException;

import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.plugin.json.JavalinJson;

public class App {

	private static final int PORT = 7000;

	public static void main(String[] args) {

		Javalin app = Javalin.create();
		Injector injector = Guice.createInjector(new ProductionModule());
		start(app, PORT, injector);
	}

	public static void start(Javalin app, int port, Injector injector) {
		app.start(port);
		injector.getInstance(PersistenceInitializer.class)
			.start();
		AccountController accController = injector.getInstance(AccountController.class);

		app.routes(() -> {
			path("/account", () -> {
				post(accController::create);
				path(":id", () -> {
					get(accController::getById);
					post("/deposit", accController::deposit);
					post("/withrow", accController::withrow);
				});
				post(":fromId/transfer/:toId", accController::transfer);
			});
		});

		Gson gson = new GsonBuilder().create();
		JavalinJson.setFromJsonMapper(gson::fromJson);
		JavalinJson.setToJsonMapper(gson::toJson);

		errorHandlingConfiguration(app);
	}

	public static void stop(Javalin app) {
		app.stop();
	}

	private static void errorHandlingConfiguration(Javalin app) {
		// TODO
		app.exception(AccountNotFoundException.class, (e, ctx) -> {
			ctx.json(new RequestErrorRespose(HttpStatus.NOT_FOUND_404, "Invalid Request. " + e.getMessage(),
					ctx.req.getPathInfo()))
				.status(HttpStatus.NOT_FOUND_404);
		});

		app.exception(AccountNotCreatedException.class, (e, ctx) -> {
			ctx.json(new RequestErrorRespose(HttpStatus.BAD_REQUEST_400, "Invalid Request. " + e.getMessage(),
					ctx.req.getPathInfo()))
				.status(HttpStatus.BAD_REQUEST_400);
		});

		app.exception(AccountDepositException.class, (e, ctx) -> {
			ctx.json(new RequestErrorRespose(HttpStatus.BAD_REQUEST_400, "Invalid Request. " + e.getMessage(),
					ctx.req.getPathInfo()))
				.status(HttpStatus.BAD_REQUEST_400);
		});

		app.exception(AccountNotCreatedException.class, (e, ctx) -> {
			ctx.json(new RequestErrorRespose(HttpStatus.BAD_REQUEST_400, "Invalid Request. " + e.getMessage(),
					ctx.req.getPathInfo()))
				.status(HttpStatus.BAD_REQUEST_400);
		});

		app.exception(BadRequestResponse.class, (e, ctx) -> {
			ctx.json(new RequestErrorRespose(HttpStatus.BAD_REQUEST_400, "Invalid Request. " + e.getMessage(),
					ctx.req.getPathInfo()))
				.status(HttpStatus.BAD_REQUEST_400);
		});
		
		app.exception(AccountOverdraftException.class, (e, ctx) -> {
			ctx.json(new RequestErrorRespose(HttpStatus.BAD_REQUEST_400, "Invalid Request. " + e.getMessage(),
					ctx.req.getPathInfo()))
				.status(HttpStatus.BAD_REQUEST_400);
		});
		
		app.exception(ExchangeRateNotFoundException.class, (e, ctx) -> {
			ctx.json(new RequestErrorRespose(HttpStatus.BAD_REQUEST_400, "Invalid Request. " + e.getMessage(),
					ctx.req.getPathInfo()))
				.status(HttpStatus.BAD_REQUEST_400);
		});
	}
}
