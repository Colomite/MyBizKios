package com.kreators.mybizkios;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.File;

public class UnitConstant {


    public static final int db_version = 1;


    public static final Integer timeOutTime     = 60 * 1000;


    public static final String db_name = "mybizkiosdb";

    public static Integer idUser = 0;
    public static Integer idSession = 0;

    // public static final String M_VERSI = "DEMO";
    public static final String M_VERSI = "FULL";

    public static final Integer M_MAX =  500;

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



    public static final int  M_NEW = 0;
    public static final int  M_EDIT = 1;

    public static final int  M_OPEN = 1;

    public static final int TT_MASTER_LOCATION    = 1;
    public static final int TT_MASTER_WAREHOUSE   = 2;
    public static final int TT_MASTER_DEPT        = 3;
    public static final int TT_MASTER_ITEM        = 4;
    public static final int TT_MASTER_CUST_TYPE   = 5;
    public static final int TT_MASTER_PARTNER     = 6;
    public static final int TT_MASTER_EMPLOYEE    = 7;
    public static final int TT_MASTER_CURRENCY    = 8;
    public static final int TT_MASTER_ACCOUNTS    = 9;
    public static final int TT_MASTER_ITEM_GROUP  = 12;
    public static final int TT_MASTER_COA         = 11;
    public static final int TT_MASTER_SESSION     = 16;
    public static final int TT_MASTER_ITEM_CATEGORY   = 18;
    public static final int TT_MASTER_ITEM_SALES = 19;

    public static final int TT_MASTER_ITEM_UNIT   = 300;
    public static final int TT_MASTER_ITEM_PRICE   = 301;
    public static final int TT_MASTER_ITEM_MATERIAL   = 302;

    //INVENTORY
    public static final int TT_ITEM_ADJ               = 21;
    public static final int TT_ITEM_TRANSF_IN         = 22;
    public static final int TT_ITEM_TRANSF_OUT        = 23;
    public static final int TT_ITEM_ASSEMBLY          = 24;
    public static final int TT_ITEM_DISSASSEMBLY      = 25;
    public static final int TT_ITEM_ADJ_OUT           = 26;

    //LAPORAN
    public static final int TT_REP_ITEM_ADJ           = 921;
    public static final int TT_REP_ITEM_TRANSF_IN    	= 923;
    public static final int TT_REP_ITEM_TRANSF_OUT    = 924;
    public static final int TT_REP_ITEM_ASSEMBLY      = 925;
    public static final int TT_REP_ITEM_DISASSEMBLY  	= 926;
    public static final int TT_MIN_STOCK        			= 927;
    public static final int TT_MIN_STOCK_WAR    			= 928;
    public static final int TT_STOCK_SUMMARY_SINGLE 	= 929;
    public static final int TT_STOCK_SUMMARY_WAR			= 930;
    public static final int TT_STOCK_MOVEMENT   			= 931;
    public static final int TT_STOCK_MOVEMENT_DETAIL 	= 932; //BEDA NAMA DI MB3 public static final int TT_STOCK_MOVEMENT_2 MAYBE
    public static final int TT_REP_STOCK_PRICE			  = 933; //TIDAK ADA DI MB3
    public static final int TT_STOCK_VALUE_SUMMARY   	= 934;
    public static final int TT_STOCK_VALUE_DETAIL    	= 935;
    public static final int TT_STOCK_VALUE_PER_WAREHOUSE 	= 936;
    public static final int TT_REP_SN           			= 937;
    public static final int TT_REP_IN_SN        			= 938;
    public static final int TT_REP_OUT_SN       			= 939;
    public static final int TT_REP_HISTORY_SN   			= 948;

    //LAPORAN CETAK
    public static final int TT_FR_ITEM_ADJ_REPORT   		    = 201; //TIDAK ADA DI MB3
    public static final int TT_FR_ITEM_TRANSF_REPORT 		   	= 202; //TIDAK ADA DI MB3
    public static final int TT_FR_ITEM_ASSEMBLY_REPORT   		= 203; //TIDAK ADA DI MB3
    public static final int TT_FR_ITEM_DISASSEMBLY_REPORT  	= 204; //TIDAK ADA DI MB3
    public static final int TT_FR_STOCK_REPORT 				      = 205; //TIDAK ADA DI MB3
    public static final int TT_FR_IN_SN_REPORT       			  = 206; //TIDAK ADA DI MB3
    public static final int TT_FR_OUT_SN_REPORT      			  = 207; //TIDAK ADA DI MB3
    public static final int TT_FR_STOCK_SN_REPORT 			    = 250; //TIDAK ADA DI MB3

