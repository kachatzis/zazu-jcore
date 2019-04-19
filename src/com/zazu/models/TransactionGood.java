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

import com.google.gson.annotations.Expose;

public class TransactionGood extends Model {
	
	@Expose(serialize=false)
	public int good_bought_id;
	
	@Expose
	public int transaction_id;
	
	@Expose
	public int customer_id;
	
	@Expose
	public int good_id;
	
	@Expose
	public float cost;
	
	@Expose
	public boolean is_canceled;
	
	@Expose
	public boolean is_processed;
	
	
	@Override
	public String getApiName() { return "zazu_transaction_good"; }
	
	@Override
	public String getFields() {return "*"; }
	
	public int getId() { return this.good_bought_id; }
	
	public void setId(int id) { this.good_bought_id = id; }
	
	public String getIdName() { return "good_bought_id"; }
}
