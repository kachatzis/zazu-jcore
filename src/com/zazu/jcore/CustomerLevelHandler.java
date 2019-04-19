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

package com.zazu.jcore;

import java.util.List;
import java.util.ArrayList;
import com.zazu.utils.Api.GetClient;
import com.zazu.utils.Api.PatchClient;
import com.zazu.models.Customer;
import com.zazu.models.Achievement;


public class CustomerLevelHandler {
	
	private List<Achievement> achievements;
	private List<Customer> customers;
	
	
	public CustomerLevelHandler() {
		this.achievements = new ArrayList<Achievement>();
		this.customers = new ArrayList<Customer>();
		this.handle();
	}
	
	
	/**
	 * Handle Customer Levels
	 */
	private void handle() {
		System.out.println("CustomerLevelHandler:");
		
		getAchievements();
		getCustomers();
		
		scanCustomers();
	}

	
	/**
	 * Retrieve available Credit Offers
	 */
	private void getAchievements() {
		GetClient getClient = new GetClient();
		getClient.setModel( new Achievement() );
		getClient.setFilter( "(is_enabled=1)and(position>=0)and(min_previous_credits>=0)&sort=position" );
		getClient.execute();
		
		if ( getClient.getResponseCode() == 200 && !(getClient.getObject().isEmpty()) ) {
			this.achievements = (List) getClient.getObject();
		}
		System.out.println(" >Retrieved Achievements: " + this.achievements.size());
		
		printAchievements();
	}
	
	
	/**
	 * Scan Each Customer for new Achievements and handle levels
	 */
	private void scanCustomers() {
		System.out.println(" >Scanning "+customers.size()+" Customers.");
		for ( Customer customer : customers ) {
			boolean customerChanged = false;
			analyzeCustomer( customer , customerChanged );
			if ( customerChanged )	
				updateCustomer( customer );
		}
	}
	
	
	/**
	 * Analyze Customer Achievements
	 * @param customer
	 * @param customerChanged
	 */
	private void analyzeCustomer( Customer customer, boolean customerChanged ) {
		//System.out.println("  >Analyzing Customer: " + customer.customer_id);
		
		Achievement currentAchievement = getCurrentCustomerAchievement( customer );
		if ( currentAchievement.card_level_id != customer.card_level_id ) {
			customerChanged = true;
			changeCustomerAchievement( customer , currentAchievement );
			//System.out.println("  >Customer Changed: True");
			updateCustomer( customer );
		} else {
			//System.out.println("  >Customer Changed: False");
		}
		
	}
	
	/**
	 * Changes Customer Achievment
	 * More Validation to be added
	 * @param customer
	 * @param achievement
	 */
	private void changeCustomerAchievement( Customer customer , Achievement achievement ) {
		customer.card_level_id = achievement.card_level_id;
	}
	
	
	/**
	 * Retrieve current achievement
	 * @param customer
	 * @return
	 */
	private Achievement getCurrentCustomerAchievement( Customer customer ) {
		Achievement currentAchievement = new Achievement();
		
		for ( Achievement achievement : achievements ) {
			if ( customer.previous_credit >= achievement.min_previous_credits ) {
				currentAchievement = achievement;
			}
		}
		//System.out.println("  >Current Achievement: " + currentAchievement.achievement_id);
		
		return currentAchievement;
	}
	
	/**
	 * Update Customer
	 */
	private void updateCustomer( Customer customer ) {
		PatchClient patchClient = new PatchClient();
		patchClient.setObject( customer );
		patchClient.setId( customer.customer_id );
		patchClient.execute();
		
		if ( patchClient.getResponseCode() == 200 ) {
			System.out.println( "  >Update Succeeded (200)" );
		} else {
			System.out.println( "  >Update Failed (" + patchClient.getResponseCode() + ")" );
		}
	}
	
	
	/**
	 * Retrieves the list of available customers
	 */
	private void getCustomers() {
		GetClient getClient = new GetClient();
		getClient.setModel( new Customer() );
		getClient.setFilter( "(is_enabled=1)and(previous_credit>0)and(is_deleted=0)" );
		getClient.execute();
		
		if ( getClient.getResponseCode()==200 && !(getClient.getObject().isEmpty()) ) {
			this.customers = (List) getClient.getObject();
		}
		System.out.println(" > Retrieved Customers: " + this.customers.size());
	}
	
	
	private void printAchievements() {
		System.out.println(" Achievements:");
		for ( Achievement achievement : achievements ) {
			System.out.println("  " + achievement.achievement_id);
		}
	}
}
