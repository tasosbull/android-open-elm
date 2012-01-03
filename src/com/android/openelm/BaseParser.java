package com.android.openelm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

import com.android.openelm.interfaces.IParser;

public class BaseParser implements IParser{
	
	private Activity activity;
	
	private List<ElmBankElement> elements = null;
	
	static final String ID = "id";
	static final String ACTIVE = "active";
	static final String OBD_MODE = "obd_mode";
	static final String OBD_PID = "obd_pid";
	static final String OBD_BYTES = "obd_bytes";
	static final String SHORT_TEXT = "short_text";
	static final String DESCRIPTION_TEXT = "description_text";
	static final String MINVAL = "minval";
	static final String MAXVAL = "maxval";
	static final String WARNING = "warning";
	static final String ERROR = "error";
	static final String FORMULA = "formula";
	static final String GAUGE_ANALOG = "gauge_analog";
	static final String GAUGE_NOTCHES_TOTAL = "gauge_notches_total";
	static final String GAUGE_NOTCHES_INCREMENT_LARGE = "gauge_notches_increment_large";
	static final String GAUGE_NOTCHES_INCREMENT_SMALL = "gauge_notches_increment_small";
	static final String GAUGE_NOTCHES_DISPLAY_FACTOR = "gauge_notches_display_factor";
	static final String GAUGE_SCALE_MIN_CENTER_MAX = "gauge_scale_min_center_max";
	static final String GAUGE_VISIBLE_GAUGE_SCALE = "gauge_visible_gauge_scale";
	static final String GAUGE_COLOR_FACE_RGB = "gauge_color_face_rgb";
	static final String GAUGE_COLOR_SCALE_RGB = "gauge_color_scale_rgb";
	static final String GAUGE_COLOR_HAND_RGB = "gauge_color_hand_rgb";
	static final String GAUGE_COLOR_TITLES_RGB = "gauge_color_titles_rgb";
	static final String GAUGE_TITLE_LOWER = "gauge_title_lower";
	static final String GAUGE_TITLE_UPPER = "gauge_title_upper";
	static final String GAUGE_TITLE_UNIT = "gauge_title_unit";



    protected BaseParser(Activity _activity){
    	activity = _activity;
    }
    public List<ElmBankElement> parse() throws XmlPullParserException, IOException {
    	elements = new ArrayList<ElmBankElement>();
    	Resources res = activity.getResources();
    	XmlResourceParser xpp = res.getXml(R.xml.elm);
    	xpp.next();
    	int eventType = xpp.getEventType();
    	while (eventType != XmlPullParser.END_DOCUMENT)
    	{
    		if(eventType == XmlPullParser.START_DOCUMENT)
    		{
    		}
    		else if(eventType == XmlPullParser.START_TAG)
    		{
    		}
    		else if(eventType == XmlPullParser.END_TAG)
    		{
    		}
    		else if(eventType == XmlPullParser.TEXT)
    		{
    		}
    		eventType = xpp.next();
    	}
    	// parse first
    	return elements;
    	
    }
	
}
