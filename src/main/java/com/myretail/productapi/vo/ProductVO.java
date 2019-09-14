package com.myretail.productapi.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ProductVO {
	@JsonProperty("id")
	private long id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("current_price")
	private PriceVO priceVO;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public PriceVO getPriceVO() {
		return priceVO;
	}
	public void setPriceVO(PriceVO priceVO) {
		this.priceVO = priceVO;
	}
	
	@Override
	public String toString() {
		return "ProductVO [id=" + id + ", "
				+ (name != null ? "name=" + name + ", " : "")
				+ (priceVO != null ? "priceVO=" + priceVO : "") + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((name == null) ? 0 : name
						.hashCode());
		result = prime * result + ((priceVO == null) ? 0 : priceVO.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductVO other = (ProductVO) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (priceVO == null) {
			if (other.priceVO != null)
				return false;
		} else if (!priceVO.equals(other.priceVO))
			return false;
		if (id != other.id)
			return false;
		return true;
	}	
}
