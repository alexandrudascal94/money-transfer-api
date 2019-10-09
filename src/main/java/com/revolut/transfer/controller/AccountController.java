package com.revolut.transfer.controller;

import static com.revolut.transfer.validator.AccountValidator.getAccountRequestIfValid;
import static com.revolut.transfer.validator.AccountValidator.getOperationRequestIfValid;
import static com.revolut.transfer.validator.PathParamValidator.getPathParamIfValid;

import org.eclipse.jetty.http.HttpStatus;

import com.google.inject.Inject;
import com.revolut.transfer.dto.AccountRequest;
import com.revolut.transfer.dto.OperationRequest;
import com.revolut.transfer.dto.TransferResponse;
import com.revolut.transfer.formater.MoneyParser;
import com.revolut.transfer.mapper.AccountMapper;
import com.revolut.transfer.model.Account;
import com.revolut.transfer.service.AccountService;

import io.javalin.http.Context;

public class AccountController {


	private AccountService accountService;
	
	@Inject
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	public void create(Context ctx) {
		AccountRequest newAccount = getAccountRequestIfValid(ctx);

		Account account = accountService.create(AccountMapper.toAccount(newAccount));

		ctx.json(AccountMapper.toAccountReponse(account))
			.status(HttpStatus.CREATED_201);
	}

	public void getById(Context ctx) {
		long accountId = getPathParamIfValid(ctx, ":id");

		Account account = accountService.findById(accountId);

		ctx.json(AccountMapper.toAccountReponse(account)).status(HttpStatus.OK_200);
	}

	public void deposit(Context ctx) {
		long accountId = getPathParamIfValid(ctx, ":id");
		OperationRequest deposit = getOperationRequestIfValid(ctx);

		Account account = accountService.deposit(accountId, MoneyParser.parse(deposit.amount), deposit.currency);

		ctx.json(AccountMapper.toAccountReponse(account))
			.status(HttpStatus.OK_200);
	}

	public void withrow(Context ctx) {
		long accountId = getPathParamIfValid(ctx, ":id");
		OperationRequest withrow = getOperationRequestIfValid(ctx);

		Account account = accountService.withrow(accountId, MoneyParser.parse(withrow.amount), withrow.currency);

		ctx.json(AccountMapper.toAccountReponse(account))
			.status(HttpStatus.OK_200);
	}

	public void transfer(Context ctx) {
		long fromId = getPathParamIfValid(ctx, ":fromId");
		long toId = getPathParamIfValid(ctx, ":toId");
		OperationRequest transfer = getOperationRequestIfValid(ctx);

		accountService.transfer(fromId, toId, MoneyParser.parse(transfer.amount), transfer.currency);
		TransferResponse message = new TransferResponse("Transfer successefull", fromId, toId, transfer.amount,
				transfer.currency);

		ctx.json(message)
			.status(HttpStatus.OK_200);
	}
}