    //PURCHASE
    public static final int TT_PURCHASE_ORDER   			= 41;
    public static final int TT_PURCHASE_INVOICE 			= 42;
    public static final int TT_PURCHASE_INVOICE_CONS 	= 47;
    public static final int TT_PURCHASE_RETURN  			= 43;
    public static final int TT_CONS_IN          			= 44;
    public static final int TT_CONS_IN_RETURN   			= 45;
    public static final int TT_PO_CANCEL        			= 46;

    //LAPORAN
    public static final int TT_REP_PURCHASE_ORDER			      = 940; //TIDAK ADA DI MB3
    public static final int TT_REP_PURCHASE        		      = 941;
    public static final int TT_REP_PURCHASE_RETURN 		      = 942;
    public static final int TT_REP_CONS_IN         		      = 943;
    public static final int TT_REP_CONS_IN_RETURN  		      = 944;
    public static final int TT_REP_PURCHASE_CONS_IN		      = 945; //TIDAK ADA DI MB3
    public static final int TT_REP_CONS_IN_SUMMARY		      = 946; //TIDAK ADA DI MB3
    public static final int TT_REP_CONS_IN_DETAIL			      = 947; //TIDAK ADA DI
    public static final int TT_REP_CONS_IN_PROCESSED        = 949; // 948 DIPAKAI public static final int TT_REP_HISTORY_SN
    public static final int TT_REP_CONS_IN_PROCESSED_DETAIL = 950;
    public static final int TT_REP_CONS_IN_LEDGER           = 951;
    public static final int TT_REP_CONS_IN_PARTNER          = 952;
    public static final int TT_REP_CONS_IN_PARTNER_DETAIL   = 953;
    public static final int TT_REP_CONS_IN_AGING            = 958;

    //LAPORAN CETAK
    public static final int TT_FR_PURCHASE_ORDER_REPORT	  	= 208; //TIDAK ADA DI MB3
    public static final int TT_FR_PURCHASE_REPORT 	  		  = 209; // NILAI LAMA 2003
    public static final int TT_FR_PURCHASE_RETURN_REPORT 	  = 210; // NILAI LAMA 2004
    public static final int TT_FR_CONS_IN_REPORT 				    = 211; // NILAI LAMA 2008
    public static final int TT_FR_PURCHASE_CONS_IN_REPORT   = 212; // NILAI LAMA 2009
    public static final int TT_FR_CONS_IN_RETURN_REPORT  	  = 213;  // NILAI LAMA 2010
    public static final int TT_FR_PURCHASE_ANALYSIS_REPORT	= 214; //TIDAK ADA DI MB3

    //SALES
    public static final int TT_DISCOUNT_MANAGER      	  = 17;
    public static final int TT_SALES_ORDER     	   		  = 31;
    public static final int TT_SALES_INVOICE   			    = 32;
    public static final int TT_SALES_INVOICE_CONS 		  = 39;
    public static final int TT_SALES_POS 					      = 38; //TIDAK ADA DI MB3
    public static final int TT_SALES_RETURN    			    = 33;
    public static final int TT_CONS_OUT        			    = 34;
    public static final int TT_CONS_OUT_RETURN 			    = 35;
    public static final int TT_PRICE_UPDATE    			    = 36;
    public static final int TT_SO_CANCEL       			    = 37;

    //LAPORAN
    public static final int TT_REP_SALES_ORDER			      = 960; //TIDAK ADA DI MB3
    public static final int TT_REP_SALES_INVOICE     		  = 961;
    public static final int TT_REP_SALES_RETURN    		    = 962;

    public static final int TT_REP_SALES_CASHIER_SESSION	= 965; //TIDAK ADA DI MB3 Sales Report per Cashier per Sesi
    public static final int TT_REP_COMMISION_SALES    	  = 972;
    public static final int TT_REP_CONS_OUT        		    = 963;
    public static final int TT_REP_CONS_OUT_RETURN 		    = 964;
    public static final int TT_REP_SALES_CONS_OUT			    = 966; //TIDAK ADA DI MB3
    public static final int TT_REP_CONS_OUT_SUMMARY		    = 967; //TIDAK ADA DI MB3
    public static final int TT_REP_CONS_OUT_DETAIL		    = 968; //TIDAK ADA DI MB3
    public static final int TT_REP_PRICE_UPDATE			      = 969; //TIDAK ADA DI MB3
    public static final int TT_REP_PRICE_UPDATE_DETAIL	  = 970; //TIDAK ADA DI MB3
    public static final int TT_REP_PRICELIST				      = 971; //TIDAK ADA DI MB3
    public static final int TT_REP_CONS_OUT_PROCESSED        = 973;
    public static final int TT_REP_CONS_OUT_PROCESSED_DETAIL = 974;
    public static final int TT_REP_CONS_OUT_LEDGER           = 975;
    public static final int TT_REP_CONS_OUT_PARTNER          = 976;
    public static final int TT_REP_CONS_OUT_PARTNER_DETAIL   = 977;
    public static final int TT_REP_CONS_OUT_AGING         = 978;

