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
