package com.android.openelm;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

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
	ExpressionEvaluator eval = null ;
	ICommPort comPort = null;
	ITimer timer = null;
	IGui gui = null;
	ElmCore core = null;
	
	public ElmMaestro(IGui _gui){
		gui = _gui;
	}
	
	public void Init() throws Exception{
		if(_activity == null)
				throw new Exception("You must set activity first");
		eval = new ExpressionEvaluator(_activity);
		LoadXmlElements();
		CreatePort();
		core = new ElmCore(comPort, timer, gui);
		
	}

	private void LoadXmlElements() {
		BaseParser parser = new BaseParser(_activity);
		try {
			_elements = parser.parse();
			Collections.sort(_elements); 
			if(_elements.size() > 0 ){
				ElmBankElement elem = _elements.get(0);
				_currentBankElement = elem.getId();
						}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void CreatePort(){
		if(comPort == null)
			comPort = new ConcreteCommPort();
		comPort.SetPort(_port);
	}
	
	private void CreateTimer(){
		if(timer == null)
		timer = new ConcreteTimer();
	}

	public int get_port() {
		return _port;
	}

	public void set_port(int _port) {
		this._port = _port;
		if(comPort != null)
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
