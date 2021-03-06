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
import java.util.ArrayList;

import com.android.openelm.interfaces.ICommPort;
import com.android.openelm.interfaces.IGui;


public class ElmCore {
	private StringBuilder vehicle_response;
	private String cmd;
	public String resetError;
	public Globals.RESETSTATE state;

	ICommPort comm;

	IGui gui;

	public ElmCore(ICommPort aCommPort, IGui aGui) {
		comm = aCommPort;

		gui = aGui;
	}

	public void SendCommand(String command) {
		command += '\r';
		comm.WriteData(command);
	}

	protected void Wait() {
		try {
			while ((comm.HasData() <= 0) )
				gui.ProcMessages();
		} catch (IOException e) {
			gui.AddError(e.getMessage());
		}

	}

	protected boolean CharIsHex(char x) {
		return (x >= '0' && x <= '9') || (x >= 'a' && x <= 'f')
				|| (x >= 'A' && x <= 'F');
	}


	public String GetCommandResult(String command) {
		StringBuilder buf = new StringBuilder("");
		SendCommand(command);
		ReadPort(buf);
		return buf.toString();

	}

	public void SendCommandNoRead(String cmd) {
		StringBuilder buf = new StringBuilder("");
		SendCommand(cmd); // reset the chip
		ReadPort(buf);
	}

	public Globals.READ_RES ReadPort(StringBuilder response) {
		boolean escape = false;
		boolean prompt = false;
		while (!escape) {
			StringBuilder tmp = new StringBuilder("");
			comm.ReadData(tmp);
			String s = tmp.toString();
			if ((s != null) && (!s.equals(""))) {
				response.append(s);
				prompt = s.indexOf(">") >= 0;
			}
			escape = (prompt);
		}
		if (prompt) {
			return Globals.READ_RES.PROMPT;
		}

		else {
			return Globals.READ_RES.READ_ERROR;
		}

	}

	public void FastInit() {
		SendCommandNoRead("atz");
		SendCommandNoRead("ate0");
		SendCommandNoRead("atl0");
	}

