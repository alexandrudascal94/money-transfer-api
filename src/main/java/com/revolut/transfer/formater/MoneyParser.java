package com.revolut.transfer.formater;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyParser {

	private final static int DECIMALS = 2;

	public static BigDecimal parse(String amount) {
		
		return new BigDecimal(amount).setScale(DECIMALS, RoundingMode.HALF_DOWN);
	}
}
