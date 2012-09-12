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

package com.viaforensics.android.view;

import java.util.ArrayList;
import java.util.List;

import com.viaforensics.android.providers.ForensicsProvider;

public class ForensicsProviderListManager {
	
	private List<ForensicsProviderListItem> providerItems;

	public void initializeProviderList(List<ForensicsProvider> providers) {
		providerItems = new ArrayList<ForensicsProviderListItem>();
		for(ForensicsProvider provider : providers ) {
			providerItems.add( new ForensicsProviderListItem( provider, Boolean.TRUE ) );
		}
	}

	public List<ForensicsProviderListItem> getProviderItems() {
		return providerItems;
	}

	public void setProviderItems(List<ForensicsProviderListItem> providerItems) {
		this.providerItems = providerItems;
	}

	public void selectAll() {
		for ( ForensicsProviderListItem rowItem : providerItems ) {
			rowItem.setIsChecked( true );
		}
	}
	
	public void deselectAll() {
		for ( ForensicsProviderListItem rowItem : providerItems ) {
			rowItem.setIsChecked( false );
		}
	}

	public int getCheckedCount() {
		int numChecked = 0;//less than 10? item checks, not worried about performance for simplicity of class
		for ( ForensicsProviderListItem rowItem : providerItems ) {
			if ( rowItem.getIsChecked() ) numChecked++;
		}
		return numChecked;
	}

	public boolean isChecked(int i) {
		return ( providerItems == null ? false : providerItems.get(i).getIsChecked() );
	}

	public void setChecked(boolean checked, int checkboxViewIndex) {
		if ( providerItems != null ) {
			providerItems.get( checkboxViewIndex ).setIsChecked( checked );
		}
	}
	public ForensicsProvider getProvider(int i) {
		return ( providerItems == null ? null : providerItems.get(i).getProvider() );
	}
	
	public List<ForensicsProvider> getSelectedContentProviders() {
		List<ForensicsProvider> subset = new ArrayList<ForensicsProvider>();
		for ( int i = 0; i < providerItems.size(); i++ ) {
			addToSubsetIfChecked(subset, i);
		}
		return subset;
	}

	private void addToSubsetIfChecked(List<ForensicsProvider> subset, int i) {
		if ( this.isChecked(i) ) subset.add( this.getProvider(i) );
	}
	
	public void setManagerCheckedValues(boolean select) {
		if ( select ) {
			this.selectAll();
		}else {
			this.deselectAll();
		}
	}

	
}
