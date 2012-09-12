/*
    This file is part of AFL-OSE (AFLogical - Open Source Edition).

    AFL-OSE is a framework for the forensic logical extraction of data 
    from Android devices.

    Copyright © 2011 viaForensics LLC
 
    AFL-OSE is available under the terms of multiple licenses.
 
    For academic, research, and experimental purposes, AFL-OSE is 
    free software: you can redistribute it and / or modify it under 
    the terms of the GNU General Public License as published by the 
    Free Software Foundation, version 3.  The source code must be made 
    available and this license must be retained.  It is distributed in 
    the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
    even the implied warranty of FITNESS FOR A PARTICULAR PURPOSE.  
    See the GNU General Public License at http://www.gnu.org/licenses/ 
    for more details.
 
    For any other purposes, this file may not be used except under the 
    terms of a commercial license granted from viaForensics.  For 
    commercial license details, contact viaForensics at 
    http://viaforensics.com/contact-us/.
 */

package com.viaforensics.android.providers;

public class CSVCharEscapeWrapper {

	public static final String SPACE = " ";
	public static final String COMMA = ",";
	public static final String DOUBLE_QUOTE = "\"";
	public static final String LINE_FEED = "\n";
	public static final String CR = "\r";
		
	public static String safeEscape(String stringToFormat) {
		
		if ( stringToFormat == null) return "";
		
		boolean hasSpaces = stringToFormat.contains(SPACE);
		boolean hasCommas = stringToFormat.contains(COMMA);
		boolean hasDoubleQuotes = stringToFormat.contains(DOUBLE_QUOTE);
		boolean hasLineBreaks = stringToFormat.contains(LINE_FEED) || stringToFormat.contains(CR);
		boolean needsEscaped = (hasSpaces || hasCommas || hasDoubleQuotes || hasLineBreaks);
		
		String updatedValue = stripLeadingAndTrailingSpaces(stringToFormat);
		
		if ( hasDoubleQuotes ) {
			updatedValue = updatedValue.replace("\"", "\"\"");
		}
		
		return (needsEscaped) ? surroundWithDoubleQuotes(updatedValue) : updatedValue;
		
	}
	

	private static String stripLeadingAndTrailingSpaces(String stringToFormat) {
		return stringToFormat.trim();
	}

	private static String surroundWithDoubleQuotes(String stringToFormat) {
		return "\"" + stringToFormat + "\"";
	}

}

