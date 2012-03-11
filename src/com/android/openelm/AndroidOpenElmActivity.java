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

import com.android.openelm.interfaces.IGui;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// getPrefs();
		/*
		 * setContentView(R.layout.main); TextView testXmlContent =
		 * (TextView)findViewById(R.id.elm); ExpressionEvaluator eval = new
		 * ExpressionEvaluator(this); double value = eval.Evaluate( "1+1");
		 * testXmlContent.setText(Double.toString(value));
		 */

		/*
		 * //test xml file String str = ""; BaseParser parser = new
		 * BaseParser(this); try { List<ElmBankElement> elements =
		 * parser.parse(); for(int i = 0; i < elements.size(); i++){
		 * ElmBankElement elem = elements.get(i); str = str + elem.toString() ;
		 * } testXmlContent.setText(str); } catch (XmlPullParserException e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		elm = (TextView) findViewById(R.id.elm);
		edittext = (EditText) findViewById(R.id.edittext);
		button = (Button) findViewById(R.id.button);
		button_write = (Button) findViewById(R.id.button_write);
		button_read = (Button) findViewById(R.id.button_read);
		button.setOnClickListener(this);
		InitMaestro();

	}

	public void onClick(View v) {
		if (v == button) {
			if(!connected)
				return;
			String command = edittext.getText().toString();
			String result = maestro.GetCommandResult(command);
			elm.setText(result);
		
			
		}
		else if(v == button_read){
			StringBuilder b = new StringBuilder();
			maestro.core.ReadPort(b);
			elm.setText(b.toString());
			
		}
		else if(v == button_write){
			maestro.core.SendCommand(elm.getText().toString());
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
		if(!connected)
			connected = maestro.Connect(deviceSelected);
		if (connected)
			maestro.Start();
	}

	private void ElmStop() {
		if (maestro != null) {
			maestro.Stop();
			maestro.Disconnect();
		}
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
			return true;
		case R.id.elm_connect:
			if(!connected)
				connected = maestro.Connect(deviceSelected);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void SetPidValue(int bankPosition, ElmBankElement elem, double value) {
		elm.setText(elem.getShortDescription() + "  " + Double.toString(value));
	}

	private void elmPreferences() {
		startActivity(new Intent(this, PreferencesFromXml.class));

	}

	public void AddError(String aError) {
		text.setText(aError);

	}

	public void ProcMessages() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 10; ++i)
			;

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

}