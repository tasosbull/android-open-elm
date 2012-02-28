package com.android.openelm;



import java.util.List;

import com.android.openelm.interfaces.IGui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;


public class AndroidOpenElmActivity extends Activity implements IGui , OnClickListener {
	/*
	 * check preferences if exists selected device 
	 * check if device exists and if is paired
	 * if   
	 * 
	 * **/
	int port = 1;
	int elmProto = 0;
	int bankLayout = 4;
	ElmMaestro maestro = null;
	TextView text = null;
	TextView elm = null;
	Button button = null;
	int dialogSelection = -1;
	List<String> devices = null;

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

	}
	
    public void onClick(View v) {
    	if(v == button){
    		/*
    		maestro.Stop();
    		try {
    			if(maestro.Init())
    				maestro.Start();
    		} catch (Exception e) {
    			this.AddError(e.getMessage());
    		}
    		*/
    		test();
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

	private void ElmStart(){
		
		getPrefs();
		maestro = new ElmMaestro(this);
		maestro.set_activity(this);
		maestro.set_port(port);
		maestro.set_bankLayout(bankLayout);
		maestro.set_elmProto(elmProto);
		maestro.set_timerRefresh(1000);
		try {
			if(maestro.Init()){
				devices = maestro.AvailableBluetoothDevices();
				if(devices.size() <= 1){
					if(maestro.Connect(devices.get(0)))
						maestro.Start();
				}
				else{
					
				}
				   
				}
		} catch (Exception e) {
			this.AddError(e.getMessage());
		}
	}
	
	private void ElmStop(){
		if(maestro != null)
    		maestro.Stop();
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
	
	public int SelectSingleItemFromList(CharSequence[] availItems, String title ){
		dialogSelection = -1;
		final CharSequence[] items = availItems;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	dialog.dismiss();
		    	dialogSelection = item;
		    	
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
		return dialogSelection;
	}
	
	public void test(){
		CharSequence[] availItems = new CharSequence[2];
		availItems[0] = "aaa";
		availItems[1] = "bbb";
		
		int x = SelectSingleItemFromList(availItems, "select");
		x++;
		
		
	}

	public String GetSelectedDevice() {
		// TODO Auto-generated method stub
		return null;
	}

}