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

package com.zazu.utils;

import com.zazu.utils.Api.*;
import com.zazu.models.*;
import java.util.List;

public class LoginHandler {

	private String username;
	private String password;
	private String deviceCode;
	private String storeCode;
	private boolean response;
	private Manager manager;
	private Device device;
	private Store store;
	
	
	public LoginHandler(){;}
	
	public LoginHandler setUsername(String username) { this.username = username; return this; }
	
	public LoginHandler setPassword(String password) { this.password = password; hashPassword(); return this; }
	
	public LoginHandler setStoreCode(String storeCode) { this.storeCode = storeCode; return this; }
	
	public LoginHandler setDeviceCode(String deviceCode) { this.deviceCode = deviceCode; return this; }
	
	public Device getDevice() { return this.device; }
	
	public Store getStore() { return this.store; }
	
	
	private void hashPassword() {
		Hash hash = new Hash()
				.setWord(this.password);
		this.password = hash.getHash();
	}
	
	/**
	 * Get Device Object from code
	 * 
	 * @return Device response success
	 */
	public boolean getDeviceResponse() {
		System.out.println("LoginHandler: getDeviceResponse");
		GetClient getClient = new GetClient();
		getClient.setModel( new Device() );
		getClient.setFilter("(code=" + this.deviceCode + ")and(is_enabled=1)&limit=1");
		getClient.execute();
		
		try {
			List<Device> deviceList = (List)getClient.getObject();
			if(deviceList.size()>0) {
				for(Device device : deviceList) {
					System.out.println("Device Successfuly found:"+device.code);
					this.device = device;
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return false;
	}
	
	/**
	 * Get Store object from code
	 * 
	 * @return Store response success
	 */
	public boolean getStoreResponse() {
		System.out.println("LoginHandler: getStoreResponse");
		GetClient getClient = new GetClient();
		getClient.setModel( new Store() );
		getClient.setFilter("(code=" + this.storeCode + ")and(is_enabled=1)&limit=1");
		getClient.execute();
		
		try {
			List<Store> storeList = (List)getClient.getObject();
			if(storeList.size()>0) {
				for(Store store : storeList) {
					System.out.println("Store Successfuly found:"+store.code);
					this.store = store;
					return true;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Get 
	 * 
	 * @return
	 */
	protected void getApiResponse() {
		
		Manager managerModel = new Manager();
		managerModel.manager_id = 0;
		
		this.response = false;
		
		try {
			
			GetClient getClient = new GetClient();
			getClient.setFilter("(dashboard_login="+username+")and(dashboard_password="+password+")");
			getClient.setModel(managerModel);
			getClient.execute();
			List<Manager> managerList = (List) getClient.getObject();
			
			if(managerList.size()>0) {
				for(Manager manager : managerList) {
					this.manager = manager;
					this.response = true;
				}
				
				System.out.println("LoginHandler: response(after manager): " + this.response + ".");
				
				this.response = this.response && getDeviceResponse();
				System.out.println("LoginHandler: response(after device): " + this.response + ".");
				
				this.response = this.response && getStoreResponse();
				System.out.println("LoginHandler: response(after store): " + this.response + ".");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Execute Login Handling
	 */
	public void execute() {
		
		getApiResponse();
		
		Manager manager = getManager();
		
		if (manager.manager_id>0) {
			System.out.println(manager.name + " " + manager.manager_id);
		} else {
			System.out.println("LoginHandler: Manager not found.");
		}
	}
	
	public boolean getResponse() { return this.response; }
	
	public Manager getManager() { return this.manager; }
	
}
