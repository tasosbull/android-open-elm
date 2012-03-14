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
import com.android.openelm.interfaces.ITimer;

public class ElmCore {

	// public boolean errorTimeout;
	private Globals.PROC_RES _device;
	private StringBuilder vehicle_response;
	private String cmd;
	public String resetError;
	public Globals.RESETSTATE state;

	ICommPort comm;
	public ITimer timer;
	IGui gui;

	public ElmCore(ICommPort aCommPort, ITimer aTimer, IGui aGui) {
		comm = aCommPort;
		timer = aTimer;
		gui = aGui;
	}

	public void SendCommand(String command) {
		// Globals.TIMEOUTS gt = Globals.TIMEOUTS.OBD_REQUEST_TIMEOUT;
		// timer.SetTimerInterval(gt.getTIMEOUTS()); // start serial timer
		// timer.StartTimer();

		// StringBuilder buf = new StringBuilder("");
		// ReadPort(buf);
		command += '\r';
		// comm.Flush();
		comm.WriteData(command);
	}

	protected void Wait() {
		try {
			while ((comm.HasData() <= 0) && (!timer.isErrorTimeout()))
				gui.ProcMessages();
		} catch (IOException e) {
			gui.AddError(e.getMessage());
		}

	}

	protected boolean CharIsHex(char x) {
		return (x >= '0' && x <= '9') || (x >= 'a' && x <= 'f')
				|| (x >= 'A' && x <= 'F');
	}

	public int ReadQueue(StringBuilder response) {
		Wait();
		if (timer.isErrorTimeout())
			return 0;
		return comm.ReadData(response);

	}

	public String GetCommandResult(String command) {
		StringBuilder buf = new StringBuilder("");
		SendCommand(command);
		//Globals.TIMEOUTS gt = Globals.TIMEOUTS.MAX_TIMEOUT;
		//timer.SetTimerInterval(gt.getTIMEOUTS());
		//timer.StartTimer();
		ReadPort(buf);
		//timer.StopTimer();
		//if (timer.isErrorTimeout()) {
		//	gui.AddError(cmd + " command" + " failed...");
		//	return "Error Timeout";
		//}
		return buf.toString();

	}

	public void SendCommandNoRead(String cmd) {
		StringBuilder buf = new StringBuilder("");
		SendCommand(cmd); // reset the chip
		//Globals.TIMEOUTS gt = Globals.TIMEOUTS.MAX_TIMEOUT;
		//timer.SetTimerInterval(gt.getTIMEOUTS()); // start serial timer
		//timer.StartTimer();
		ReadPort(buf);
		//timer.StopTimer();
		//if (timer.isErrorTimeout()) {
		//	gui.AddError(cmd + " command" + " failed...");
		//	return;
		//}

	}

