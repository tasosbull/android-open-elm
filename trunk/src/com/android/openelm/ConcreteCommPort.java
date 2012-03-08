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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.android.openelm.interfaces.ICommPort;
import com.android.openelm.interfaces.IGui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class ConcreteCommPort implements ICommPort {
	Set<BluetoothDevice> pairedDevices = null;

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

	public void SetError(String error) {
		if (_gui != null)
			_gui.AddError(error);
	}

	public ConcreteCommPort() {

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			SetError("Bluetooth Not Available.");
			return;
		}
		blueToothDevice = null;
		socket = null;
	}

	public boolean FoundBluetooth() {
		return (bluetoothAdapter != null);

	}

	public void SetAutoDetect(boolean autoDetect) {
	}

	public boolean GetAutoDetect() {
		// TODO For the future
		return false;
	}

	
	public void SetPort(int aCommPort) {
		port = aCommPort;
	}

	public boolean Connect(String deviceName) {
		bluetoothAdapter.cancelDiscovery();
		if (deviceName == null) {
			SetError("You must select a bluetooth device");
			return false;
		}
		try {
			for (BluetoothDevice device : pairedDevices) {
				if (device.getName().equals(deviceName)) {
					blueToothDevice = device;
					break;
				}
			}
			if (blueToothDevice == null) {
				SetError("Bluetooth device is lost");
				return false;
			}
			Method m = blueToothDevice.getClass().getMethod(
					"createRfcommSocket", new Class[] { int.class });
			socket = (BluetoothSocket) m.invoke(blueToothDevice, port);
			assert (socket != null) : "Socket is Null";
			socket.connect();
			return true;
		} catch (IOException ex) {
			SetError("IOException " + ex.getMessage());
			return false;
		} catch (NoSuchMethodException ex) {
			SetError("NoSuchMethodException " + ex.getMessage());
			return false;
		} catch (IllegalAccessException ex) {
			SetError("IllegalAccessException " + ex.getMessage());
			return false;
		} catch (InvocationTargetException ex) {
			SetError("InvocationTargetException " + ex.getMessage());
			return false;
		}
	}

	public void Disconnect() {
		try {
			socket.close();
		} catch (IOException e) {

			SetError("IOException " + e.getMessage());
		}
	}

	public boolean HasData() throws IOException {
		InputStream mmInStream;

		mmInStream = socket.getInputStream();
		boolean result = (mmInStream.available() > 0);
		return result;
	}

	public void Flush() {
	}

	public int WriteData(String data) {
		try {
			OutputStream outputStream = socket.getOutputStream();
			outputStream.flush();
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
			while ((HasData())) {
				bytes = mmInStream.read(buffer);
				total += bytes;
			}
			data.append(new String(buffer));
			String str = data.toString();
			str = str;
			
		} catch (IOException e) {
			SetError(e.getMessage());

		}
		return total;
	}

	private void GetBlueToothDevices() {
		pairedDevices = bluetoothAdapter.getBondedDevices();
	}

	public List<String> GetNameDevices() {
		GetBlueToothDevices();
		ArrayList<String> list = new ArrayList<String>();
		for (BluetoothDevice device : pairedDevices) {
			list.add(device.getName());
		}
		return list;
	}

}
