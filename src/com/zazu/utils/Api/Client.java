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

package com.zazu.utils.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zazu.config.ApiConfig;
import com.zazu.models.*;
import java.util.ArrayList;
import java.util.List;



public class Client {
	
	// Get Api Configuration
	ApiConfig config = new ApiConfig();
	
	protected String apiKey = config.API_KEY;
	protected String baseUrl = config.BASE_URL;
	protected String data;
	protected ArrayList<Object> object = new ArrayList();
	protected Model model;
	protected String modelName;
	protected String fields;
	protected Resource resource;
	protected String idName;
	protected int id;
	protected String filter;
	protected int responseCode;
	
	public static boolean DEBUG = false;
	
	public Client() {
		this.data = "";
	}
	
	public Client(Model model) {
		this.model = model;
		//setFields();
	}
	
	public Client setData(String data) {
		this.data = data;
		return this;
	}
	
	protected void objectToData() {
		GsonBuilder gsonBuilder = new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation();
		Gson gson = gsonBuilder.create();
		this.data = gson.toJson(this.object);
		
		this.data = createResourceTag(this.data);
		//System.out.println("ObjectToData()"+this.data);
	}
	
	
	protected String createResourceTag(String data) {
		data = "{\"resource\":" + data + "}";
		return data;
	}
	
	protected String[] getDatas(String data) {
		data = data.replace("]}{{end}}", "");
		data = data.replace("{\"resource\":[", "");
		
		String[] datas = data.split("(?<=},)");
		
		for(int i=0; i<datas.length; i++) {
			datas[i] = datas[i].replace("},", "}");
		}
		
		return datas;
	}	
	
	protected void dataToObject() {

		String[] datas = getDatas(this.data);
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		
		this.object.removeAll(this.object);
		this.object.clear();
		
		for(String data : datas) {
			if( data.length()>=1 ) {
				Object object = gson.fromJson(data, this.model.getClass());
				this.object.add(object);
			}
		}
		 
	}
	
	public List<Object> getObject() { return this.object; }
	
	public Client setObject(Object object) {
		this.object.add( object );
		this.modelName = ((Model)object) . getApiName();
		this.model = (Model) object;
		objectToData();
		
		return this;
	}
	
	public void debugClient() {
		System.out.println(this.data);
	}
	
	/**
	 * Set a model to relate and form the response. 
	 * 
	 * @param model Model-Object the client will relate the response to
	 */
	public Client setModel(Model model) {
		this.model = model;
		this.modelName = model.getApiName();
		setFields();
		
		return this;
	}
	
	/**
	 * Set fields the response should contain.
	 * This method is called by the user for custom response.
	 * 
	 * @param fields
	 */
	//public Client setFields(String fields) { this.fields = fields; return this; }
	
	/**
	 * Set fields the response should contain.
	 * This method is called by the client automatically on creation,
	 * and it sets the fields specified in the model.
	 */
	protected void setFields() { this.fields = model.getFields(); }
	
	
	public Client setFilter(String filter) { this.filter = filter; return this; }
	
	
	public int getResponseCode() { return this.responseCode; }
	
	
}
