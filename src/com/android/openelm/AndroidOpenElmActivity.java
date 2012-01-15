package com.android.openelm;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
public class AndroidOpenElmActivity extends Activity  {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView testXmlContent = (TextView)findViewById(R.id.elm);
        String str = "";
        BaseParser parser = new BaseParser(this);
        try {
			List<ElmBankElement> elements = parser.parse();
			for(int i = 0; i < elements.size(); i++){
				ElmBankElement elem = elements.get(i);
				str = str + elem.toString() ;
			}
			testXmlContent.setText(str);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	 
	 private void elmPreferences(){
		 startActivity(new Intent(this, PreferencesFromXml.class));
	 
	 }
}