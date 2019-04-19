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

import com.zazu.jcore.*;

public class Main {
	
	public static void main(String[] args) {
		
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
		
		
		System.out.println("####################################");
		System.out.println("START - CustomerLevelHandler");
		CustomerLevelHandler cLH = new CustomerLevelHandler();
		System.out.println("END - CustomerLevelHandler");
		System.out.println("####################################");
		
	}

}
