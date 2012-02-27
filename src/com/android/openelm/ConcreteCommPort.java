package com.android.openelm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.openelm.interfaces.ICommPort;
import com.android.openelm.interfaces.IGui;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class ConcreteCommPort implements ICommPort {

	//private boolean autoDetect = false;

	private int port = 0;
	
	private IGui _gui = null;

	public IGui get_gui() {
		return _gui;
	}

	public void set_gui(IGui _gui) {
		this._gui = _gui;
	}

	protected BluetoothAdapter bluetoothAdapter = null;

	protected BluetoothSocket socket = null;

	BluetoothDevice blueToothDevice = null;
	
	public void SetError(String error){
		if(_gui != null)
			_gui.AddError(error);
	}

	public ConcreteCommPort() {

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			SetError("Bluetooth Not Available.");
			return;
		}
		blueToothDevice = bluetoothAdapter.getRemoteDevice("00:00:00:00:00:00");
		socket = null;

	}
	
	 public boolean FoundBluetooth(){
		 return (bluetoothAdapter != null);
		 
	 }
	
	

	public void SetAutoDetect(boolean autoDetect) {
		// TODO unimlemented yet
//		this.autoDetect = false;
	}

	public boolean GetAutoDetect() {
		// TODO For the future
		return false;
	}

	public void SetPort(int aCommPort) {
		port = aCommPort;
	}

	public void Connect() {
		bluetoothAdapter.cancelDiscovery();
		try {
			Method m = blueToothDevice.getClass().getMethod(
					"createRfcommSocket", new Class[] { int.class });
			socket = (BluetoothSocket) m.invoke(blueToothDevice, port);
			assert (socket != null) : "Socket is Null";
			socket.connect();
		} catch (IOException ex) {
			SetError("IOException " + ex.getMessage());
		} catch (NoSuchMethodException ex) {
			SetError("NoSuchMethodException " + ex.getMessage());
		} catch (IllegalAccessException ex) {
			SetError("IllegalAccessException " + ex.getMessage());
		} catch (InvocationTargetException ex) {
			SetError("InvocationTargetException " + ex.getMessage());
		}
	}

	public void Disconnect() {
		try {
			socket.close();
		} catch (IOException e) {

			SetError("IOException " + e.getMessage());
		}
	}

	public boolean HasData() {
		InputStream mmInStream;
		try {
			mmInStream = socket.getInputStream();
			return (mmInStream.available() > 0);	
		} catch (IOException e) {
			SetError("IOException " + e.getMessage());
			return false;
		}
		
	}

	public void Flush() {
	}

	public int WriteData(String data) {
		try {
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write(data.getBytes());
		} catch (IOException ex) {
			SetError("IOException " + ex.getMessage());
		}

		return 0;
	}

	public int ReadData(StringBuilder data) {
		byte[] buffer = new byte[1024];
		int bytes;
		int total = 0;
		InputStream mmInStream = null;
		try {
			mmInStream = socket.getInputStream();
			bytes = mmInStream.read(buffer);
			total += bytes;
			while (bytes > 0) {
				bytes = mmInStream.read(buffer);
				total += bytes;
			}
			return total;
		} catch (IOException e) {
			SetError(e.getMessage());
			
		}
		return 0;
	}

}
