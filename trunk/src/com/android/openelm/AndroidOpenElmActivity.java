package com.android.openelm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
public class AndroidOpenElmActivity extends Activity  {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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