	protected Globals.PROC_RES ProcessResponse(String cmd_sent,
			StringBuilder msg_received) {
		String str = msg_received.toString();
		int i = 0;
		boolean is_hex_num = true;
		str = str.replace("SEARCHING...", "");
		str = str.replace("BUS INIT: OK", "");
		str = str.replace("BUS INIT: ...OK", "");
		str = str.replace("\r", "");
		str = str.replace("\t", "");
		str = str.replace(" ", "");
		str = str.replace(">", "");
		str = str.trim();
		msg_received.delete(0, msg_received.length());
		msg_received.append(str);
		if ((str.length() >= 10)
				&& (str.substring(0, 10) == "<")) {
			if (str.substring(0, 10).compareTo("<DATA ERROR") == 0)
				return Globals.PROC_RES.DATA_ERROR2;
			else
				return Globals.PROC_RES.RUBBISH;
		}
		for (i = 0; i < str.length(); i++) {
			if (!CharIsHex(str.charAt(i)))
				is_hex_num = false;

		}
		if (is_hex_num)
			return Globals.PROC_RES.HEX_DATA;
		if (str.indexOf("UNABLETOCONNECT") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.UNABLE_TO_CONNECT));
			return Globals.PROC_RES.UNABLE_TO_CONNECT;
		}
		if (str.indexOf("BUSBUSY") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.BUS_BUSY));
			return Globals.PROC_RES.BUS_BUSY;
		}
		if (str.indexOf("DATAERROR") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.DATA_ERROR));
			return Globals.PROC_RES.DATA_ERROR;
		}
		if ((str.indexOf("BUSERROR") >= 0)
				|| (str.indexOf("FBERROR") >= 0)) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.BUS_ERROR));
			return Globals.PROC_RES.BUS_ERROR;
		}
		if (str.indexOf("CANERROR") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.CAN_ERROR));
			return Globals.PROC_RES.CAN_ERROR;
		}
		if (str.indexOf("BUFFERFULL") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.BUFFER_FULL));
			return Globals.PROC_RES.BUFFER_FULL;
		}

		if ((str.indexOf("BUSINIT:ERROR") >= 0)
				|| (msg_received.indexOf("BUSINIT:...ERROR") >= 0)) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.BUS_INIT_ERROR));
			return Globals.PROC_RES.BUS_INIT_ERROR;
		}
		if ((str.indexOf("BUS INIT:") >= 0)
				|| (msg_received.indexOf("BUS INIT:...") >= 0)) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.SERIAL_ERROR));
			return Globals.PROC_RES.SERIAL_ERROR;
		}
		if (str.indexOf("?") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.UNKNOWN_CMD));
			return Globals.PROC_RES.UNKNOWN_CMD;
		}
		if (str.indexOf("ELM320") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.INTERFACE_ELM320));
			return Globals.PROC_RES.INTERFACE_ELM320;
		}
		if (str.indexOf("ELM322") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.INTERFACE_ELM322));
			return Globals.PROC_RES.INTERFACE_ELM322;
		}
		if (str.indexOf("ELM323") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.INTERFACE_ELM323));
			return Globals.PROC_RES.INTERFACE_ELM323;
		}
		if (str.indexOf("ELM327") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.INTERFACE_ELM327));
			return Globals.PROC_RES.INTERFACE_ELM327;
		}
		if (str.indexOf("OBDLink") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.INTERFACE_OBDLINK));
			return Globals.PROC_RES.INTERFACE_OBDLINK;
		}
		if (str.indexOf("SCANTOOL.NET") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.STN_MFR_STRING));
			return Globals.PROC_RES.STN_MFR_STRING;
		}
		if (str.indexOf("OBDIItoRS232Interpreter") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.ELM_MFR_STRING));
			return Globals.PROC_RES.ELM_MFR_STRING;
		}

		gui.AddError("UNKNOWN_MESSAGE: " + msg_received);
		return Globals.PROC_RES.RUBBISH;

	}

	protected boolean IsValidResponse(StringBuilder strbuf, String response,
			String filter) {
		boolean res = false;

		String buf = response;
		buf = buf.trim();
		buf = buf.replace("\t", "");
		buf = buf.replace(" ", "");
		buf = buf.replace(">", "");
		res = (buf.indexOf(filter) == 0);
		if (res)
			buf = buf.substring(filter.length(), buf.length() );
		strbuf.delete(0, strbuf.length());
		strbuf.append(buf);
		return res;
	}

	public String GetPidResponse(String pid, int pidBytes) {
		StringBuilder buf = new StringBuilder("");
		Globals.PROC_RES resState;
		cmd = "01" + pid;
		vehicle_response = new StringBuilder("");
		SendCommand(cmd);
		ReadPort(vehicle_response);
		resState = ProcessResponse(cmd, vehicle_response);
		if (resState == Globals.PROC_RES.HEX_DATA) {
			cmd = "41" + pid;
			String v = vehicle_response.toString();
			if (IsValidResponse(buf, v, cmd)) {
				if (buf.toString().length() > pidBytes * 2)
					buf = new StringBuilder(buf.toString().substring(0,
							pidBytes * 2));

				return buf.toString();
			} else {

				return "ERROR INVALID RESPONSE";
			}

		} else {

			return "ERROR " + BadResponseToString(resState);
		}
	}

	public int GetNumOfDTC(boolean[] noError, boolean[] milIsOn) {
		StringBuilder buf = new StringBuilder("");
		Globals.PROC_RES resState;
		cmd = "0101";
		vehicle_response = new StringBuilder("");
		SendCommand(cmd);
		ReadPort(vehicle_response);
		resState = ProcessResponse(cmd, vehicle_response);
		if (resState == Globals.PROC_RES.HEX_DATA) {
			cmd = "4101";
			if (IsValidResponse(buf, vehicle_response.toString(), cmd)) {
				if (buf.length() > 2) {
					String tmp = buf.substring(0, 2);
					int itmp = HexToInt(tmp);
					noError[0] = true;
					milIsOn[0] = (itmp & 0x80) == 0x80;
					return (itmp & 0x7F);
				}
				noError[0] = true;
				milIsOn[0] = false;
				return 0;
			} else {
				noError[0] = false;
				return 0;
			}
		} else {
			noError[0] = false;
			return 0;
		}
	}

	public String DTCLookupFirstChar(char ch) {
		switch (ch) {
		case '0':
			return "P0";
		case '1':
			return "P1";
		case '2':
			return "P2";
		case '3':
			return "P3";
		case '4':
			return "C0";
		case '5':
			return "C1";
		case '6':
			return "C2";
		case '7':
			return "C3";
		case '8':
			return "B0";
		case '9':
			return "B1";
		case 'A':
			return "B2";
		case 'B':
			return "B3";
		case 'C':
			return "U0";
		case 'D':
			return "U1";
		case 'E':
			return "U2";
		case 'F':
			return "U3";
		}
		return "";
	}

	public void ResetDtc(boolean[] noError) {
		noError[0] = false;
		StringBuilder buf = new StringBuilder("");
		Globals.PROC_RES resState;
		cmd = "04";
		vehicle_response = new StringBuilder("");
		SendCommand(cmd);
		ReadPort(vehicle_response);
		resState = ProcessResponse(cmd, vehicle_response);
		if (resState == Globals.PROC_RES.HEX_DATA) {
			cmd = "44";
			if (IsValidResponse(buf, vehicle_response.toString(), cmd)) {
				noError[0] = true;
				return;
			}
		}

	}

	//TODO debug this funnction
	public ArrayList<String> getDtcList(boolean[] noError) {
		ArrayList<String> list = new ArrayList<String>();
		StringBuilder buf = new StringBuilder("");
		Globals.PROC_RES resState;
		cmd = "03";
		vehicle_response = new StringBuilder("");
		SendCommand(cmd);
		ReadPort(vehicle_response);
		resState = ProcessResponse(cmd, vehicle_response);
		if (resState == Globals.PROC_RES.HEX_DATA) {
			cmd = "43";
			if (IsValidResponse(buf, vehicle_response.toString(), cmd)) {
				int cnt = buf.toString().length() / 4;
				for (int i = 0; i < cnt; i++) {
					String tmp = buf.toString().substring(i * 4, (i * 4) + 4);
					if (!tmp.equals("0000")) {
						String prefix = DTCLookupFirstChar(tmp.charAt(0));
						list.add(prefix + tmp.substring(1, 1 + 3));
					}
				}
			} else {
				noError[0] = false;
				return null;
			}
		} else {
			noError[0] = false;
			return null;
		}
		return list;

	}

	public String BadResponseToString(Globals.PROC_RES resState) {
		switch (resState) {
		case BUFFER_FULL:
			return "BUFFER_FULL";
		case BUS_BUSY:
			return "BUS_BUSY";
		case BUS_ERROR:
			return "BUS_ERROR";
		case BUS_INIT_ERROR:
			return "BUS_INIT_ERROR";
		case CAN_ERROR:
			return "CAN_ERROR";
		case DATA_ERROR:
			return "DATA_ERROR";
		case DATA_ERROR2:
			return "DATA_ERROR2";
		case ELM_MFR_STRING:
			return "ELM_MFR_STRING";
		case ERR_NO_DATA:
			return "ERR_NO_DATA";
		case HEX_DATA:
			return "HEX_DATA";
		case INTERFACE_ELM320:
			return "INTERFACE_ELM320";
		case INTERFACE_ELM322:
			return "INTERFACE_ELM322";
		case INTERFACE_ELM323:
			return "INTERFACE_ELM323";
		case INTERFACE_ELM327:
			return "INTERFACE_ELM327";
		case INTERFACE_ID:
			return "INTERFACE_ID";
		case INTERFACE_OBDLINK:
			return "INTERFACE_OBDLINK";
		case RUBBISH:
			return "RUBBISH";
		case SERIAL_ERROR:
			return "SERIAL_ERROR";
		case STN_MFR_STRING:
			return "STN_MFR_STRING";
		case UNABLE_TO_CONNECT:
			return "UNABLE_TO_CONNECT";
		case UNKNOWN_CMD:
			return "UNKNOWN_CMD";
		default:
			return "UNKNOWN_ERROR";

		}

	}

	public int HexToInt(String hexStr) {
		return Integer.valueOf(hexStr, 16).intValue();
	}

	public static String HexString2Ascii(String hex) {
		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < hex.length() - 1; i += 2) {
			String output = hex.substring(i, (i + 2));
			int decimal = Integer.parseInt(output, 16);
			sb.append((char) decimal);
			temp.append(decimal);
		}
		System.out.println("Decimal : " + temp.toString());
		return sb.toString();
	}

}
