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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.viaforensics.android.ForensicsException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class MmsPartsProvider extends CSVForensicsProvider {
	
	private static final String TAG = "com.viaforensics.android.providers.MmsPartsProvider";
	public static final String CONTENT_URI = "content://mms/part";
	private int mIdColumnIndex;
	private int mFileNameColumnIndex;
	private int mDataColumnIndex;
	private Uri mProviderUri;
	private File mForensicsDir;
	private ContentResolver mContentResolver;
	
	public MmsPartsProvider(String displayName, Uri uri) {
		super(displayName, uri);
		mProviderUri = uri;
	}
	
	
	
	@Override
	public void process(Context context, File forensicsDir) throws ForensicsException {
		mContentResolver = context.getContentResolver();
		mForensicsDir = forensicsDir;
		super.process(context, forensicsDir);
	}



	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		mContentResolver = null;
		mProviderUri = null;
		mForensicsDir = null;
	}



	@Override
	protected void writeColumnNames(BufferedWriter writer, Cursor cursor, int numColumns) throws IOException {
		 
		for ( int i = 0; i < numColumns; i++ ) {
			String columnName = cursor.getColumnName(i);
			if ( "_id".equals(columnName ) ) {
				mIdColumnIndex = i;
			}
			if ( "cl".equals(columnName) ) {
				mFileNameColumnIndex = i;
			}
			if ( "_data".equals(columnName) ) {
				mDataColumnIndex= i;
			}
			
		}
		super.writeColumnNames(writer, cursor, numColumns);
	}
	
	
	protected void onAllColumnValuesWrote(Cursor cursor, int numColumns) throws IOException {
		super.onAllColumnValuesWrote(cursor, numColumns);
		
			final long dataPartId = cursor.getLong(mIdColumnIndex);
			String data = cursor.getString(mDataColumnIndex);
			final boolean hasEmptyDataValue = (data == null || "".equals( data ) );
			if ( hasEmptyDataValue ) {
				return;
			}
			
			//now save attachment
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream is = null;
	
			try {
				
				Uri dataPartUri = ContentUris.withAppendedId( Uri.parse( MmsPartsProvider.CONTENT_URI ), dataPartId);
				String dataPartFileName = cursor.getString( mFileNameColumnIndex );
				if ( dataPartFileName != null ) {
					
					Log.d(TAG, dataPartUri.toString() );
					Log.d(TAG, "file name: " + dataPartFileName );
					
					is = mContentResolver.openInputStream(dataPartUri);
					final byte[] buffer = new byte[2048];
					int len = is.read(buffer);
					while (len >= 0) {
						baos.write(buffer, 0, len);
						len = is.read(buffer);
					}
					
					final FileOutputStream fos = new FileOutputStream( new File( mForensicsDir, dataPartFileName ) );
					baos.writeTo( fos );

				}else {
					Log.w(TAG, "Invalid filename detected on MMSParts!" + dataPartUri.toString() );
				}
			}catch (IOException e) {
				Log.e(TAG, "Failed to load part data", e);
				cursor.close();
				throw e;
			}finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						Log.e(TAG, "Failed to close stream", e);
					} // Ignore
				}
			}
	}
	

	@Override
	protected void writeColumnValue(BufferedWriter writer, Cursor cursor, int columnIndex) throws Exception {
		super.writeColumnValue(writer, cursor, columnIndex);
	}
		

	
}
