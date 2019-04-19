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

package com.zazu.jcoreRuntime;

import com.amazonaws.services.lambda.runtime.Context; 
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.zazu.jcore.CancelledTransactionHandler;
import com.zazu.jcore.CustomerLevelHandler;
import com.zazu.jcore.UnprocessedTransactionHandler;
import com.zazu.jcoreRuntime.Main;


public class LambdaFunction {

	public void handler(Object input, Context context) {
    	// Main Instance
    	Main.main( new String[0] );
    }
	
	public void transactionHandler(Object input, Context context) {
		
		
		System.out.println("####################################");
		System.out.println("START - UnprocessedTransactionHandler");
		UnprocessedTransactionHandler uTH = new UnprocessedTransactionHandler();
		System.out.println("END - UnprocessedTransactionHandler");
		System.out.println("####################################");
		
		
		System.out.println("####################################");
		System.out.println("START - CancelledTransactionHandler");
		CancelledTransactionHandler cTH = new CancelledTransactionHandler();
		System.out.println("END - CancelledTransactionHandler");
		System.out.println("####################################");
    }
	

	/* !!!Not tested on Lambda!!! */
	public void singleTransactionHandler(Object input, Context context) {
		
		System.out.println(input.toString());
		
		System.out.println("####################################");
		System.out.println("START - UnprocessedTransactionHandle/Singler");
		UnprocessedTransactionHandler uTH = new UnprocessedTransactionHandler( input.toString() );
		System.out.println("END - UnprocessedTransactionHandler/Single");
		System.out.println("####################################");
		
    }
	
	public void levelHandler(Object input, Context context) {
		System.out.println("####################################");
		System.out.println("START - CustomerLevelHandler");
		CustomerLevelHandler cLH = new CustomerLevelHandler();
		System.out.println("END - CustomerLevelHandler");
		System.out.println("####################################");
    }
	
	
	

}
