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

import java.util.Timer;
import java.util.TimerTask;

import com.android.openelm.interfaces.ITimer;

import android.os.CountDownTimer;

public class ConcreteTimer implements ITimer {

	boolean started;
	boolean errorTimeout = false;

	public boolean isErrorTimeout() {
		return errorTimeout;
	}

	public void setErrorTimeout(boolean errorTimeout) {
		this.errorTimeout = errorTimeout;
	}

	int timerTimeout = 0;
	int interval = 0;

	Timer timer = null;

	public void SetTimerInterval(int aInterval) {
		interval = aInterval;

	}

	public boolean TimerStarted() {
		return started;

	}

	public void StartTimer() {
		started = true;
		errorTimeout = false;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				TimerMethod();
			}
		}, 0, interval);
	}

	private void TimerMethod() {
		errorTimeout = true;
		started = false;
		timer.cancel();

	}

	public void StopTimer() {
		timer.cancel();
		started = false;
		errorTimeout = false;
	}

}