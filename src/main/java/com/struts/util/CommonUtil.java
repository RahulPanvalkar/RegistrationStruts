package com.struts.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;

public class CommonUtil {

	// Method to convert java object to JSON String
	/*public static String convertToJson(Map<String, Object> resp) {
		JsonObject jsonObject = new Gson().toJsonTree(resp).getAsJsonObject();

		JsonObject result = new JsonObject();
		result.add("response", jsonObject);

		return result.toString();
	}*/

	// Method to convert date in String to java.sql.Date format
	public static Date convertStringToSqlDate(String dateString) throws ParseException {
		String format = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		java.util.Date date = sdf.parse(dateString);
		return new Date(date.getTime());
	}
	
	// Method to parse date
	public static LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid Date Format: " + dateStr);
            throw e; // Or return null based on your requirement
        }
    }

	// Method to convert LocalDate to String
	public static String convertToString(LocalDate date) {
		if (date == null) {
			return "";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return date.format(formatter);
	}

}
