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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;

import com.viaforensics.android.ForensicsException;
import com.viaforensics.android.logs.DebugLogger;

public class CSVForensicsProvider extends ForensicsProvider {

	private static final String TAG = "AndroidForensics";

	public CSVForensicsProvider(String displayName, Uri uri) {
		super(displayName, uri);
	}

	public void process(Context context, File forensicsDir) throws ForensicsException {
		ContentResolver resolver = context.getContentResolver();
		
		DebugLogger.d(TAG, "processing " + displayName );
		try {
			Cursor idsOnlyCursor = null;
			try {
				idsOnlyCursor = resolver.query( uri, new String[]{"_id"}, null, null, "_id ASC");
				if ( idsOnlyCursor != null ) {
					
					//consider opening csv here for all writing (normal and chunks)
					BufferedWriter writer = new BufferedWriter(
							new FileWriter(new File(forensicsDir, displayName + ".csv")), 8096);
					
					try {
						final int numRecords = idsOnlyCursor.getCount();
						{
							queryContent(resolver, writer, true, null );
						}
					}finally {
						if (writer != null) {
							try {
								writer.close();
							} catch (IOException ex) { Log.e(this.getClass().getName(), "Error message: ", ex ); }
						}
					}
				}else {
					idsOnlyCursor = resolver.query( uri, null, null, null, null);
					if (idsOnlyCursor != null ) {
						String[] cols = idsOnlyCursor.getColumnNames();
						throw new ForensicsException("No '_id' column found. " + cols.toString());
					}else {
						Log.w(TAG, "Unable to find data for " + this.displayName );
					}
				}
			} finally {
				if (idsOnlyCursor != null) {
					idsOnlyCursor.close();
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, "Unexpected error in (" + displayName + "): ", ex);
			throw new ForensicsException(ex);
		}
		postProcess();
	}

	public int queryForNumberOfRecords(ContentResolver resolver) throws ForensicsException {
		Cursor cursor = null;
		try {
			cursor = resolver.query(this.uri, new String[]{"_id"}, null, null, null);
			if ( cursor != null ) {
				return cursor.getCount();
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return 0;
	}
	
	private void queryContent(ContentResolver resolver, final BufferedWriter writer, 
			final boolean writeColumnNamesRow, final String selection ) throws Exception {
		Cursor cursor = null;
		try {
			cursor = resolver.query(this.uri, getProviderProjection(), selection, null, null );
			if (cursor != null) {
				int numColumns = cursor.getColumnCount();
				if (numColumns > 0) {
					
						if ( writeColumnNamesRow ) this.writeColumnNames(writer, cursor, numColumns);
					    if (cursor.moveToFirst()) {
					        do {
								this.writeColumnValues(writer, cursor, numColumns);
					        } while (cursor.moveToNext());
					    }
					
				}
			}else {
				Log.w(TAG, "Invalid cursor detected. skipping..");
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

	}
	
	protected String[] getProviderProjection() {
		return null;
	}

	
	protected void postProcess() {
		DebugLogger.d(TAG, "completed " + displayName );
	}
	
	protected void writeColumnNames(BufferedWriter writer, Cursor cursor, int numColumns) throws java.io.IOException {
		String[] columnNames = cursor.getColumnNames();
		Log.d(this.getClass().getName(), displayName + " number of column headers = " + columnNames.length + "/" + numColumns );
		
		for (int columnIndex = 0; columnIndex < numColumns; columnIndex++) {
			String formattedHeaderValue = this.formatStringForCSV(columnNames[columnIndex]);
			Log.d(this.getClass().getName(), displayName + " column headers columnIndex = " + columnIndex + "(" + formattedHeaderValue+ ")" );
			writer.write(formattedHeaderValue);
			if ( isNotLastColumn(numColumns, columnIndex) ) writer.write(COMMA_SEP);
		}
		writer.newLine();		
	}

	private boolean isNotLastColumn(int numColumns, int columnIndex) {
		return columnIndex != numColumns - 1;
	}
	
	protected void writeColumnValues(BufferedWriter writer, Cursor cursor, int numColumns) throws Exception {
		for (int columnIndex = 0; columnIndex < numColumns; columnIndex++) {
			writeColumnValue(writer, cursor, columnIndex);
			if ( isNotLastColumn(numColumns, columnIndex) ) writer.write(COMMA_SEP);
		}
		writer.newLine();	
		onAllColumnValuesWrote( cursor, numColumns );
	}

	protected void onAllColumnValuesWrote(Cursor cursor, int numColumns) throws IOException {}

	protected void writeColumnValue(BufferedWriter writer, Cursor cursor, int columnIndex) throws Exception {
		try {
			String value = cursor.getString(columnIndex);
			if (value != null) {
				writer.write(this.formatStringForCSV(value));
			}
		} catch (Exception e) {
			Log.d(TAG, "Error pulling String for index: " + columnIndex);
			throw e;
		}
		
	} 
	
	protected void writeDateValue(BufferedWriter writer, Cursor cursor, int columnIndex) throws IOException {
		String value = cursor.getString(columnIndex);
		String formattedDate = formatDate( value ); 
		writer.write(this.formatStringForCSV( formattedDate ));
	}
	
	public String formatDate(String value) {
		if ( ! isValidDateValue(value) ) {
			return "";
		}
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
		cal.setTime(new Date(Long.valueOf(value)));
		return (String) DateFormat.format("MMMM dd, yyyy h:mmAA", cal);
	}

	private boolean isValidDateValue(String value) {
		return value != null && !"0".equals(value);
	}
	
	protected String formatStringForCSV(String stringToFormat) {
		return CSVCharEscapeWrapper.safeEscape(stringToFormat);
	}
	
	
	
}
