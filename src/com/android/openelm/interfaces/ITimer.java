package com.android.openelm.interfaces;

public interface ITimer {
    public  boolean  TimerStarted();
    public  void  StartTimer();
    public  void  StopTimer();
    public  void SetTimerInterval(int interval);  

}
