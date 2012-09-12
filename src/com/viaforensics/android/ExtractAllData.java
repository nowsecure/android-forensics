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

package com.viaforensics.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.viaforensics.android.aflogical_ose.R;

public class ExtractAllData extends Activity {
	
	private ForensicsGatherer mGatherer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		
		mGatherer = new ForensicsGatherer(this);
		try {
			mGatherer.processProviders();
		} catch (ForensicsException e) {
			processError(e);
		}
		
	}
	
	private void processError(Exception ex) {
		Log.e(this.getClass().getName(), "Error message: ", ex );
		showErrorOccuredToast(ex.getMessage());
	}
	
	public void showErrorOccuredToast(String errorMessage) {
		Toast.makeText( this, ForensicsApplication.getApplicationName() + " detected an error.", Toast.LENGTH_SHORT).show();
	}
}
