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

public class Customer extends Model {
	
	@Expose(serialize=false)
	public int customer_id;
	
	@Expose
	public String first_name;
	
	@Expose
	public String last_name;
	
	@Expose
	public String birthday;
	
	@Expose
	public String email1;
	
	@Expose
	public String email2;
	
	@Expose
	public String phone_home;
	
	@Expose
	public String phone_mobile;
	
	@Expose
	public boolean is_enabled;
	
	@Expose
	public boolean is_deleted;
	
	@Expose
	public String address;
	
	@Expose
	public String city;
	
	@Expose
	public float money_spent;
	
	@Expose
	public int card;
	
	@Expose
	public String registration_date;
	
	@Expose
	public String registration_time;
	
	@Expose
	public boolean is_individual;
	
	@Expose
	public String last_visit_date;
	
	@Expose
	public String last_visit_time;
	
	@Expose
	public boolean send_sms;
	
	@Expose
	public boolean send_email;
	
	@Expose
	public String country;
	
	@Expose
	public int postal_code;
	
	@Expose
	public int credit;
	
	@Expose 
	public int previous_credit;
	
	@Expose
	public int card_level_id;
	
	@Expose
	public boolean is_granted_topup;
	
	@Expose
	public boolean is_card_printed;
	
	@Expose
	public int registration_store_id;
	
	
	@Override
	public String getApiName() { return "zazu_customer"; }
	
	@Override
	public String getFields() {return "*"; }
	
	public int getId() { return this.customer_id; }
	
	public Customer setId(int id) { this.customer_id = id; return this; }
	
	public String getIdName() { return "customer_id"; }
}