	public Globals.READ_RES ReadPort(StringBuilder response) {
		boolean escape = false;
		boolean prompt = false;
		while (!escape) {
			//Wait();
			StringBuilder tmp = new StringBuilder("");
			comm.ReadData(tmp);
			String s = tmp.toString();
			if ((s != null) && (!s.equals(""))) {
				response.append(s);
				prompt = s.indexOf(">") >= 0;
			}
			escape = (prompt /*|| timer.isErrorTimeout()*/);
		}
		if (prompt) {
			return Globals.READ_RES.PROMPT;
		}

		else {
			// gui.AddError("PORT_READ_ERROR");
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
		int i = 0;
		boolean echo_on = true; // echo status
		boolean is_hex_num = true;
		StringBuilder temp_buf = new StringBuilder("");
		echo_on = msg_received.toString().indexOf(cmd_sent) >= 0;
		if (echo_on) {
			msg_received = new StringBuilder(msg_received.toString().substring(
					cmd_sent.length(),
					msg_received.toString().length() - cmd_sent.length()));
			SendCommand("ate0"); // turn off echo
			Globals.TIMEOUTS gt = Globals.TIMEOUTS.MAX_TIMEOUT;
			timer.SetTimerInterval(gt.getTIMEOUTS());
			timer.StartTimer();
			Globals.READ_RES res = ReadPort(temp_buf);
			timer.StopTimer();
			if (res == Globals.READ_RES.PROMPT) {
				SendCommand("atl0");
				timer.SetTimerInterval(gt.getTIMEOUTS());
				timer.StartTimer();
				res = ReadPort(temp_buf);
				timer.StopTimer();
			}

			msg_received = new StringBuilder(msg_received.toString().trim());
		}
		// clean up known garbages
		String str = msg_received.toString();
		str = str.replace("SEARCHING...", "");
		str = str.replace("BUS INIT: OK", "");
		str = str.replace("BUS INIT: ...OK", "");
		str = str.replace("\r", "");
		str = str.replace("\t", "");
		str = str.replace(" ", "");
		str = str.replace(">", "");
		str = str.trim();
		msg_received = new StringBuilder(str);
		if ((msg_received.length() >= 10)
				&& (msg_received.substring(0, 10) == "<")) {
			if (msg_received.substring(0, 10).compareTo("<DATA ERROR") == 0)
				return Globals.PROC_RES.DATA_ERROR2;
			else
				return Globals.PROC_RES.RUBBISH;
		}
		for (i = 0; i < msg_received.length(); i++) {
			if (!CharIsHex(msg_received.charAt(i)))
				is_hex_num = false;

		}
		if (is_hex_num)
			return Globals.PROC_RES.HEX_DATA;
		if (msg_received.indexOf("UNABLETOCONNECT") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.UNABLE_TO_CONNECT));
			return Globals.PROC_RES.UNABLE_TO_CONNECT;
		}
		if (msg_received.indexOf("BUSBUSY") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.BUS_BUSY));
			return Globals.PROC_RES.BUS_BUSY;
		}
		if (msg_received.indexOf("DATAERROR") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.DATA_ERROR));
			return Globals.PROC_RES.DATA_ERROR;
		}
		if ((msg_received.indexOf("BUSERROR") >= 0)
				|| (msg_received.indexOf("FBERROR") >= 0)) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.BUS_ERROR));
			return Globals.PROC_RES.BUS_ERROR;
		}
		if (msg_received.indexOf("CANERROR") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.CAN_ERROR));
			return Globals.PROC_RES.CAN_ERROR;
		}
		if (msg_received.indexOf("BUFFERFULL") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.BUFFER_FULL));
			return Globals.PROC_RES.BUFFER_FULL;
		}

		if ((msg_received.indexOf("BUSINIT:ERROR") >= 0)
				|| (msg_received.indexOf("BUSINIT:...ERROR") >= 0)) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.BUS_INIT_ERROR));
			return Globals.PROC_RES.BUS_INIT_ERROR;
		}
		if ((msg_received.indexOf("BUS INIT:") >= 0)
				|| (msg_received.indexOf("BUS INIT:...") >= 0)) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.SERIAL_ERROR));
			return Globals.PROC_RES.SERIAL_ERROR;
		}
		if (msg_received.indexOf("?") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.UNKNOWN_CMD));
			return Globals.PROC_RES.UNKNOWN_CMD;
		}
		if (msg_received.indexOf("ELM320") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.INTERFACE_ELM320));
			return Globals.PROC_RES.INTERFACE_ELM320;
		}
		if (msg_received.indexOf("ELM322") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.INTERFACE_ELM322));
			return Globals.PROC_RES.INTERFACE_ELM322;
		}
		if (msg_received.indexOf("ELM323") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.INTERFACE_ELM323));
			return Globals.PROC_RES.INTERFACE_ELM323;
		}
		if (msg_received.indexOf("ELM327") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.INTERFACE_ELM327));
			return Globals.PROC_RES.INTERFACE_ELM327;
		}
		if (msg_received.indexOf("OBDLink") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.INTERFACE_OBDLINK));
			return Globals.PROC_RES.INTERFACE_OBDLINK;
		}
		if (msg_received.indexOf("SCANTOOL.NET") >= 0) {
			gui.AddError(BadResponseToString(Globals.PROC_RES.STN_MFR_STRING));
			return Globals.PROC_RES.STN_MFR_STRING;
		}
		if (msg_received.indexOf("OBDIItoRS232Interpreter") >= 0) {
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
		res = (buf.indexOf(filter) == 0);
		if (res)

			buf = buf
					.substring(filter.length(), buf.length() - filter.length());
		strbuf = new StringBuilder(buf);
		return res;
	}

	private Globals.RESETSTATE Reset_send_reset_request() {
		SendCommand("atz"); // reset chip
		Globals.TIMEOUTS gt = Globals.TIMEOUTS.ATZ_TIMEOUT;
		timer.SetTimerInterval(gt.getTIMEOUTS()); // start serial timer
		vehicle_response = new StringBuilder("");
		return Globals.RESETSTATE.RESET_GET_REPLY_TO_RESET;

	}

	private Globals.RESETSTATE Reset_get_reply_to_reset() {
		StringBuilder buf = new StringBuilder("");
		Globals.RESETSTATE next_state;
		Globals.READ_RES status;

		timer.StartTimer();
		status = ReadPort(buf);
		timer.StopTimer();
		if (status == Globals.READ_RES.PROMPT) // if '>' detected
		{
			vehicle_response = buf;
			_device = ProcessResponse("atz", vehicle_response);
			if (_device == Globals.PROC_RES.INTERFACE_ELM323)
				next_state = Globals.RESETSTATE.RESET_START_ECU_TIMER;
			else if (_device == Globals.PROC_RES.INTERFACE_ELM327)
				next_state = Globals.RESETSTATE.RESET_SEND_AT_AT1_REQUEST;
			else
				next_state = Globals.RESETSTATE.RESET_CLOSE_DIALOG;
		} else if (timer.isErrorTimeout()) // if the timer timed out
		{
			next_state = Globals.RESETSTATE.RESET_CLOSE_DIALOG; // close dialog
		} else
			next_state = Globals.RESETSTATE.RESET_CLOSE_DIALOG;
		return next_state;
	}

	private Globals.RESETSTATE Reset_send_at_at1_request() {
		SendCommand("at@1");
		Globals.TIMEOUTS gt = Globals.TIMEOUTS.AT_TIMEOUT;

		timer.SetTimerInterval(gt.getTIMEOUTS()); // start serial timer
		vehicle_response = new StringBuilder("");
		return Globals.RESETSTATE.RESET_GET_AT_AT1_RESPONSE;
	}

	private Globals.RESETSTATE Reset_get_at_at1_response() {
		StringBuilder buf = new StringBuilder("");
		Globals.RESETSTATE next_state;
		Globals.READ_RES status;
		Globals.PROC_RES procRes;

		timer.StartTimer();
		status = ReadPort(buf);
		timer.StopTimer();
		if (status == Globals.READ_RES.PROMPT) {
			vehicle_response = buf;
			procRes = ProcessResponse("at@1", vehicle_response);
			if (procRes == Globals.PROC_RES.STN_MFR_STRING)
				next_state = Globals.RESETSTATE.RESET_START_ECU_TIMER;
			else if (procRes == Globals.PROC_RES.ELM_MFR_STRING)
				next_state = Globals.RESETSTATE.RESET_SEND_AT_AT2_REQUEST;
			else
				next_state = Globals.RESETSTATE.RESET_HANDLE_CLONE;

		} else if (timer.isErrorTimeout()) {
			next_state = Globals.RESETSTATE.RESET_CLOSE_DIALOG;
		} else
			// serial buffer was empty, but we still got time
			next_state = Globals.RESETSTATE.RESET_GET_AT_AT1_RESPONSE;

		return next_state;

	}

	private Globals.RESETSTATE Reset_send_at_at2_request() {

		SendCommand("at@2");
		Globals.TIMEOUTS gt = Globals.TIMEOUTS.AT_TIMEOUT;
		timer.SetTimerInterval(gt.getTIMEOUTS()); // start serial timer
		vehicle_response = new StringBuilder("");

		return Globals.RESETSTATE.RESET_GET_AT_AT2_RESPONSE;

	}

	private Globals.RESETSTATE Reset_get_at_at2_response() {
		StringBuilder buf = new StringBuilder("");
		Globals.RESETSTATE next_state;
		Globals.READ_RES status;
		Globals.PROC_RES procRes;

		timer.StartTimer();
		status = ReadPort(buf);
		timer.StopTimer();
		if (status == Globals.READ_RES.PROMPT) // if '>' detected
		{
			vehicle_response = buf;
			procRes = ProcessResponse("at@2", vehicle_response);

			if (procRes == Globals.PROC_RES.STN_MFR_STRING)
				next_state = Globals.RESETSTATE.RESET_START_ECU_TIMER;
			else
				next_state = Globals.RESETSTATE.RESET_HANDLE_CLONE;
		} else if (timer.isErrorTimeout()) // if the timer timed out
		{
			next_state = Globals.RESETSTATE.RESET_CLOSE_DIALOG; // close dialog
		} else
			// serial buffer was empty, but we still got time
			next_state = Globals.RESETSTATE.RESET_GET_AT_AT2_RESPONSE;

		return next_state;

	}

	private Globals.RESETSTATE Reset_start_ecu_timer() {
		Globals.TIMEOUTS gt = Globals.TIMEOUTS.ECU_TIMEOUT;
		timer.SetTimerInterval(gt.getTIMEOUTS());
		return Globals.RESETSTATE.RESET_WAIT_FOR_ECU_TIMEOUT;
	}

	private Globals.RESETSTATE Reset_wait_for_ecu_timeout() {
		Globals.RESETSTATE next_state;
		if (timer.isErrorTimeout()) {
			if (_device == Globals.PROC_RES.INTERFACE_ELM327)
				next_state = Globals.RESETSTATE.RESET_SEND_DETECT_PROTOCOL_REQUEST;
			else
				next_state = Globals.RESETSTATE.RESET_CLOSE_DIALOG;
		} else
			next_state = Globals.RESETSTATE.RESET_WAIT_FOR_ECU_TIMEOUT;

		return next_state;
	}

	private Globals.RESETSTATE Reset_send_detect_protocol_request() {
		Globals.RESETSTATE next_state;
		SendCommand("0100");

		Globals.TIMEOUTS gt = Globals.TIMEOUTS.OBD_REQUEST_TIMEOUT;
		timer.SetTimerInterval(gt.getTIMEOUTS());
		vehicle_response = new StringBuilder("");
		next_state = Globals.RESETSTATE.RESET_GET_REPLY_TO_DETECT_PROTOCOL;

		return next_state;
	}

	private Globals.RESETSTATE Reset_get_reply_to_detect_protocol() {
		StringBuilder buf = new StringBuilder("");
		Globals.RESETSTATE next_state;
		Globals.READ_RES status;
		Globals.PROC_RES procRes;

		timer.StartTimer();
		status = ReadPort(buf);
		timer.StopTimer();
		if (status == Globals.READ_RES.PROMPT) {
			vehicle_response = buf;
			procRes = ProcessResponse("0100", vehicle_response);

			if (procRes == Globals.PROC_RES.ERR_NO_DATA
					|| procRes == Globals.PROC_RES.UNABLE_TO_CONNECT)
				resetError = "Protocol could not be detected. ";
			else if (procRes != Globals.PROC_RES.HEX_DATA)
				resetError = "Communication error";
			next_state = Globals.RESETSTATE.RESET_CLOSE_DIALOG;
		} else if (timer.isErrorTimeout()) {
			resetError = "Interface not found";
			next_state = Globals.RESETSTATE.RESET_CLOSE_DIALOG;
		} else
			next_state = Globals.RESETSTATE.RESET_GET_REPLY_TO_DETECT_PROTOCOL;

		return next_state;

	}

	private Globals.RESETSTATE Reset_handle_clone() {
		return Globals.RESETSTATE.RESET_CLOSE_DIALOG;
	}

	public void Reset_Proc() {
		if (state == Globals.RESETSTATE.RESET_CLOSE_DIALOG)
			return;
		switch (state) {
		case RESET_SEND_RESET_REQUEST:
			state = Reset_send_reset_request();
			Reset_Proc();
			break;
		case RESET_GET_REPLY_TO_RESET:
			state = Reset_get_reply_to_reset();
			Reset_Proc();
			break;
		case RESET_SEND_AT_AT1_REQUEST:
			state = Reset_send_at_at1_request();
			Reset_Proc();
			break;
		case RESET_GET_AT_AT1_RESPONSE:
			state = Reset_get_at_at1_response();
			Reset_Proc();
			break;
		case RESET_SEND_AT_AT2_REQUEST:
			state = Reset_send_at_at2_request();
			Reset_Proc();
			break;
		case RESET_GET_AT_AT2_RESPONSE:
			state = Reset_get_at_at2_response();
			Reset_Proc();
			break;
		case RESET_START_ECU_TIMER:
			state = Reset_start_ecu_timer();
			Reset_Proc();
			break;
		case RESET_WAIT_FOR_ECU_TIMEOUT:
			state = Reset_wait_for_ecu_timeout();
			Reset_Proc();
			break;
		case RESET_SEND_DETECT_PROTOCOL_REQUEST:
			state = Reset_send_detect_protocol_request();
			Reset_Proc();
			break;
		case RESET_GET_REPLY_TO_DETECT_PROTOCOL:
			state = Reset_get_reply_to_detect_protocol();
			Reset_Proc();
			break;
		case RESET_HANDLE_CLONE:
			state = Reset_handle_clone();
			Reset_Proc();
			break;
		case RESET_CLOSE_DIALOG:
			break;

		}
	}

	public void Reset() {
		state = Globals.RESETSTATE.RESET_SEND_RESET_REQUEST;
		Reset_Proc();
	}

	public String GetPidResponse(String pid, int pidBytes) {
		StringBuilder buf = new StringBuilder("");
		// Globals.READ_RES response_status;
		Globals.PROC_RES resState;
		cmd = "01" + pid;
		vehicle_response = new StringBuilder("");
		SendCommand(cmd);
		Globals.TIMEOUTS gt = Globals.TIMEOUTS.OBD_REQUEST_TIMEOUT;

		timer.SetTimerInterval(gt.getTIMEOUTS());
		timer.StartTimer();
		/* response_status = */ReadPort(vehicle_response);
		timer.StopTimer();
		String vr = vehicle_response.toString();
		if (timer.isErrorTimeout()) {

			return "ERROR TIMEOUT";
		}
		resState = ProcessResponse(cmd, vehicle_response);
		if (resState == Globals.PROC_RES.HEX_DATA) {
			cmd = "41" + pid;
			String s = buf.toString();
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

	public int GetNumOfDTC(boolean noError, boolean milIsOn) {
		StringBuilder buf = new StringBuilder("");
		// Globals.READ_RES response_status;
		Globals.PROC_RES resState;
		cmd = "0101";
		vehicle_response = new StringBuilder("");
		SendCommand(cmd);

		Globals.TIMEOUTS gt = Globals.TIMEOUTS.OBD_REQUEST_TIMEOUT;
		timer.SetTimerInterval(gt.getTIMEOUTS());
		timer.StartTimer();
		/* response_status = */ReadPort(vehicle_response);
		timer.StopTimer();
		if (timer.isErrorTimeout()) {
			noError = false;
			return 0;
		}
		resState = ProcessResponse(cmd, vehicle_response);
		if (resState == Globals.PROC_RES.HEX_DATA) {
			cmd = "4101";
			if (IsValidResponse(buf, vehicle_response.toString(), cmd)) {
				if (buf.length() > 2) {
					String tmp = buf.substring(0, 2);
					int itmp = HexToInt(tmp);
					noError = true;
					milIsOn = (itmp & 0x80) == 0x80;
					return (itmp & 0x7F);
				}
				noError = true;
				milIsOn = false;
				return 0;
			} else {
				noError = false;
				return 0;
			}
		} else {
			noError = false;
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

	public void ResetDtc(boolean noError) {
		noError = false;
		StringBuilder buf = new StringBuilder("");
		// Globals.READ_RES response_status;
		Globals.PROC_RES resState;
		cmd = "04";
		vehicle_response = new StringBuilder("");
		SendCommand(cmd);
		Globals.TIMEOUTS gt = Globals.TIMEOUTS.OBD_REQUEST_TIMEOUT;
		timer.SetTimerInterval(gt.getTIMEOUTS());
		timer.StartTimer();
		/* response_status = */ReadPort(vehicle_response);
		timer.StopTimer();
		if (timer.isErrorTimeout()) {
			noError = false;
			return;
		}
		resState = ProcessResponse(cmd, vehicle_response);
		if (resState == Globals.PROC_RES.HEX_DATA) {
			cmd = "44";
			if (IsValidResponse(buf, vehicle_response.toString(), cmd)) {
				noError = true;
				return;
			}
		}

	}

	public ArrayList<String> getDtcList(boolean noError) {
		ArrayList<String> list = new ArrayList<String>();
		StringBuilder buf = new StringBuilder("");
		// Globals.READ_RES response_status;
		Globals.PROC_RES resState;
		cmd = "03";
		vehicle_response = new StringBuilder("");
		SendCommand(cmd);
		Globals.TIMEOUTS gt = Globals.TIMEOUTS.OBD_REQUEST_TIMEOUT;

		timer.SetTimerInterval(gt.getTIMEOUTS());
		timer.StartTimer();
		/* response_status = */ReadPort(vehicle_response);
		timer.StopTimer();
		if (timer.isErrorTimeout()) {
			noError = false;
			return null;
		}
		resState = ProcessResponse(cmd, vehicle_response);
		if (resState == Globals.PROC_RES.HEX_DATA) {
			cmd = "43";
			if (IsValidResponse(buf, vehicle_response.toString(), cmd)) {
				int cnt = buf.length() / 4;
				for (int i = 0; i < cnt; i++) {
					String tmp = buf.substring(i * 4, 4);
					if (!tmp.equals("0000")) {
						String prefix = DTCLookupFirstChar(tmp.charAt(0));
						list.add(prefix + tmp.substring(1, 3));
					}
				}
			} else {
				noError = false;
				return null;
			}
		} else {
			noError = false;
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
