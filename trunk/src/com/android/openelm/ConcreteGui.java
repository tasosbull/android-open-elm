package com.android.openelm;

import android.util.Log;

import com.android.openelm.interfaces.IGui;

public class ConcreteGui implements IGui{

	public void AddError(String aError) {
		Log.e(this.toString(), " " + aError);
		
	}

	public void ProcMessages() {
		// TODO Unfortunally android do not support this task
		
	}

}