    //LAPORAN CETAK
    public static final int TT_FR_SALES_ORDER_REPORT	 	    = 215; //TIDAK ADA DI MB3
    public static final int TT_FR_SALES_REPORT 			  	    = 216; // NILAI LAMA 2001
    public static final int TT_FR_SALES_RETURN_REPORT 	    = 217; // NILAI LAMA 2002
    public static final int TT_FR_CONS_OUT_REPORT 			    = 218; // NILAI LAMA 2005
    public static final int TT_FR_SALES_CONS_OUT_REPORT 	  = 219; // NILAI LAMA 2006
    public static final int TT_FR_CONS_OUT_RETURN_REPORT	  = 220; // NILAI LAMA 2007
    public static final int TT_FR_COMMISION_SALES_REPORT		= 221; //TIDAK ADA DI MB3
    public static final int TT_FR_SALES_PERFORMANCE_REPORT 	= 222; //NILAI LAMA 2011
    public static final int TT_FR_SALES_ANALYSIS_REPORT	  	= 223; //TIDAK ADA DI MB3
    public static final int TT_FR_SALES_PERITEM_CATEGORY_ANAlYSIS_REPORT = 224; //TIDAK ADA DI MB3
    public static final int TT_FR_BEST_CUSTOMER_REPORT	 	  = 270; //TIDAK ADA DI MB3
    public static final int TT_FR_BEST_ITEM_REPORT	 	      = 271; //TIDAK ADA DI MB3
    public static final int TT_FR_BEST_SALES_PERSON_REPORT  = 272; //TIDAK ADA DI MB3

    //AP
    public static final int TT_AP_NOTE    			    = 71;
    public static final int TT_AP_MEMO    	    		= 72;
    public static final int TT_AP_PAYMENT   		  	= 73;

    //LAPORAN
    public static final int TT_REP_AP_PAY		      	= 704;
    public static final int TT_REP_AP_MEMO	    		= 701; //TIDAK ADA DI MB3
    public static final int TT_REP_AP_NOTE		    	= 702; //TIDAK ADA DI MB3
    public static final int TT_REP_APAGING_DETAIL  	= 706;
    public static final int TT_REP_AP_PAY_HISTORY  	= 705;
    public static final int TT_REP_AP_PAY_HISTORY_DETAIL = 703;
    public static final int TT_AP_SUMMARY          	= 707;
    public static final int TT_REP_APMOVEMENT      	= 708;
    public static final int TT_REP_APLEDGER        	= 709;
    public static final int TT_REP_CHEQUEAPAGE      = 710;
    public static final int TT_REP_POAGING          = 711;

    //LAPORAN CETAK
    public static final int TT_FR_AP_PAY_REPORT		    	= 224; //TIDAK ADA DI MB3
    public static final int TT_FR_AP_MEMO_REPORT			  = 225; //TIDAK ADA DI MB3
    public static final int TT_FR_AP_NOTE_REPORT		  	= 227; //TIDAK ADA DI MB3
    public static final int TT_FR_APAGING_REPORT  	  	= 228; //TIDAK ADA DI MB3
    public static final int TT_FR_APLEDGER_REPORT		  	= 229; //TIDAK ADA DI MB3
    public static final int TT_FR_AP_PAY_HISTORY_REPORT	= 230; //TIDAK ADA DI MB3

    //service
    public static final int TT_SERVICE_IN       = 51;
    public static final int TT_SERVICE_PROCESS  = 52;
    public static final int TT_SERVICE_DATA     = 53;
    public static final int TT_SERVICE_OUT      = 54;
    public static final int TT_TAG              = 55;

    //AR
    public static final int TT_AR_NOTE    = 61;
    public static final int TT_AR_MEMO    = 62;
    public static final int TT_AR_PAYMENT = 63;

