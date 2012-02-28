package com.android.openelm.interfaces;

import java.util.List;

public interface ICommPort {
	public void SetAutoDetect(boolean autoDetect);
	public boolean GetAutoDetect();
	public void SetPort(int aCommPort);
    public boolean Connect(String device);
    public void Disconnect();
    public boolean HasData();
    public void Flush();
    public int  WriteData(String data);
    public int  ReadData(StringBuilder data);
    public boolean FoundBluetooth();
    public void SetError(String error);
    public List<String> GetNameDevices();
    

}
