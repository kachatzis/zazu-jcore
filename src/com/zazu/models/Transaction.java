/*******************************************************************************
 * Copyright (C) 2018 Konstantinos Chatzis - All Rights Reserved
 * 
 * Licensed Under:
 * Creative Commons Attribution-NoDerivatives 4.0 International Public License
 *  
 * You must give appropriate credit, provide a link to the license, and indicate 
 * if changes were made. You may do so in any reasonable manner, but not in 
 * any way that suggests the licensor endorses you or your use. If you remix, 
 * transform, or build upon the material, you may not distribute the modified material. 
 * 
 * Konstantinos Chatzis <kachatzis@ece.auth.gr>
 ******************************************************************************/

package com.zazu.models;

import com.google.gson.annotations.*;

public class Transaction extends Model {
	
	@Expose(serialize=false)
	public int transaction_id;
	
	@Expose
	public int device_id;
	
	@Expose
	public int credit;
	
	@Expose
	public int positive_credit;
	
	@Expose
	public String note;
	
	@Expose
	public String transaction_date;
	
	@Expose
	public String request_date;
	
	@Expose
	public int previous_card_credit;
	
	@Expose
	public boolean is_rolled_back;
	
	@Expose
	public float money_spent;
	
	@Expose
	public String transaction_time;
	
	@Expose
	public String request_time;
	
	@Expose
	public int store_id;
	
	@Expose
	public int manager_id;
	
	@Expose
	public int customer_id;
	
	@Expose
	public boolean is_processed;
	
	@Expose
	public boolean is_canceled;
	
	@Expose
	public boolean is_topup;
	
	@Expose
	public int code;
	
	
	@Override
	public String getApiName() { return "zazu_transaction"; }
	
	@Override
	public String getFields() {return "*"; }
	
	public int getId() { return this.transaction_id; }
	
	public void setId(int id) { this.transaction_id = id; }
	
	public String getIdName() { return "transaction_id"; }
}