    //LAPORAN
    public static final int TT_REP_AR_PAY		      	  	  = 604;
    public static final int TT_REP_AR_MEMO				        = 601; //TIDAK ADA DI MB3
    public static final int TT_REP_AR_NOTE				        = 602; //TIDAK ADA DI MB3
    public static final int TT_REP_ARAGING_DETAIL  	  	  = 606;
    public static final int TT_REP_AR_PAY_HISTORY  	  	  = 605;
    public static final int TT_REP_AR_PAY_HISTORY_DETAIL  = 603; //TIDAK ADA DI MB3
    public static final int TT_AR_SUMMARY          		    = 607;
    public static final int TT_REP_ARMOVEMENT      		    = 608;
    public static final int TT_REP_ARLEDGER        		    = 609;
    public static final int TT_REP_CHEQUEARAGE            = 610;
    public static final int TT_REP_SOAGING                = 611;

    //LAPORAN CETAK
    public static final int TT_FR_AR_PAY_REPORT		    	  = 231; //TIDAK ADA DI MB3
    public static final int TT_FR_AR_MEMO_REPORT		  	  = 232; //TIDAK ADA DI MB3
    public static final int TT_FR_AR_NOTE_REPORT			    = 233; //TIDAK ADA DI MB3
    public static final int TT_FR_ARAGING_REPORT  	  	  = 234; //TIDAK ADA DI MB3
    public static final int TT_FR_ARLEDGER_REPORT			    = 235; //TIDAK ADA DI MB3
    public static final int TT_FR_AR_PAY_HISTORY_REPORT	  = 237; //TIDAK ADA DI MB3

    //CASH
    public static final int TT_WITHDRAWAL 			    = 81;
    public static final int TT_DEPOSIT    		    	= 82;
    public static final int TT_TRANSF_OUT 		    	= 83;
    public static final int TT_TRANSF_IN  		    	= 84;
    public static final int TT_IN_CHQ_CLEARING 	   	= 91;
    public static final int TT_IN_CHQ_CANCEL    		= 93;
    public static final int TT_OUT_CHQ_CLEARING 		= 94;
    public static final int TT_OUT_CHQ_CANCEL   		= 96;

    //LAPORAN
    public static final int TT_REP_WITHDRAWAL 		    = 981;
    public static final int TT_REP_DEPOSIT    		    = 982;
    public static final int TT_REP_TRANSF			    	  = 983;
    public static final int TT_REP_IN_CHQ  		    	  = 991;
    public static final int TT_REP_IN_CHQ_CANCEL		  = 992; //TIDAK ADA DI MB3
    public static final int TT_REP_OUT_CHQ 		    	  = 994;
    public static final int TT_REP_OUT_CHQ_CANCEL		  = 993; //TIDAK ADA DI MB3
    public static final int TT_ACC_SUMMARY 		    	  = 904;
    public static final int TT_REP_ACCMOVEMENT 		    = 909;
    public static final int TT_REP_ACCMOVEMENT_DETAIL = 910; //TIDAK ADA DI MB3
    public static final int TT_REP_ACCLEDGER 		    	= 906;  //TIDAK ADA DI MB3

    //LAPORAN CETAK
    public static final int TT_FR_WITHDRAWAL_REPORT	  = 238; //TIDAK ADA DI MB3
    public static final int TT_FR_DEPOSIT_REPORT		  = 239; //TIDAK ADA DI MB3
    public static final int TT_FR_TRANSFER_OUT_REPORT	= 240; //TIDAK ADA DI MB3
    public static final int TT_FR_TRANSFER_IN_REPORT	= 241; //TIDAK ADA DI MB3
    public static final int TT_FR_TYPE_TRANS_REPORT	  = 242; //TIDAK ADA DI MB3
    public static final int TT_FR_GIRO_REPORT			    = 243; //TIDAK ADA DI MB3
    public static final int TT_FR_LEDGER_REPORT		    = 244; //TIDAK ADA DI MB3

