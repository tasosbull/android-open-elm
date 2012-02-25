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

}
