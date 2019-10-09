package com.revolut.transfer.validator;

import java.math.BigDecimal;

import com.revolut.transfer.dto.AccountRequest;
import com.revolut.transfer.dto.OperationRequest;

import io.javalin.http.Context;

public class AccountValidator {
	public static AccountRequest getAccountRequestIfValid(Context ctx) {
		return ctx.bodyValidator(AccountRequest.class)
			.check(it -> it.currency != null, "Invalid or Null Currency")
			.check(it -> it.name != null, "Null account name")
			.get();
	}

	public static OperationRequest getOperationRequestIfValid(Context ctx) {
		return ctx.bodyValidator(OperationRequest.class)
			.check(it -> it.currency != null, "Invalid or Null Currency")
			.check(it -> new BigDecimal(it.amount).compareTo(new BigDecimal("0.01")) > 0,
					"Transfered amount less then 0.01")
			.get();
	}
}
