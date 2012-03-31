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

import java.util.List;

import com.android.openelm.gui.LightGauge;
import com.android.openelm.interfaces.IGui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AndroidOpenElmActivity extends Activity implements IGui,
		OnClickListener {
	/*
	 * add initialize menu button add select device menu button
	 * 
	 * *
	 */
	int port = 1;
	int elmProto = 0;
	int bankLayout = 4;
	ElmMaestro maestro = null;
	EditText edittext = null;
	TextView text = null;
	TextView elm = null;
	Button button = null;
	Button button_write = null;
	Button button_read = null;
	String deviceSelected = "";
	List<String> devices = null;
	boolean connected = false;
	boolean initialized = false;
	ElmBankElement[] currentElements;
	int buttonSelectedColor = Color.GREEN;
	int sensorTextSize = 12;

	int wOffset = 100; // todo application param
	int hOffset = 100;

	LightGauge gauge = null;
	ElmBankElement gaugeElement = null;
	double gaugeValueFactor = 1;
	Button sensor1 = null;
	Button sensor2 = null;
	Button sensor3 = null;
	Button sensor4 = null;
	Button bankPrev = null;
	Button bankNext = null;
	RelativeLayout rl = null;
	int idbg = 0;
	boolean elmStarted = false;
	int refreshMs = 500; // TODO add to preferences

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InitSensorUI();
		InitMaestro();
		updateUI();
		/*
		 * setContentView(R.layout.main); elm = (TextView)
		 * findViewById(R.id.elm); edittext = (EditText)
		 * findViewById(R.id.edittext); button = (Button)
		 * findViewById(R.id.button); button_write = (Button)
		 * findViewById(R.id.button_write); button_read = (Button)
		 * findViewById(R.id.button_read); button.setOnClickListener(this);
		 * button_read.setOnClickListener(this);
		 * button_write.setOnClickListener(this); InitMaestro();
		 */
	}

	private void InitSensorUI() {

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		setContentView(R.layout.main);
		rl = (RelativeLayout) findViewById(R.id.relative);
		int h = metrics.heightPixels - hOffset;
		int w = metrics.widthPixels - wOffset;

		RelativeLayout.LayoutParams params;
		int buttonHeight = (h - w) / 2;
		gauge = new LightGauge(this);
		params = new RelativeLayout.LayoutParams(w, w);
		params.leftMargin = wOffset / 2;
		params.topMargin = 0;
		rl.addView(gauge, params);

		sensor1 = new Button(this);
		sensor1.setOnClickListener(this);
		sensor1.setTextSize(sensorTextSize);
		sensor1.setText(new StringBuffer("test 12"));
		sensor1.setBackgroundColor(Color.WHITE);
		sensor1.setTextColor(Color.BLACK);
		params = new RelativeLayout.LayoutParams(w / 4, buttonHeight);
		params.leftMargin = wOffset / 2;
		params.topMargin = h - (buttonHeight * 2);
		rl.addView(sensor1, params);

		sensor2 = new Button(this);
		sensor2.setOnClickListener(this);
		sensor2.setTextSize(sensorTextSize);
		sensor2.setText(new StringBuffer("test 12"));
		sensor2.setBackgroundColor(Color.WHITE);
		sensor2.setTextColor(Color.BLACK);
		params = new RelativeLayout.LayoutParams(w / 4, buttonHeight);
		params.leftMargin = (w / 4) + (wOffset / 2);
		params.topMargin = h - (buttonHeight * 2);
		rl.addView(sensor2, params);

		sensor3 = new Button(this);
		sensor3.setOnClickListener(this);
		sensor3.setTextSize(sensorTextSize);
		sensor3.setBackgroundColor(Color.WHITE);
		sensor3.setTextColor(Color.BLACK);

		params = new RelativeLayout.LayoutParams(w / 4, buttonHeight);
		params.leftMargin = ((w / 4) * 2) + (wOffset / 2);
		params.topMargin = h - (buttonHeight * 2);
		rl.addView(sensor3, params);

		sensor4 = new Button(this);
		sensor4.setOnClickListener(this);
		sensor4.setTextSize(sensorTextSize);
		sensor4.setBackgroundColor(Color.WHITE);
		sensor4.setTextColor(Color.BLACK);

		params = new RelativeLayout.LayoutParams(w / 4, buttonHeight);
		params.leftMargin = ((w / 4) * 3) + (wOffset / 2);
		params.topMargin = h - (buttonHeight * 2);
		rl.addView(sensor4, params);

		bankPrev = new Button(this);
		bankPrev.setOnClickListener(this);
		bankPrev.setText("Bank prev");
		params = new RelativeLayout.LayoutParams(w / 2, buttonHeight);
		params.leftMargin = (wOffset / 2);
		params.topMargin = h - buttonHeight;
		rl.addView(bankPrev, params);

		bankNext = new Button(this);
		bankNext.setOnClickListener(this);
		bankNext.setText("Bank next");
		params = new RelativeLayout.LayoutParams(w / 2, buttonHeight);
		params.leftMargin = (w / 2) + (wOffset / 2);
		params.topMargin = h - buttonHeight;
		rl.addView(bankNext, params);

		text = new TextView(this);
		text.setText("****Android Open Elm****");
		params = new RelativeLayout.LayoutParams(w, (hOffset / 2));
		params.leftMargin = wOffset / 2;
		params.topMargin = h;
		rl.addView(text, params);

	}

	public void onClick(View v) {
		if (v == sensor1) {
			SetSelectedElement(0);
		} else if (v == sensor2) {
			SetSelectedElement(1);
		} else if (v == sensor3) {
			SetSelectedElement(2);
		} else if (v == sensor4) {
			SetSelectedElement(3);
		} else if (v == bankPrev) {
			maestro.NextBank(false);
		} else if (v == bankNext) {
			maestro.NextBank(true);
		}

		else if (v == button_read) {
			if (!connected)
				return;
			StringBuilder b = new StringBuilder();
			maestro.core.ReadPort(b);
			elm.setText(b.toString());

		} else if (v == button_write) {
			if (!connected)
				return;
			maestro.core.SendCommand(edittext.getText().toString());
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		getPrefs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	private void InitMaestro() {
		getPrefs();
		maestro = new ElmMaestro(this);
		maestro.set_activity(this);
		maestro.set_port(port);
		maestro.set_bankLayout(bankLayout);
		maestro.set_elmProto(elmProto);
		maestro.set_timerRefresh(5);
		try {
			initialized = maestro.Init();
			if (!initialized)
				return;
			boolean bFound = false;
			devices = maestro.AvailableBluetoothDevices();
			if ((devices == null) || (devices.size() == 0))
				return;
			CharSequence[] availItems = new CharSequence[devices.size()];
			for (int i = 0; i < devices.size(); i++) {
				availItems[i] = devices.get(i);
				if (devices.get(i).equals(deviceSelected)) {
					bFound = true;
				}
			}
			if ((deviceSelected == "") || (!bFound)) {
				SelectSingleItemFromList(availItems, "Select device");
			}
		} catch (Exception e) {

			this.AddError(e.getMessage());
		}
	}

	private void ElmStart() {
		elmStarted = true;
	}

	private void ElmStop() {
		elmStarted = false;
	}

	private void SelectDevice() {
		CharSequence[] availItems = new CharSequence[devices.size()];
		for (int i = 0; i < devices.size(); i++)
			availItems[i] = devices.get(i);
		SelectSingleItemFromList(availItems, "Select device");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.elm_preferences:
			elmPreferences();
			return true;
		case R.id.elm_start:
			ElmStart();
			return true;
		case R.id.elm_stop:
			ElmStop();
			return true;
		case R.id.elm_selectdevice:
			SelectDevice();
			return true;
		case R.id.elm_connect:
			maestro.Connect(deviceSelected);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void updateUI() {
		try {
			ClearSensorText();
			for (int i = 0; i < 4; i++) {
				mRedrawHandler.sleep(refreshMs / 4);
				Button button = null;
				switch (i) {
				case 0:
					button = sensor1;
					break;
				case 1:
					button = sensor2;
					break;
				case 2:
					button = sensor3;
					break;
				case 3:
					button = sensor4;
					break;
				}
				if (currentElements[i] != null) {
					if (elmStarted) {
						maestro.GetPidValue(currentElements[i]);
						if (currentElements[i] == gaugeElement)
							gauge.setValue((float) (currentElements[i].currentValue * gaugeValueFactor));
						button.setText(currentElements[i].getShortDescription()
								+ " "
								+ Double.toString(currentElements[i].currentValue));
					}
				} else {
					button.setText("");
				}

			}
		} catch (Exception ex) {
			AddError(ex.getMessage());
		}

	}

	private void elmPreferences() {
		startActivity(new Intent(this, PreferencesFromXml.class));
	}

	public void AddError(String aError) {
		text.setText(aError);
	}

	public void ProcMessages() {
	}

	public int GetPortSelected() {
		return port;
	}

	public int GetElmProtoSelected() {
		return elmProto;
	}

	public int GetBankLayoutElementsSelected() {
		return bankLayout;

	}

	private void getPrefs() {
		Context ctx = this.getBaseContext();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		port = Integer.parseInt(prefs.getString("list_preference_port", "1"));
		elmProto = Integer.parseInt(prefs.getString("list_preference_proto",
				"0"));
		String bankStr = prefs.getString("list_preference_bank", "4");
		bankLayout = Integer.parseInt(bankStr);
		deviceSelected = prefs.getString("device_selected", "");
	}

	public void SetDevice(String item) {
		Context ctx = this.getBaseContext();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("device_selected", item);
		editor.commit();
	}

	public void SelectSingleItemFromList(CharSequence[] availItems, String title) {
		final CharSequence[] items = availItems;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				dialog.dismiss();
				SetDevice(items[item].toString());
				deviceSelected = items[item].toString();

			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	public String GetSelectedDevice() {
		// TODO Auto-generated method stub
		return null;
	}

	public void ClearSensorText() {

		sensor1.setText("");
		sensor2.setText("");
		sensor3.setText("");
		sensor4.setText("");
	}

	private void CreateGauge(int idx) {
		// gauge = new LightGauge(this);
		ElmBankElement elem = currentElements[idx];
		GaugeSchema gs = elem.getGauge();
		gaugeValueFactor = elem.getValuefactor();
		int totalNotches = gs.getTotalNotches();
		int incrementPerLargeNotch = gs.getIncrementLarge();
		int incrementPerSmallNotch = gs.getIncrementSmall();
		int notcheDisplayFactor = gs.getFactor();
		gauge.setNotches(totalNotches, incrementPerLargeNotch,
				incrementPerSmallNotch, notcheDisplayFactor);
		int scaleMinValue = (int) (gs.getScaleMin());
		int scaleCenterValue = (int) (gs.getScaleCenter());
		int scaleMaxValue = (int) (gs.getScaleMax());
		gauge.setScale(scaleMinValue, scaleCenterValue, scaleMaxValue);
		int r = gs.getFaceRed();
		int g = gs.getFaceGreen();
		int b = gs.getFaceBlue();
		gauge.setFaceColor(r, g, b);
		r = gs.getScaleRed();
		g = gs.getScaleGreen();
		b = gs.getScaleBlue();
		gauge.setScaleColor(r, g, b);
		r = gs.getHandRed();
		g = gs.getHandGreen();
		b = gs.getHandBlue();
		gauge.setHandColor(r, g, b);
		float min = (float) (elem.getMinval() * gaugeValueFactor);
		float warn = (float) (elem.getWarning() * gaugeValueFactor);
		float err = (float) (elem.getError() * gaugeValueFactor);
		float max = (float) (elem.getMaxval() * gaugeValueFactor);
		gauge.setRangeOkValues(min, warn);
		gauge.setRangeWarningValues(warn, err);
		gauge.setRangeErrorValues(err, max);
		gauge.setShowRange(true);
		gauge.setShowGauge(true);
		String lowerTitle = gs.getTitleLower();
		String upperTitle = gs.getTitleUpper();
		String unitTitle = gs.getTitleUnit();
		gauge.setTitles(lowerTitle, upperTitle, unitTitle);
		r = gs.getTitlesRed();
		g = gs.getTitlesGreen();
		b = gs.getTitlesBlue();
		gauge.setTitlesColor(r, g, b);
		gauge.init();
		gauge.regenerateBackground();
		gauge.invalidate();
		gauge.setValue((float) elem.getMinval());

	}

	private void SetSelectedElement(int idx) {
		if (currentElements[idx] == null)
			return;
		sensor1.setClickable(true);
		sensor2.setClickable(true);
		sensor3.setClickable(true);
		sensor4.setClickable(true);
		sensor1.setBackgroundColor(Color.WHITE);
		sensor2.setBackgroundColor(Color.WHITE);
		sensor3.setBackgroundColor(Color.WHITE);
		sensor4.setBackgroundColor(Color.WHITE);
		sensor1.setTextColor(Color.BLACK);
		sensor2.setTextColor(Color.BLACK);
		sensor3.setTextColor(Color.BLACK);
		sensor4.setTextColor(Color.BLACK);
		Button button = null;
		switch (idx) {
		case 0:
			button = sensor1;
			break;
		case 1:
			button = sensor2;
			break;
		case 2:
			button = sensor3;
			break;
		case 3:
			button = sensor4;
			break;
		}
		button.setClickable(false);
		button.setBackgroundColor(Color.GREEN);
		button.setTextColor(Color.WHITE);
		CreateGauge(idx);
		gaugeElement = currentElements[idx];
	}

	public void GetCurrentElements(ElmBankElement[] _currentElements) {
		currentElements = _currentElements;
		ClearSensorText();
		if (currentElements[0] != null) {
			SetSelectedElement(0);
		}

	}

	private RefreshHandler mRedrawHandler = new RefreshHandler();

	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			AndroidOpenElmActivity.this.updateUI();
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

}