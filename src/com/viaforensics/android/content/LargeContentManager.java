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

package com.viaforensics.android.content;

import android.database.Cursor;

import com.viaforensics.android.logs.DebugLogger;

public class LargeContentManager {
	
	public static final int BLOCKSIZE = 2000;
	

	public static long[] buildIdBlocks(Cursor idsOnlyCursor) {
		
		
		idsOnlyCursor.moveToFirst();
		long[] idList = new long[ idsOnlyCursor.getCount() ];
		int i = 0;
		do {
			idList[i++] = idsOnlyCursor.getLong(0);
		}while(idsOnlyCursor.moveToNext());
		
		
		return idList;
	}

	public static long[][] parseIds(long[] idList, int chunkSize) {
		
		if ( idList == null || idList.length == 0 ) return null;
		
		final int divisor   = ( idList.length / chunkSize );
		final int remainder = ( idList.length % chunkSize );
		final boolean hasRemainder = ( remainder != 0);
		
		DebugLogger.d("AndroidForensics", "divisor = " + divisor);
		DebugLogger.d("AndroidForensics", "remainder = " + remainder);
		DebugLogger.d("AndroidForensics", "last is = " + idList[idList.length-1]);
		
		final int numChunks = ( divisor + ( hasRemainder ? 1 : 0 ) );
		long[][] idChunks = new long[numChunks][2];
		
		for ( int i = 1; i <= numChunks; i ++) {
			final int previousIndex = i-1;
			final int minIdIndex = ( i * chunkSize ) - chunkSize;
			final int maxIdIndex = ( i == numChunks ? remainder + ( previousIndex * chunkSize ) : ( i * chunkSize ) ) - 1;
			
			DebugLogger.d("AndroidForensics", "minIdIndex = " + minIdIndex );
			DebugLogger.d("AndroidForensics", "maxIdIndex = " + maxIdIndex );
			
			idChunks[previousIndex][0] = idList[ minIdIndex ];
			idChunks[previousIndex][1] = idList[ maxIdIndex ];
		}
		
		return idChunks;
	}
	
	
	
}
