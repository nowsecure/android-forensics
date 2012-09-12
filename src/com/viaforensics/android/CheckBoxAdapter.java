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

import java.util.List;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import com.viaforensics.android.aflogical_ose.R;

public class CheckBoxAdapter<T> extends ArrayAdapter<T> {
	
	private Context mContext = null;
	
	public CheckBoxAdapter(Context context, int textViewResourceId, List<T> objects) {
		super(context, textViewResourceId, objects);
		mContext = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CheckBox box = (CheckBox) super.getView(position, convertView, parent);
		
		final ForensicsActivity activity = (ForensicsActivity) mContext;
		box.setChecked( activity.getListManager().isChecked(position) );
		
		// Check to see if this is a new box
		if (convertView == null || box != convertView) {
			box.setOnClickListener( activity );		
		}
		
		return box;
	}
	

}