    //ACCOUNTING
    public static final int TT_GENERAL_JOURNAL  		    = 101;
    public static final int TT_TRIAL_BALANCE            = 111;
    public static final int TT_BALANCE_SHEET    		    = 112;
    public static final int TT_INCOME_STATEMENT  		    = 113;
    public static final int TT_FOREX_GAIN_LOSS  		    = 114;
    public static final int TT_JOURNAL_TRACKING 		    = 116;
    public static final int TT_GL_MAPPING    			      = 913;
    public static final int TT_GENERAL_LEDGER_SUMMARY   = 117;
    public static final int TT_GENERAL_LEDGER_DETAIL    = 118;
    public static final int TT_GL_MAPPINGREPORT 		    = 914;
    public static final int TT_INV_PROFIT_LOSS_SUMMARY  = 119;
    public static final int TT_INV_PROFIT_LOSS_DETAIL   = 120;
    public static final int TT_POSTING_NEW              = 123;
    public static final int TT_CLOSING_NEW              = 124;
    public static final int TT_PROFIT_LOSS_SUMMARY      = 126;
    public static final int TT_PROFIT_LOSS_DETAIL       = 127;
    public static final int TT_PROFIT_LOSS_SALES		    = 130;

    //UTILITY
    public static final int TT_BACKUP				      = 251; //TIDAK ADA DI MB3
    public static final int TT_BACKUP_CLOSE				= 259; //TIDAK ADA DI MB3
    public static final int TT_RESTORE			      = 252; //TIDAK ADA DI MB3
    public static final int TT_MASTER_USER        = 10;
    public static final int TT_MASTER_ACC_USE     = 270;
    public static final int TT_MASTER_LOC_USE     = 271;
    public static final int TT_MASTER_USER_MENU   = 272;
    public static final int TT_MASTER_USER_AUTH   = 273;
    public static final int TT_CHANGE_PASSWORD 	  = 253; //NILAI LAMA 1001
    public static final int TT_PURGEDATA_DESIGN	  = 254; //TIDAK ADA DI MB3
    public static final int TT_VOID    			      = 902;
    public static final int TT_EFAKTUR 			      = 255; //NILAI LAMA 1000
    public static final int TT_OPTION_GLOBAL		  = 256; //TIDAK ADA DI MB3
    public static final int TT_OPTION_LOCAL		    = 257; //TIDAK ADA DI MB3
    public static final int TT_ABOUT				      = 258; //TIDAK ADA DI MB3

    //NOTE
    public static final int TT_NOTE_PURCHASE_RETURN = 260;
    public static final int TT_NOTE_CONS_IN_RETURN  = 261;
    public static final int TT_NOTE_SALES_INVOICE   = 262;
    public static final int TT_NOTE_CONS_OUT        = 263;


    //JENIS PENGECEKAN
    public static final int CHECK_CODE_DOUBLE = 1;
    public static final int CHECK_EMPTY_FIELD = 2;

    public static final String filename = "posandroidkreators.dat";

    public static boolean sucReg = Boolean.FALSE;


    public static final int ITE_TYPE_NONSTOCK = 0;
    public static final int ITE_TYPE_STOCK    = 1;
    //public static final int ITE_TYPE_ASSEMBLY    = 2;
    public static final int ITE_TYPE_BUNDLE    = 2;
    //public static final int ITE_TYPE_MENU    = 4;

    //public static final String ITE_TYPE_NAME[] = {"Non Stock","Stock","Assembly","Bundle","Menu"};
    public static final String ITE_TYPE_NAME[] = {"Non Stock","Stock","Bundle"};

    public static final int ITE_STATUS_ACTIVE = 0;
    public static final int ITE_STATUS_NONACTIVE    = 1;
    public static final String ITE_STATUS_NAME[] = {"Active","Non Active"};


    public static final int PARTNER_TYPE_CUSTOMER = 0;
    public static final int PARTNER_TYPE_SUPPLIER    = 1;
    public static final String PARTNER_TYPE_NAME[] = {"Customer","Supplier"};

    public static final int ACCOUNT_TYPE_CASH = 0;
    public static final int ACCOUNT_TYPE_BANK = 1;
    public static final String ACCOUNT_TYPE_NAME[] = {"Cash","bank"};

    public static final int PAYMENT_TYPE_CASH = 0;
    public static final int ACCOUNT_TYPE_DEBIT_CARD = 1;
    public static final int ACCOUNT_TYPE_CREDIT_CARD = 2;
    public static final String PAYMENT_TYPE_NAME[] = {"Cash","Debit Card","Credit Card"};

    public static final int BARCODE_TYPE_1D     = 0;
    public static final int BARCODE_TYPE_QRCODE = 1;

    public static final int PAYMENT_TYPE_PURCHASE = 0;
    public static final int PAYMENT_TYPE_PAYMENT = 1;


    public static final int TYPE_BACKUP = 0;
    public static final int TYPE_RESTORE = 1;

    public static final int DISC_NO = 0;
    public static final int DISC_YES = 1;

    public static final int TT_UNSUSPEND = 50;


    public static final int TEST = 1;



}
