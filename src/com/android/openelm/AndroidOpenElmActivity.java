package com.android.openelm;



import com.android.openelm.interfaces.IGui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class AndroidOpenElmActivity extends Activity implements IGui {

	int port = 1;
	int elmProto = 0;
	int bankLayout = 4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//getPrefs();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.elm_preferences:
			elmPreferences();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void elmPreferences() {
		startActivity(new Intent(this, PreferencesFromXml.class));

	}

	public void AddError(String aError) {
		// TODO Auto-generated method stub

	}

	public void ProcMessages() {
		// TODO Auto-generated method stub

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
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(  ctx);
		port = Integer.parseInt(prefs.getString("list_preference_port", "1"));
		elmProto = Integer.parseInt(prefs.getString("list_preference_proto", "0"));
		String bankStr = prefs.getString("list_preference_bank", "4");
		bankLayout = Integer.parseInt(bankStr);
	}

}