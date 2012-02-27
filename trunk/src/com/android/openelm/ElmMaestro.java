package com.android.openelm;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParserException;

import com.android.openelm.interfaces.ICommPort;
import com.android.openelm.interfaces.IGui;
import com.android.openelm.interfaces.ITimer;

import android.app.Activity;

public class ElmMaestro {
	private int _port = 1;
	private int _elmProto = 0;
	private int _bankLayout = 4;
	private Activity _activity;
	private List<ElmBankElement> _elements;
	private int _currentBankElement = -1;
	private long _timerRefresh = 5;
	private Timer _localTimer;
	boolean connected = false;
	ExpressionEvaluator eval = null;
	ICommPort comPort = null;
	ITimer timer = null;
	IGui gui = null;
	ElmCore core = null;
	int debugCounter = 0;
	Pattern hex = Pattern.compile("^[0-9A-F]+$");

	public int get_currentBankElement() {
		return _currentBankElement;
	}

	public void set_currentBankElement(int _currentBankElement) {
		this._currentBankElement = _currentBankElement;
	}

	public long get_timerRefresh() {
		return _timerRefresh;
	}

	public void set_timerRefresh(long _timerRefresh) {
		this._timerRefresh = _timerRefresh;
	}

	public ElmMaestro(IGui _gui) {
		gui = _gui;
	}

	public boolean Init() throws Exception {
		if (_activity == null)
			throw new Exception("You must set activity first");
		eval = new ExpressionEvaluator(_activity);
		LoadXmlElements();
		if (!CreatePort())
			return false;
		if (!comPort.Connect())
			return false;
		connected = true;
		CreateTimer();
		core = new ElmCore(comPort, timer, gui);
		core.FastInit();
		return true;

	}

	public void Start() {

		_localTimer = new Timer();
		_localTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				TimerMethod();
			}

		}, 0, _timerRefresh);

	}

	public void Stop() {
		if (_localTimer != null) {
			_localTimer.cancel();

		}

	}

	public double Evaluate(String formula, String data, int bytes) {
		int dataIndex = 0;
		String stbyte = "";
		int byteValue = 0;
		for (int i = 1; i <= bytes; i++) {

			stbyte = data.substring(dataIndex, 2);
			byteValue = core.HexToInt(stbyte);
			dataIndex += 2;
			formula = formula.replace("{" + Integer.toString(i) + "}",
					Integer.toString(byteValue));
		}
		return eval.Evaluate(formula);

	}

	public void GetPidValue(ElmBankElement elem) {
		String result = "";
		result = core.GetPidResponse(elem.getPid(), elem.getNumbytes());
		// if is hex
		if (hex.matcher(result).matches()) {
			// evaluate and pass result to gui
			double evaluated = Evaluate(elem.getFormula(), result,
					elem.getNumbytes());
			if (!elem.getMode().equals("CALC")) {
				gui.SetPidValue(0, elem, evaluated);
			}

		} else {
			// is error so pass it to the error log
			gui.AddError(result);
		}
	}

	private void TimerMethod() {
		if(!connected)
			_localTimer.cancel();
		if ((_activity != null))
			_activity.runOnUiThread(Timer_Tick);
		else {
			if (_localTimer != null)
				_localTimer.cancel();
		}

	}

	private Runnable Timer_Tick = new Runnable() {
		public void run() {
			// gui
			// ++debugCounter;
			// gui.AddError(Integer.toString(debugCounter));
			if (_elements.size() > 0) {
				GetPidValue(_elements.get(0));
			}

		}
	};

	private void LoadXmlElements() {
		BaseParser parser = new BaseParser(_activity);
		try {
			_elements = parser.parse();
			Collections.sort(_elements);
			if (_elements.size() > 0) {
				ElmBankElement elem = _elements.get(0);
				_currentBankElement = elem.getId();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean CreatePort() {
		if (comPort == null) {
			comPort = new ConcreteCommPort();
			ConcreteCommPort cp = (ConcreteCommPort) comPort;
			cp.set_gui(gui);
		}
		comPort.SetPort(_port);
		if (!comPort.FoundBluetooth()) {
			gui.AddError("Bluetooth not found");
			return false;
		}
		return true;
	}

	private void CreateTimer() {
		if (timer == null)
			timer = new ConcreteTimer();

	}

	public int get_port() {
		return _port;
	}

	public void set_port(int _port) {
		this._port = _port;
		if (comPort != null)
			comPort.SetPort(this._port);
	}

	public int get_elmProto() {
		return _elmProto;
	}

	public void set_elmProto(int _elmProto) {
		this._elmProto = _elmProto;
	}

	public int get_bankLayout() {
		return _bankLayout;
	}

	public void set_bankLayout(int _bankLayout) {
		this._bankLayout = _bankLayout;
	}

	public Activity get_activity() {
		return _activity;
	}

	public void set_activity(Activity _activity) {
		this._activity = _activity;

	}
}
