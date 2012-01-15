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

public class BaseParser implements IParser {

	private Activity activity;

	private List<ElmBankElement> elements = null;

	static final String ITEM = "item";
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

	protected BaseParser(Activity _activity) {
		activity = _activity;
	}
	//TODO exception handling 
	public List<ElmBankElement> parse() throws XmlPullParserException,
			IOException {
		elements = new ArrayList<ElmBankElement>();
		ElmBankElement current = null;
		String currentTag = "";
		Resources res = activity.getResources();
		XmlResourceParser xpp = res.getXml(R.xml.elm);
		xpp.next();
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_DOCUMENT) {
			} 
			else if (eventType == XmlPullParser.START_TAG) {
				//if tag==item create a new item
				if (xpp.getName().toLowerCase().equals(ITEM)) {
					current = new ElmBankElement();
					current.setGauge(new GaugeSchema());
					
				}
				currentTag = xpp.getName().toLowerCase(); 
			}
			else if (eventType == XmlPullParser.END_TAG) {
				//if tag==item insert item to list	
				if (xpp.getName().toLowerCase().equals(ITEM)) {
					elements.add(current);
					currentTag = "";
				}

			} 
			else if (eventType == XmlPullParser.TEXT) {
				if(currentTag.equals(ID)){
					current.setId(Integer.parseInt(xpp.getText()));
				}
				/* 
				else if(currentTag.equals()){
					
				}
				 */
				else if(currentTag.equals(ACTIVE)){
					boolean b = Integer.parseInt(xpp.getText()) != 0;
					current.setActive(b);
				}
				else if(currentTag.equals(OBD_MODE)){
					current.setMode(xpp.getText());
				}
				else if(currentTag.equals(OBD_PID)){
					current.setPid(xpp.getText());
				}
				else if(currentTag.equals(OBD_BYTES)){
					current.setNumbytes(Integer.parseInt(xpp.getText()));
				}
				else if(currentTag.equals(SHORT_TEXT)){
					current.setShortDescription(xpp.getText());
				}
				else if(currentTag.equals(DESCRIPTION_TEXT)){
					current.setDescription(xpp.getText());
				}
				else if(currentTag.equals(MINVAL)){
					current.setMinval(Double.parseDouble(xpp.getText()));
				}
				else if(currentTag.equals(MAXVAL)){
					current.setMaxval(Double.parseDouble(xpp.getText()));
				}
				else if(currentTag.equals(WARNING)){
					current.setWarning(Double.parseDouble(xpp.getText()));
				}
				else if(currentTag.equals(ERROR)){
					current.setError(Double.parseDouble(xpp.getText()));
				}
				else if(currentTag.equals(FORMULA)){
					current.setFormula(xpp.getText());
				}
				else if(currentTag.equals(GAUGE_ANALOG)){
					boolean b = Integer.parseInt(xpp.getText()) != 0;
					current.getGauge().setAnalog(b);
				}
				else if(currentTag.equals(GAUGE_NOTCHES_TOTAL)){
					current.getGauge().setTotalNotches(Integer.parseInt(xpp.getText()));
				}
				else if(currentTag.equals(GAUGE_NOTCHES_INCREMENT_LARGE)){
					current.getGauge().setIncrementLarge(Integer.parseInt(xpp.getText()));
				}
				else if(currentTag.equals(GAUGE_NOTCHES_INCREMENT_SMALL)){
					current.getGauge().setIncrementSmall(Integer.parseInt(xpp.getText()));
				}
				else if(currentTag.equals(GAUGE_NOTCHES_DISPLAY_FACTOR)){
					current.getGauge().setFactor(Integer.parseInt(xpp.getText()));
				}
				else if(currentTag.equals(GAUGE_SCALE_MIN_CENTER_MAX)){
					String text = xpp.getText();
					String [] split = text.split(";");
					if(split.length == 3){
						current.getGauge().setScaleMin(Integer.parseInt(split[0]));
						current.getGauge().setScaleCenter(Integer.parseInt(split[1]));
						current.getGauge().setScaleMax(Integer.parseInt(split[2]));
					}
				}
				else if(currentTag.equals(GAUGE_VISIBLE_GAUGE_SCALE)){
					String text = xpp.getText();
					String [] split = text.split(";");
					if(split.length == 2){
						boolean b1 = Integer.parseInt(split[0]) != 0;
						boolean b2 = Integer.parseInt(split[1]) != 0;
						current.getGauge().setGaugeVisible(b1);
						current.getGauge().setRangeVisible(b2);
					}
				}
				else if(currentTag.equals(GAUGE_COLOR_FACE_RGB)){
					String text = xpp.getText();
					String [] split = text.split(";");
					if(split.length == 3){
						current.getGauge().setFaceRed(Integer.parseInt(split[0]));
						current.getGauge().setFaceGreen(Integer.parseInt(split[1]));
						current.getGauge().setFaceBlue(Integer.parseInt(split[2]));
					}
				}
				else if(currentTag.equals(GAUGE_COLOR_SCALE_RGB)){
					String text = xpp.getText();
					String [] split = text.split(";");
					if(split.length == 3){
						current.getGauge().setScaleRed(Integer.parseInt(split[0]));
						current.getGauge().setScaleGreen(Integer.parseInt(split[1]));
						current.getGauge().setScaleBlue(Integer.parseInt(split[2]));
					}
				}
				else if(currentTag.equals(GAUGE_COLOR_HAND_RGB)){
					String text = xpp.getText();
					String [] split = text.split(";");
					if(split.length == 3){
						current.getGauge().setHandRed(Integer.parseInt(split[0]));
						current.getGauge().setHandGreen(Integer.parseInt(split[1]));
						current.getGauge().setHandBlue(Integer.parseInt(split[2]));
					}
				}
				else if(currentTag.equals(GAUGE_COLOR_TITLES_RGB)){
					String text = xpp.getText();
					String [] split = text.split(";");
					if(split.length == 3){
						current.getGauge().setTitlesRed(Integer.parseInt(split[0]));
						current.getGauge().setTitlesGreen(Integer.parseInt(split[1]));
						current.getGauge().setTitlesBlue(Integer.parseInt(split[2]));
					}
				}
				else if(currentTag.equals(GAUGE_TITLE_LOWER)){
					current.getGauge().setTitleLower(xpp.getText());
				}
				else if(currentTag.equals(GAUGE_TITLE_UPPER)){
					current.getGauge().setTitleUpper(xpp.getText());
				}
				else if(currentTag.equals(GAUGE_TITLE_UNIT)){
					current.getGauge().setTitleUnit(xpp.getText());
				}

			
			}
			eventType = xpp.next();
		}
		// parse first
		return elements;

	}

}
