package com.android.openelm.interfaces;

public interface ICommPort {
	public void SetAutoDetect(boolean autoDetect);
	public boolean GetAutoDetect();
	public void SetPort(int aCommPort);
    public void Connect();
    public void Disconnect();
    public boolean HasData();
    public void Flush();
    public int  WriteData(String data);
    public int  ReadData(StringBuilder data);

}
