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


public class Manager extends Model {
	
	@Expose(serialize = false)
	public int manager_id;
	
	@Expose
	public String name;
	
	@Expose
	public String dashboard_login;
	
	@Expose
	public String dashboard_password;
	
	@Expose
	public boolean is_enabled;
	
	@Expose
	public String can_create_device;
	
	@Expose
	public boolean is_employee;
	
	@Expose
	public boolean is_admin;
	
	
	@Override
	public String getApiName() { return "zazu_manager"; }
	
	@Override
	public String getFields() {return "*"; }
	
	public int getId() { return this.manager_id; }
	
	public void setId(int id) { this.manager_id = id; }
	
	public String getIdName() { return "manager_id"; }
	
}
