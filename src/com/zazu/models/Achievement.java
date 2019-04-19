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

public class Achievement extends Model {

	@Expose(serialize = false)
	public int achievement_id;
	
	@Expose
	public int min_previous_credits;
	
	@Expose
	public boolean is_enabled;
	
	@Expose
	public int position;
	
	@Expose
	public int card_level_id;
	
	
	@Override
	public String getApiName() { return "zazu_achievement"; }
	
	@Override
	public String getFields() {return "achievement_id,min_previous_credits,is_enabled,position,card_level_id"; }
	
	public int getId() { return this.achievement_id; }
	
	public void setId(int id) { this.achievement_id = id; }
	
	public String getIdName() { return "achievement_id"; }
	
	
}
