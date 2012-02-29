// *********************************************************************************
// ***** BEGIN GPL LICENSE BLOCK *****
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software  Foundation,
// Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//
// The Original Code is Copyright (C) 2012 by Tasos Boulasikis tasosbull@gmail.com 
// All rights reserved.
//
// The Original Code is: all of this file.
//
// Contributor(s): none yet.
//
// ***** END GPL LICENSE BLOCK *****
//
// Short description of this file
//************************************************************************************

package com.android.openelm;

import android.util.Log;

import com.android.openelm.interfaces.IGui;

public class ConcreteGui implements IGui{

	public void AddError(String aError) {
		Log.e(this.toString(), " " + aError);
		
	}

	public void ProcMessages() {
		// TODO Android do not support this task
		
	}

	public int GetPortSelected() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int GetElmProtoSelected() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int GetBankLayoutElementsSelected() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void SetError(String error) {
		// TODO Auto-generated method stub
		
	}
	
	 public void SetPidValue(int bankPosition, ElmBankElement elem, double value){
		 
	 }

	public String GetSelectedDevice() {
		// TODO Auto-generated method stub
		return null;
	}

}
