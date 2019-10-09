package com.revolut.transfer.validator;

import io.javalin.http.Context;

public class PathParamValidator {
	public static Long getPathParamIfValid(Context ctx, String pathParam) {
		return ctx.pathParam(pathParam, Long.class)
			 .check(id -> id > new Long("0"), "Invalid " + pathParam + " parameter")
			 .getValue();
	}
}
