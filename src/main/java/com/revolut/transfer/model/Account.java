package com.revolut.transfer.model;

import java.math.BigDecimal;
import java.math.MathContext;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.revolut.transfer.enumeration.Currency;

@Entity
@Table(name = "ACCOUNT")
public class Account {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name")
	@Nonnull
	private String name;

	@Column(name = "currency")
	@Nonnull
	private Currency currency;

	@Column(name = "amount")
	private BigDecimal balance;

	public Long getId() {
		return id;
	}

	public Currency getCurrency() {
		return currency;
	}
	
	public String getName() {
		return name;
	}
	
	public BigDecimal getBalance() {
		return this.balance;
	}
	
	public Account() {
		
	}

	public Account(String name, Currency currency, BigDecimal balance) {
		this.name = name;
		this.currency = currency;
		this.balance = balance;		
	}
	
	public Account(Long id, String name, Currency currency, BigDecimal balance) {
		this.id = id;
		this.name = name;
		this.currency = currency;
		this.balance = balance;		
	}
	
	public Account addBalance(BigDecimal amount) {
		return new Account(this.id, this.name, this.currency, this.balance.add(amount, new MathContext(5)));
	}
	
	public Account subtractBalance(BigDecimal amount) {
		return new Account(this.id, this.name, this.currency, this.balance.subtract(amount));
	}
}
