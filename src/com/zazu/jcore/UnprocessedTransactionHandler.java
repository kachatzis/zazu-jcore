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
 * Lists and checks unprocessed transactions.
 */
public class UnprocessedTransactionHandler {
	
	private List<Transaction> transactions;
	
	
	public UnprocessedTransactionHandler() {
		System.out.println("  Starting " + getCurrentTime() );
		initTransactions();
		handleTransactions();
	}
	
	public UnprocessedTransactionHandler(String input) {
		System.out.println("  Starting " + getCurrentTime() );
		initTransaction( input );
		handleTransactions();
	}
	
	
	/**
	 * Initialize not processed Transactions List
	 */
	private void initTransactions() {
		this.transactions = new ArrayList<Transaction>();
		
		GetClient getClient = new GetClient();
		getClient.setModel( new Transaction() );
		getClient.setFilter( "(is_processed=0)and(is_canceled=0)&limit=10&sort=request_date" );
		getClient.execute();
		
		if ( getClient.getResponseCode()==200 && !(getClient.getObject()).isEmpty()) {
			this.transactions = (List) getClient.getObject();
		}
		
		System.out.println("UnprocessedTransactionHandler: Initialized "+this.transactions.size()+" Transactions.");
	}
	
	
	/**
	 * Initialize requested Transaction
	 */
	private void initTransaction( String input ) {
		this.transactions = new ArrayList<Transaction>();
		
		GetClient getClient = new GetClient();
		getClient.setModel( new Transaction() );
		//getClient.setFilter( "(is_processed=0)and(is_canceled=0)&limit=10&sort=request_date" );
		getClient.setFilter( "(transaction_id="+input+")" );
		getClient.execute();
		
		if ( getClient.getResponseCode()==200 && !(getClient.getObject()).isEmpty()) {
			this.transactions = (List) getClient.getObject();
		}
		
		System.out.println("UnprocessedTransactionHandler: Initialized "+this.transactions.size()+" Transactions.");
	}
	
	
	/**
	 * Handles Transaction List
	 */
	private void handleTransactions() {
		for (Transaction transaction : this.transactions) {
			if (transaction.transaction_id>0) {
				System.out.println("UnprocessedTransactionHandler: Processing Transaction "+transaction.transaction_id+".");
				handleTransaction( transaction );
			}
		}
	}
	
	
	/**
	 * Single Transaction handling.
	 * @param transaction Transaction that will be handled.
	 */
	private void handleTransaction( Transaction transaction ) {
		List<TransactionGood> transactionGoods = new ArrayList<TransactionGood>();
		Customer customer = new Customer();
		
		// Find Transaction Goods
		GetClient getClient = new GetClient();
		getClient.setModel( new TransactionGood() );
		getClient.setFilter( "transaction_id="+transaction.transaction_id+"&limit=120" );
		getClient.execute();
		
		// Find Customer
		GetClient getClientCustomer = new GetClient();
		getClientCustomer.setModel( new Customer() );
		getClientCustomer.setFilter( "customer_id="+transaction.customer_id+"&limit=1" );
		getClientCustomer.execute();
		// If customer is found
		if (getClientCustomer.getResponseCode()==200 && !getClientCustomer.getObject().isEmpty()) {
			customer = (Customer) getClientCustomer.getObject().get(0);
			System.out.println(">Load Customer: Successful.");
			
			// If products are found
			if ( getClient.getResponseCode()==200 /*&& !getClient.getObject().isEmpty()*/ ) {
				// Load transaction Goods
				transactionGoods = (List) getClient.getObject();
				System.out.println(">Load Transaction Products: Successful.");
				
				// Process as successful only if the checks pass.
				if ( checkTransaction(transaction, transactionGoods, customer) ) {
					processTransaction(transaction, transactionGoods, customer);
				} else {
					cancelTransaction(transaction, transactionGoods, customer);
				}
				
				// Update transaction & products bought 
				// Either processed or canceled
				updateTransactionAndGoods(transaction, transactionGoods);
				// Update Customer (last visit and credits)
				updateCustomer( customer );
			}
			
		}
		
		
	}
	
