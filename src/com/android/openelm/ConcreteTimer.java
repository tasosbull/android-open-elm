package com.android.openelm;

import com.android.openelm.interfaces.ITimer;

import android.os.CountDownTimer;

public class ConcreteTimer implements ITimer {

	boolean started ;
    boolean errorTimeout = false;
    int timerTimeout = 0;
    int interval = 0;
	
	ElmCountDownTimer timer = null;
	

	public void SetTimerInterval(int aInterval) {
		interval = aInterval;
		
	}
	public boolean TimerStarted() {
		return started;

	}

	public void StartTimer() {
		started = true;
		errorTimeout = false;
		timer = new ElmCountDownTimer(interval,  interval); 
		timer.start();
	}

	public void StopTimer() {
		timer.cancel();
		started = false;
	}


	public final class ElmCountDownTimer extends CountDownTimer {
		public ElmCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			errorTimeout = true;
			started = false;
			this.cancel();

		}

		@Override
		public void onFinish() {
		}

	}

}
