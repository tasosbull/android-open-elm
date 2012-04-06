package com.android.openelm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.android.openelm.interfaces.IDtcParser;


public class DtcParser implements IDtcParser  {
	
	private Activity activity;
	private List<ElmDtcElement> elements = null;

	static final String DESCR = "descr";
	static final String ID = "id";
	static final String ELEM = "elem";
	static final String DTC = "dtc";


	public List<ElmDtcElement> parse() throws XmlPullParserException,
			IOException {
		elements = new ArrayList<ElmDtcElement>();
		ElmDtcElement current = null;
		String currentTag = "";
		Resources res = activity.getResources();
		XmlResourceParser xpp = res.getXml(R.xml.dtc);
		xpp.next();
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_DOCUMENT) {
			} 
			else if (eventType == XmlPullParser.START_TAG) {
				if (xpp.getName().toLowerCase().equals(ELEM)) {
					current = new ElmDtcElement();
				}
				currentTag = xpp.getName().toLowerCase(); 
			}
			else if (eventType == XmlPullParser.END_TAG) {
				if (xpp.getName().toLowerCase().equals(ELEM)) {
					elements.add(current);
					currentTag = "";
				}
			} 
			else if (eventType == XmlPullParser.TEXT) {
				if(currentTag.equals(ID)){
					current.setId(xpp.getText());
				}
				else if(currentTag.equals(DESCR)){
					current.setDescr(xpp.getText());
				}
			}
			eventType = xpp.next();
		}
		return elements;
	}

}