	/**
	 * Check Transaction Validity.
	 * @param transaction
	 * @param transactionGoods
	 */
	private boolean checkTransaction(Transaction transaction, List<TransactionGood> transactionGoods, Customer customer) {
		boolean response = true;
		
		response = checkTransactionMoney(transaction, transactionGoods) 
				&& checkTransactionCredits(transaction, transactionGoods, customer) 
				&& checkTransactionCustomerValidity(transaction, customer);
		
		return response;
	}
	
	
	private boolean checkTransactionMoney(Transaction transaction, List<TransactionGood> transactionGoods) {
		float moneySpent = 0;
		
		for (TransactionGood transactionGood : transactionGoods) {
			moneySpent += transactionGood.cost;
		}
		
		if (moneySpent == transaction.money_spent) {
			System.out.println(">Transaction Money Check: Successful.");
			return true;
		} else {
			System.out.println(">Transaction Money Check: Failed.");
			return false;
		}
	}
	
	/**
	 * Checks if credits are enough to get the gifts.
	 * (Calculates Gift Credits only, & therefore the transaction
	 * contains the required gift credits, always negative number).
	 * @param transaction
	 * @param transactionGoods
	 * @param customer
	 * @return
	 */
	private boolean checkTransactionCredits(Transaction transaction, List<TransactionGood> transactionGoods, Customer customer) {
		float credits = 0;
		
		if (!transaction.is_topup) {
		
		for ( TransactionGood transactionGood : transactionGoods ) {
			GetClient getClient = new GetClient();
			getClient.setModel( new Good() );
			getClient.setFilter("good_id="+transactionGood.good_id+"&limit=1");
			getClient.execute();
			
			if (getClient.getResponseCode()==200 && !(getClient.getObject().isEmpty()) && ((Good) getClient.getObject().get(0)).is_gift) {
				credits -= ((Good) getClient.getObject().get(0)).credits_cost;
			}
			
			//credits += creditOffer.credits_per_unit*transactionGood.cost;
			
		}
		
		transaction.credit = (int)credits;
		if ( (-1*(transaction.credit) <= customer.credit) ) {
			System.out.println(">Transaction Credits Check: Successful.");
			return true;
		}
		
		System.out.println(">Transaction Credits Check: Failed (Calculated:"+(int)credits+",Found:"+customer.credit+")");
		return false;
		}
		
		return true;
	}
	
	
	/**
	 * Calculates the credits given by the products bought (not gifts).
	 * The Transaction it receives, contains already the required gift credits,
	 * so they are not counted again.
	 * @param transaction
	 * @param transactionGoods
	 */
	private void setTransactionCredits(Transaction transaction, List<TransactionGood> transactionGoods) {
		float credits = transaction.credit; // Credits already calculated for gifts (lower or equal to 0)
		int positiveCredits = 0;
		
		if (!transaction.is_topup) {
			for ( TransactionGood transactionGood : transactionGoods ) {
				GetClient getClientGood = new GetClient();
				getClientGood.setModel( new Good() );
				getClientGood.setFilter("good_id="+transactionGood.good_id+"&limit=1");
				getClientGood.execute();
				
				// If the product is not a gift (which they have already been counted)
				if (getClientGood.getResponseCode()==200 && !getClientGood.getObject().isEmpty() && !((Good) getClientGood.getObject().get(0)).is_gift ) {
					
					// Get Credit Offer
					CreditOffer creditOffer = new CreditOffer();
					GetClient getClient  = new GetClient();
					getClient.setModel( new CreditOffer() );
					getClient.setFilter("good_id="+transactionGood.good_id+"&limit=1");
					getClient.execute();
					
					/*GetClient getClient = new GetClient();
					getClient.setModel( new CreditOffer() );
					getClient.setFilter("good_id="+transactionGood.good_id+"&limit=1");
					getClient.execute();
					*/
					
					if (getClient.getResponseCode()==200 && !(getClient.getObject().isEmpty())) {
						creditOffer = (CreditOffer) (getClient.getObject()).get(0);
						System.out.println(" !>> Credit Offer Found in setting Credits: " + creditOffer.credit_offer_id);
					} else {
						System.out.println(" !>> Credit Offering not found for Product: " + transactionGood.good_id);
					}
					
					credits += creditOffer.credits_per_unit*transactionGood.cost;
					positiveCredits += creditOffer.credits_per_unit*transactionGood.cost;
					System.out.println(" !>> Credits calculated: " + credits);
					
				}
				
			}
			
			transaction.positive_credit = positiveCredits;
			transaction.credit = (int)credits;
		}else {
			System.out.println(" !>> Topup Credits: "+ transaction.credit);
			transaction.positive_credit = transaction.credit;
		}
		
	}
	
	
	private boolean checkTransactionCustomerValidity(Transaction transaction, Customer customer) {
		
		if (
			customer.is_enabled 
			&& !customer.is_deleted
			&& customer.credit >= -(transaction.credit)) {
				System.out.println(">Customer Validity Check: Successful.");
				return true;
		}
		
		System.out.println(">Customer Validity Check: Failed.");
		return false;
	}
	
	
	/**
	 * Process Transaction and related products.
	 * @param transaction
	 * @param transactionGoods
	 */
	private void processTransaction(Transaction transaction, List<TransactionGood> transactionGoods, Customer customer) {
		// Process Transaction
		transaction.is_processed = true;
		transaction.is_canceled = false;
		transaction.note = "Processed by jCore";
		transaction.previous_card_credit = customer.credit;
		
		transaction.transaction_date = getCurrentDate();
		transaction.transaction_time = getCurrentTime();
		
		setTransactionCredits( transaction, transactionGoods );
				
		for (TransactionGood transactionGood : transactionGoods) {
			// Process Transaction Good
			transactionGood.is_processed = true;
			transactionGood.is_canceled = false;
		}
		
		customer.credit += transaction.credit;
		customer.previous_credit += transaction.positive_credit;
		customer.last_visit_date = transaction.request_date;
		customer.last_visit_time = transaction.request_time;
		
		System.out.println(">Set as Processed: Successful.");
	}
	
	
	/**
	 * Cancel Transaction and related products.
	 * @param transaction
	 * @param transactionGoods
	 */
	private void cancelTransaction(Transaction transaction, List<TransactionGood> transactionGoods, Customer customer) {
		// Cancel Transaction
		transaction.is_processed = true;
		transaction.is_canceled = true;
		transaction.note = "Canceled by jCore.";
		
		for (TransactionGood transactionGood : transactionGoods) {
			// Cancel Transaction Good
			transactionGood.is_processed = true;
			transactionGood.is_canceled = true;
		}
		
		transaction.transaction_date = getCurrentDate();
		transaction.transaction_time = getCurrentTime();
		
		customer.last_visit_date = transaction.request_date;
		customer.last_visit_time = transaction.request_time;
		
		System.out.println(">Set as Processed: Successful.");
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
	
	/**
	 * Update Transaction & Products.
	 * @param transaction
	 * @param transactionGoods
	 */
	private void updateTransactionAndGoods(Transaction transaction, List<TransactionGood> transactionGoods) {
		updateTransaction( transaction );
		updateGoods( transactionGoods );
	}
	
	/**
	 * Update Transaction.
	 * @param transaction
	 */
	private void updateTransaction(Transaction transaction) {
		PatchClient patchClient = new PatchClient();
		patchClient.setObject( transaction );
		patchClient.setId(transaction.transaction_id);
		patchClient.execute();
		
		System.out.println(">Update Transaction: Successful.");
	}
	
	/**
	 * Update Transaction Products.
	 * @param transactionGoods
	 */
	private void updateGoods(List<TransactionGood> transactionGoods) {
		for ( TransactionGood transactionGood : transactionGoods ) {
			updateGood ( transactionGood );
		}
	}
	
	/**
	 * Update Customer (most about credits).
	 * @param transaction
	 */
	private void updateCustomer(Customer customer) {
		PatchClient patchClient = new PatchClient();
		patchClient.setObject( customer );
		patchClient.setId(customer.customer_id);
		patchClient.execute();
		
		System.out.println(">Update Customer: Successful.");
	}
	
	
	/**
	 * Update Single Transaction Product.
	 * @param transactionGood
	 */
	private void updateGood(TransactionGood transactionGood) {
		PatchClient patchClient = new PatchClient();
		patchClient.setObject( transactionGood );
		patchClient.setId(transactionGood.good_bought_id);
		patchClient.execute();
		
		System.out.println(">Update Product: Successful.");
	}
	
}