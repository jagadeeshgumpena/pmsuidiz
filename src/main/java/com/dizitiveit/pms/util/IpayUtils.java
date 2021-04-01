package com.dizitiveit.pms.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IpayUtils {
	
public static String getTransactionId() {

		
		return "TRANS" + getRandomstring();
			
		
	}

//generate random String for unique Ids
	public static String getRandomstring() {
int min = 1;
int max = 100;
// Generate random double value from 50 to 100
// System.out.println("Random value in double from "+min+" to "+max+ ":");
double random_double = Math.random() * (max - min + 1) + min;

String doubleAsString = String.valueOf(random_double);
int indexOfDecimal = doubleAsString.indexOf(".");
// System.out.println("Double Number: " + random_double);
// System.out.println("Integer Part: " + doubleAsString.substring(0,
// indexOfDecimal));
// System.out.println("Decimal Part: " +
// doubleAsString.substring(indexOfDecimal+1));
// System.out.println(random_double);

String pattern = "yyyyMMddhh24mmssSSS";
SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
String date = simpleDateFormat.format(new Date());

// generate random string
return  date + doubleAsString.substring(indexOfDecimal + 1);
}

}
