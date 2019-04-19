package com.zazu.jcore;

import com.zazu.models.Transaction;
import java.util.List;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.zazu.utils.Api.GetClient;
import com.zazu.utils.Api.PostClient;
import com.zazu.utils.Api.DeleteClient;


/**
 * !Not Implemented!
 * Handles sales statistics
 * @author Konstantinos Chatzis <kachatzis@ece.auth.gr>
 */
public class StatsSalesHandler {
	
	private List<Transaction> sales;
	private boolean DEBUG;
	private List<String> periods;
	
	StatsSalesHandler(){
		sales = new ArrayList<Transaction>();
		periods = new ArrayList<String>();
		DEBUG = true;
	}
	
	public void handle() {
		createPeriods();
		retrieveSales();
		
	}
	
	
	private void createPeriods() {
		
	}
	
	private void retrieveSales() {
		GetClient getClient = new GetClient();
		getClient.setModel( new Transaction() );
		getClient.setFilter( "&" );
	}
	
	private String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Athens"));
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	
	private String getCurrentTime() {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		timeFormat.setTimeZone(TimeZone.getTimeZone("Europe/Athens"));
		Date time = new Date();
		return timeFormat.format(time);
	}
	
}
