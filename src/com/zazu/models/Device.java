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


public class Device extends Model {
	
	@Expose(serialize = false)
	public int device_id;
	
	@Expose
	public String code;
	
	@Expose
	public String login;
	
	@Expose
	public String password;
	
	@Expose
	public boolean is_enabled;
	
	@Expose
	public String api_key;
	
	@Expose
	public int store_id;
	
	
	@Override
	public String getApiName() { return "zazu_device"; }
	
	@Override
	public String getFields() {return "*"; }
	
	public int getId() { return this.device_id; }
	
	public void setId(int id) { this.device_id = id; }
	
	public String getIdName() { return "device_id"; }
	
}
