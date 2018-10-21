package com.vo;

import com.view.OperationType;

/**
 * @author edgar.luis
 */
public class VOAdjustments {

	private String product;
	private float value;
	private OperationType operation;

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public OperationType getOperation() {
		return operation;
	}

	public void setOperation(OperationType operation) {
		this.operation = operation;
	}

}
