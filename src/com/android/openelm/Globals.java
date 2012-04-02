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

public class Globals {
	public enum ELM_MODE{ SENSOR, CONSOLE, CHECK_ENGINE };
	public enum ELM_METRICS { METRICS1, METRICS2};
	public enum READ_RES {EMPTY, DATA, PROMPT, READ_ERROR };
	public enum COM_STATUS { READY, NOT_OPEN, USER_IGNORED };
    public enum PROC_RES
    {
        HEX_DATA,
        BUS_BUSY,
        BUS_ERROR,
        BUS_INIT_ERROR,
        UNABLE_TO_CONNECT,
        CAN_ERROR,
        DATA_ERROR,
        DATA_ERROR2,
        ERR_NO_DATA,
        BUFFER_FULL,
        SERIAL_ERROR,
        UNKNOWN_CMD,
        RUBBISH,
        INTERFACE_ID,
        INTERFACE_ELM320,
        INTERFACE_ELM322,
        INTERFACE_ELM323,
        INTERFACE_ELM327,
        INTERFACE_OBDLINK,
        STN_MFR_STRING,
        ELM_MFR_STRING
    };

    public enum TIMEOUTS
    {
        PORT_ERROR_TIMEOUT (-11111),
        OBD_REQUEST_TIMEOUT(9900),
        ATZ_TIMEOUT(1500),
        AT_TIMEOUT(1500),
        ECU_TIMEOUT(5000),
        MAX_TIMEOUT(30000),
        DUMMY (80);
        private int code;

        private TIMEOUTS(int c) {
          code = c;
        }

        public int getTIMEOUTS() {
          return code;
        }
    };

    public enum RESETSTATE
    {
        RESET_SEND_RESET_REQUEST,
        RESET_GET_REPLY_TO_RESET,
        RESET_SEND_AT_AT1_REQUEST,
        RESET_GET_AT_AT1_RESPONSE,
        RESET_SEND_AT_AT2_REQUEST,
        RESET_GET_AT_AT2_RESPONSE,
        RESET_START_ECU_TIMER,
        RESET_WAIT_FOR_ECU_TIMEOUT,
        RESET_SEND_DETECT_PROTOCOL_REQUEST,
        RESET_GET_REPLY_TO_DETECT_PROTOCOL,
        RESET_CLOSE_DIALOG,
        RESET_INTERFACE_NOT_FOUND,
        RESET_HANDLE_CLONE
    };

}
