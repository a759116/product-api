package com.myretail.productapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="products")
public class Price {
	@Id
	private long id;
	
	private String currencyCode;
	private double value;
	
	public Price() {}

	public Price(Long id, String currencyCode, double value) {
		this.id = id;
		this.currencyCode = currencyCode;
		this.value = value;
	}

	public long getId() {
		return id;
	}

	public void setId(long productId) {
		this.id = productId;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Price [id="
				+ id
				+ ", "
				+ (currencyCode != null ? "currencyCode=" + currencyCode + ", "
						: "") + "value=" + value + "]";
	}
	
}
