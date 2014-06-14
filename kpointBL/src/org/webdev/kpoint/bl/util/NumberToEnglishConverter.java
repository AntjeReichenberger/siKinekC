/**
 * Number to English converter:
 * 
 * The purpose of this class to convert number value to English word value such as 13 to Thirteen.
 * 
 * Right now this class can able to convert 1 to 100 number value to English word value.
 * 
 * This class can be handled 1000 number also.....but need to add extra method.....
 * 
 * */


package org.webdev.kpoint.bl.util;

public class NumberToEnglishConverter {
		
	private static String [] englishNumber_19 = {"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
	private static String [] englishTen = {"Zero", "Ten", "Twenty", "Thirty", "Fourty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"}; 
	
	public static String getEnlishNumberUpTo19(int index) {
		if (index == 0)
			return "";
		
		return englishNumber_19[index];
	}
	
	public static String getEnglishTen(int index) {
		return englishTen[index];
	}
	
	public static String convertNumberToEnglish(String number) {
		String englishNumber = "";
		
		int numberInt = Integer.parseInt(number);
		StringBuilder numberBuilder = new StringBuilder(number);
		char[] numberChar = numberBuilder.toString().toCharArray();
					
		if (numberInt < 20 && numberInt > 0){
			englishNumber = getEnlishNumberUpTo19(numberInt);
		} else if (numberInt < 100 && numberInt > 19) {
			englishNumber = getConvertNumberToEnglishTen(number, numberChar);
		} else if (numberInt == 100) {
			englishNumber = "Hundred";
		} else {
			englishNumber = number;
		}
		
		return englishNumber;
	}
	
	private static String getConvertNumberToEnglishTen(String numberTenStr, char[] numberTenChar) {
		String englishTenNumber = "";
		
		char firstDigit = numberTenChar[0];
		char secondDigit = numberTenChar[1];
		
		int firstDigitInt = Integer.parseInt(String.valueOf(firstDigit));
		int secondDigitInt = Integer.parseInt(String.valueOf(secondDigit));
				
		englishTenNumber = getEnglishTen(firstDigitInt) + " " + getEnlishNumberUpTo19(secondDigitInt);
		
		return englishTenNumber;
	}
}
