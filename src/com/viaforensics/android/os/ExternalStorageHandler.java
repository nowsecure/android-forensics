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

package com.viaforensics.android.os;

import java.io.File;

import com.viaforensics.android.ForensicsException;
import com.viaforensics.android.aflogical_ose.R;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class ExternalStorageHandler {
	
	public static boolean isMounted() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
	
	public File prepareStorageLocation( Context context, String subFolder ) throws ForensicsException {
		validateSDCardIsReady();
		return getForensicsFile(context, getFileForExternalStorage(context), subFolder );
		
	}

	private File getForensicsFile(Context context, File esd, String subFolder) throws ForensicsException {
		String subDir = context.getString(R.string.forensics_subdir) + "/" + subFolder;
		File forensicsDir = new File(esd, subDir);
		if (forensicsDir == null || forensicsDir.isFile() || (! forensicsDir.exists() && ! forensicsDir.mkdirs())) {
			// Report problem accessing sub-directory
			throw new ForensicsException(context.getString(R.string.cant_access_subdirectory));			
		}
		return forensicsDir;
	}

	public File getFileForForensicsRoot(Context ctx) throws ForensicsException {
		return new File( getFileForExternalStorage(ctx), ctx.getString(R.string.forensics_subdir) );
	}
	
	public File getFileForExternalStorage(Context context) throws ForensicsException {
		File esd = Environment.getExternalStorageDirectory();
		if (esd == null || ! esd.exists() || ! esd.isDirectory()) {
			throw new ForensicsException(context.getString(R.string.no_external_drive));
		}
		return esd;
	}

	private void validateSDCardIsReady() throws ForensicsException {
		String storageState = Environment.getExternalStorageState();
		Log.d(this.getClass().getName(), "Storage state: " + storageState );
		if ( ! Environment.MEDIA_MOUNTED.equals(storageState) ) {
			throw new ForensicsException("Storage State: " + storageState);
		}
	}
	
    public boolean checkFsWritable(Context context ) {
        // Create a temporary file to see whether a volume is really writeable.
        // It's important not to put it in the root directory which may have a
        // limit on the number of files.
    	
        try {
        	File directory = getFileForForensicsRoot(context);
            if (!directory.isDirectory()) {
	            try {
					boolean mkdirs = directory.mkdirs();
					if (!mkdirs) {
						return false;
					}
				} catch (Throwable t) {
					Log.e("ExternalStorageHandler", "checking for r/w access", t);
		        	throw t;
				}
	        }
            File f = new File(directory, ".dat");
            // Remove stale file if any
            if (f.exists()) {
                f.delete();
            }
            if (!f.createNewFile()) {
                return false;
            }
            f.delete();
            return true;
        } catch (Throwable t) {
            Log.e("ExternalStorageHandler", "checking for r/w access", t);
        	return false;
        }
    }
    
	
}
