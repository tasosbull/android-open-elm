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
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParserException;
import com.android.openelm.interfaces.ICommPort;
import com.android.openelm.interfaces.IGui;
import android.app.Activity;

public class ElmMaestro {

	private int _port = 1;
	private int _elmProto = 0;
	private int _bankLayout = 4;
	private int _banks = 0;
	private int _currentBank;
	private Activity _activity;
	private List<ElmBankElement> _elements;
	private ElmBankElement[] _currentElements = null;
	private int _currentBankElement = -1;
	private long _timerRefresh = 200;
	private ExpressionEvaluator eval = null;
	private ICommPort comPort = null;
	private IGui gui = null;
	public ElmCore core = null;
	private Pattern hex = Pattern.compile("^[0-9A-F]+$");
	private Pattern calc = Pattern.compile("(?i)(\\{)(.+?)(\\})");

	public ElmMaestro(IGui _gui) {
		gui = _gui;
	}

	public boolean Init() throws Exception {
		if (_activity == null)
			throw new Exception("You must set activity first");
		eval = new ExpressionEvaluator(_activity);
		LoadXmlElements();
		InitBanks();
		if (!CreatePort())
			return false;
		core = new ElmCore(comPort, gui);
		return true;
	}

	public List<String> AvailableBluetoothDevices() {
		return comPort.GetNameDevices();
	}

	public boolean Connect(String aDevice) {
		boolean result = comPort.Connect(aDevice);
		if (result) {
			core.FastInit();
		}
		return result;
	}

	public void Disconnect() {
		comPort.Disconnect();
	}

	public boolean IsConnected() {
		return comPort.IsConnected();
	}

	public String GetCommandResult(String command) {
		return core.GetCommandResult(command);

	}

	public double Evaluate(String formula, String data, int bytes) {
		int dataIndex = 0;
		String stbyte = "";
		int byteValue = 0;
		for (int i = 1; i <= bytes; i++) {
			stbyte = data.substring(dataIndex, dataIndex + 2);
			byteValue = core.HexToInt(stbyte);
			dataIndex += 2;
			formula = formula.replace("{" + Integer.toString(i) + "}",
					Integer.toString(byteValue));
		}
		return eval.Evaluate(formula);
	}

	public void GetPidOriginalValue(ElmBankElement elem) {
		String result = "";
		result = core.GetPidResponse(elem.getPid(), elem.getNumbytes());
		if (hex.matcher(result).matches()) {
			double evaluated = Evaluate(elem.getFormula(), result,
					elem.getNumbytes());
			elem.currentValue = evaluated;
		} else {
			gui.AddError(result);
		}

	}

	private ElmBankElement GetElementById(int id) {
		for (int i = 0; i < _elements.size(); i++) {
			if (_elements.get(i).getId() == id)
				return _elements.get(i);
		}
		return null;
	}

	public void GetPidValue(ElmBankElement elem) {
		if (!elem.getMode().equals("CALC")) {
			GetPidOriginalValue(elem);

		} else {
			String formula = elem.getFormula();
			Matcher matcher = calc.matcher(formula);
			while (matcher.find()) {
				String original = matcher.group();
				String found = original.replace("{", "").replace("}", "");
				int pidFound = Integer.parseInt(found);
				ElmBankElement fElem = GetElementById(pidFound);
				if (fElem != null) {
					GetPidOriginalValue(fElem);
					formula = formula.replace(original,
							Double.toString(fElem.currentValue));
				}
				// find by id the element
			}
			double val = eval.Evaluate(formula);
			elem.currentValue = val;
		}
	}

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

	private void InitBanks() {
		_banks = _elements.size() / _bankLayout;
		if ((_elements.size() % _bankLayout) > 0)
			++_banks;
		if (_banks > 0)
			_currentBank = 1;
		SetCurrentElements(_currentBank);
	}

	private void SetCurrentElements(int bank) {
		_currentElements = new ElmBankElement[4];
		int startElement = 1;
		if (bank > 1)
			startElement = ((bank - 1) * _bankLayout) + 1;
		if (startElement <= _elements.size()) {
			for (int i = 0; i < 4; ++i) {
				if ((startElement + i) > _elements.size())
					break;
				_currentElements[i] = _elements.get((startElement - 1) + i);
			}
		} else {
			InitBanks();
			return;
		}
		gui.GetCurrentElements(_currentElements);

	}

	public void NextBank(boolean forward) {
		if (forward) {
			_currentBank = (_currentBank < _banks) ? _currentBank + 1 : 1;
		} else {
			_currentBank = (_currentBank <= 1) ? _banks : _currentBank - 1;
		}
		SetCurrentElements(_currentBank);

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

	public int GetNumOfDTC(boolean[] noError, boolean[] milIsOn){
		return core.GetNumOfDTC(noError, milIsOn);
	}
	
	public ArrayList<String> getDtcList(boolean[] noError){
		return core.getDtcList(noError);
	}
	
	public boolean ResetDtc(boolean[] noError) {
		core.ResetDtc(noError);
		return noError[0];
	}
}
