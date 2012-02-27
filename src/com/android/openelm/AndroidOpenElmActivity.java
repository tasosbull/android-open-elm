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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class AndroidOpenElmActivity extends Activity implements IGui , OnClickListener {

	int port = 1;
	int elmProto = 0;
	int bankLayout = 4;
	ElmMaestro maestro = null;
	TextView text = null;
	TextView elm = null;
	Button button = null;

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
		
		text = (TextView)findViewById(R.id.errors);
		button = (Button)findViewById(R.id.button);
		elm = (TextView)findViewById(R.id.elm);
		button.setOnClickListener(this);
		getPrefs();
		maestro = new ElmMaestro(this);
		maestro.set_activity(this);
		maestro.set_port(port);
		maestro.set_bankLayout(bankLayout);
		maestro.set_elmProto(elmProto);
		maestro.set_timerRefresh(1000);
		try {
			if(maestro.Init())
				maestro.Start();
		} catch (Exception e) {
			this.AddError(e.getMessage());
		}

	}
	
    public void onClick(View v) {
    	if(v == button){
    		maestro.Stop();
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
	
	public void SetPidValue(int bankPosition, ElmBankElement elem, double value){
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