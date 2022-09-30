package com.example.demo.bean;

import java.util.Date;

public class Transactions {
	private int id;
	private String name;
	private String type;
	private int amount;
	private int amount1;
	private int amount2;
	private int amount3;
	private String isArchived;
	private Date insertedAt;
	private String insertedBy;
	private Date updatedAt;
	private String updatedBy;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getAmount1() {
		return amount1;
	}

	public void setAmount1(int amount1) {
		this.amount1 = amount1;
	}
	
	public int getAmount2() {
		return amount2;
	}

	public void setAmount2(int amount2) {
		this.amount2 = amount2;
	}
	
	public int getAmount3() {
		return amount3;
	}

	public void setAmount3(int amount3) {
		this.amount3 = amount3;
	}

	public String getIsArchived() {
		return isArchived;
	}

	public void setIsArchived(String isArchived) {
		this.isArchived = isArchived;
	}
	
	public Date getInsertedAt() {
		return insertedAt;
	}

	public void setInsertedAt(Date insertedAt) {
		this.insertedAt = insertedAt;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public String getInsertedBy() {
		return insertedBy;
	}

	public void setInsertedBy(String insertedBy) {
		this.insertedBy = insertedBy;
	}
	
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
}
