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

public class Good extends Model {
	
	@Expose(serialize = false)
	public int good_id;
	
	@Expose
	public int card_level_id;
	
	@Expose
	public int min_credits;
	
	@Expose
	public boolean is_enabled;
	
	@Expose
	public String title;
	
	@Expose
	public boolean store_is_enabled;
	
	@Expose
	public String unit;
	
	@Expose
	public boolean is_gift;
	
	@Expose
	public float price;
	
	@Expose
	public int credits_cost;
	
	
	@Override
	public String getApiName() { return "zazu_good"; }
	
	@Override
	public String getFields() {return "good_id,card_level_id,min_credits,is_enabled,title,store_is_enabled,unit,is_gift,price,credits_cost"; }
	
	public int getId() { return this.good_id; }
	
	public void setId(int id) { this.good_id = id; }
	
	public String getIdName() { return "good_id"; }
	
	@Override
	public String toString() { return title + " (" + unit + ")"; }
	
}
