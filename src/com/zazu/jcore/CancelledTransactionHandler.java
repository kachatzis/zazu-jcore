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
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.zazu.utils.Api.GetClient;
import com.zazu.utils.Api.PatchClient;
import com.zazu.models.CreditOffer;
import com.zazu.models.Customer;
import com.zazu.models.Good;
import com.zazu.models.Transaction;
import com.zazu.models.TransactionGood;



/**
 * Processes cancelled (processed) transactions
 */
public class CancelledTransactionHandler {

	private List<Transaction> transactions;
	
	public CancelledTransactionHandler() {
		System.out.println("  Starting " + getCurrentTime() );
		initTransactions();
		handleTransactions();
	}
	
	
	private void initTransactions() {
		this.transactions = new ArrayList<Transaction>();
		
		GetClient getClient = new GetClient();
		getClient.setModel( new Transaction() );
		getClient.setFilter( "(is_processed=1)and(is_canceled=1)and(is_rolled_back=0)&limit=10&sort=request_date" );
		getClient.execute();
		
		if ( getClient.getResponseCode()==200 && !(getClient.getObject()).isEmpty()) {
			this.transactions = (List) getClient.getObject();
		}
		
		System.out.println("CancelledTransactionHandler: Initialized "+this.transactions.size()+" Transactions.");
	}
	
	
	private void handleTransactions() {
		for (Transaction transaction : this.transactions) {
			if (transaction.transaction_id>0) {
				System.out.println("  Cancelling (Handling) Transaction "+transaction.transaction_id+".");
				handleTransaction( transaction );
			}
		}
	}
	
	
	private void handleTransaction( Transaction transaction ) {
		try {
			Customer customer = getCustomer(transaction);
			List<TransactionGood> transactionGoods = getTransactionGoods(transaction);
			
			if (customer.getId()<1) {
				System.out.println("  Customer not found. Exiting.");
				return;
			}
			if (transactionGoods.size()<1 && !transaction.is_topup) {
				System.out.println("  Products required and not found. Exiting.");
				return;
			}
			System.out.println("  Continuing...");
			
			cancelTransaction( transaction );
			cancelTransactionCustomer( customer, transaction );
			
			if(!transaction.is_topup)
				cancelTransactionGoods( transactionGoods );
			
			updateTransaction( transaction );
			updateTransactionCustomer( customer );
			updateTransactionGoods( transactionGoods );
		}catch(NullPointerException e) {
			System.out.println("Null Pointer Exception at handleTransaction.");
			e.printStackTrace();
		}
	}
	
	
	private void cancelTransaction( Transaction transaction ) {
		transaction.is_canceled = true;
		transaction.is_processed = true;
		transaction.is_rolled_back = true;
	}
	
	
	private void cancelTransactionCustomer( Customer customer, Transaction transaction ) {
		customer.credit -= transaction.credit;
		if (customer.credit<0) {
			customer.credit = 0;
		}
		if(transaction.credit > 0) {
			customer.previous_credit -= Math.abs(transaction.credit);
			if (customer.previous_credit<0) {
				customer.previous_credit = 0;
			}
		}
		
		customer.money_spent -= transaction.money_spent;
		if (customer.money_spent<0) {
			customer.money_spent = 0;
		}
		
	}
	
	
	private void cancelTransactionGoods( List<TransactionGood> transactionGoods ) {
		for (TransactionGood good : transactionGoods) {
			good.is_canceled = true;
			good.is_processed = true;
		}
	}
	
	
	private void updateTransaction( Transaction transaction ) {
		PatchClient patchClient = new PatchClient();
		patchClient.setObject( transaction );
		patchClient.setId(transaction.getId());
		patchClient.execute();
		
		System.out.println("  Transaction Updated");
	}
	
	
	private void updateTransactionCustomer( Customer customer ) {
		PatchClient patchClient = new PatchClient();
		patchClient.setObject( customer );
		patchClient.setId(customer.getId());
		patchClient.execute();
		
		System.out.println("  Customer Updated");
	}
	
	
	private void updateTransactionGoods( List<TransactionGood> transactionGoods ) {
		for (TransactionGood good : transactionGoods) {
			PatchClient patchClient = new PatchClient();
			patchClient.setObject( good );
			patchClient.setId(good.transaction_id);
			patchClient.execute();
		}
		System.out.println("  Products Updated");
	}
	
	
	private Customer getCustomer( Transaction transaction ) {
		GetClient getClient = new GetClient();
		getClient.setModel( new Customer() )
				.setFilter( "customer_id="+transaction.customer_id );
		getClient.execute();
		
		if (getClient.getResponseCode()==200 && getClient.getObject().size()>0) {
			System.out.println("  Customer: "+((Customer)getClient.getObject().get(0)).getId());
			return (Customer)getClient.getObject().get(0);
		}
		
		return new Customer().setId(0);
	}
	
	
	private List<TransactionGood> getTransactionGoods( Transaction transaction ){
		GetClient getClient = new GetClient();
		getClient.setModel( new TransactionGood() )
				.setFilter( "transaction_id="+transaction.transaction_id );
		getClient.execute();
		
		if (getClient.getResponseCode()==200 && getClient.getObject().size()>0) {
			System.out.println("  Products: "+(getClient.getObject().size()));
			return (List) getClient.getObject();
		}
		
		//return null;
		return new ArrayList<TransactionGood>();
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
