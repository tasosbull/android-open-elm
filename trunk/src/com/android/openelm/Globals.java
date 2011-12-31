package com.android.openelm;

public class Globals {
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
