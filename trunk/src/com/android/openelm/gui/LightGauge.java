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

package com.android.openelm.gui;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class LightGauge extends View {
	private static final String TAG = LightGauge.class.getSimpleName();
	private static final float rimSize = 0.02f;
	private Bitmap background; 
	private Paint backgroundPaint; 
	private RectF rimRect;
	private Paint rimPaint;
	private RectF faceRect;
	private Paint facePaint;
	private Paint rimShadowPaint;
	private Paint rimCirclePaint;
	private Paint scalePaint;
	private RectF scaleRect;
	private int totalNotches = 0; 
	private int incrementPerLargeNotch = 0;
	private int incrementPerSmallNotch = 0;
	private int notcheDisplayFactor = 1;
	
	private int titlesColor = Color.rgb(255, 250, 250) ;
	private int scaleColor = Color.rgb(255, 250, 250) ;
	private int handColor = Color.rgb(255, 0, 0);
	private int faceColor = Color.rgb(32,178, 170);

	private int scaleMinValue = 0;
	private int scaleCenterValue = 0; 
	private int scaleMaxValue = 0;

	private  float degreesPerNotch = 360.0f/totalNotches;	
	private static final float scalePosition = 0.12f; 
	private static final float titlePosition = 0.145f;
	private static final float unitPosition  = 0.300f;

	private String lowerTitle = "";
	private String upperTitle = "";
	private String unitTitle = "";

	private Paint lowerTitlePaint;	
	private Paint upperTitlePaint;	
	private Path  lowerTitlePath;
	private Path  upperTitlePath;
	private RectF titleRect;
	private Paint unitPaint;	
	private Path  unitPath;
	private RectF unitRect;
	
	private Paint handPaint;
	private Path handPath;
	private Paint handScrewPaint;
	
	private float currentValue = 0;
	
	private int rangeOkColor = 0x9f00ff00;
	private float rangeOkMinValue = 0;
	private float rangeOkMaxValue = 0;
	
	private int rangeWarningColor = 0x9fff8800;
	private float rangeWarningMinValue = 0;
	private float rangeWarningMaxValue = 0;

	private int rangeErrorColor = 0x9fff0000;
	private float rangeErrorMinValue = 0;
	private float rangeErrorMaxValue = 0;
	
	private RectF valueRect;
	private RectF rangeRect;
	
	private Paint rangeOkPaint;
	private Paint rangeWarningPaint;
	private Paint rangeErrorPaint;
	private Paint rangeAllPaint;
	
	private Paint valueOkPaint;
	private Paint valueWarningPaint;
	private Paint valueErrorPaint;
	private Paint valueAllPaint;
	
	private static final float valuePosition = 0.285f; // The distance from the rim to the ranges
	private static final float rangePosition = 0.122f; // The distance from the rim to the ranges
	private float degreeMinValue             = 0;
	private float degreeMaxValue             = 0;
	private float degreeOkMinValue           = 0;
	private float degreeOkMaxValue           = 0;
	private float degreeWarningMinValue      = 0;
	private float degreeWarningMaxValue      = 0;
	private float degreeErrorMinValue        = 0;
	private float degreeErrorMaxValue        = 0;

	private boolean showRange = false;
	private boolean showGauge = false;
	private static  int centerDegrees   =  -90; // the one in the top center (12 o'clock), this corresponds with -90 degrees
	private float rangeStroke = 0.012f;
	private float valueStroke = 0.25f;
	
	public LightGauge(Context context) {
		super(context);
		init();
	}

	public LightGauge(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public LightGauge(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public void setNotches(	int totalNotches, int incrementPerLargeNotch, 
							int incrementPerSmallNotch, int notcheDisplayFactor){
		this.totalNotches = totalNotches;
		this.incrementPerLargeNotch = incrementPerLargeNotch;
		this.incrementPerSmallNotch = incrementPerSmallNotch;
		this.notcheDisplayFactor = notcheDisplayFactor;
		this.degreesPerNotch = 360.0f / this.totalNotches;	
	}
	
	public void setScale(int scaleMinValue, int scaleCenterValue, int scaleMaxValue){
		this.scaleMinValue = scaleMinValue;
		this.scaleCenterValue = scaleCenterValue;
		this.scaleMaxValue = scaleMaxValue;
		
	}
	
	public void setShowRange(boolean show){
		showRange = show;
	}
	public void setShowGauge(boolean show){
		showGauge = show;
	}
	
	public void setRangeOkValues(float min, float max){
		rangeOkMinValue = min;
		rangeOkMaxValue = max;
	}
	public void setRangeWarningValues(float min, float max){
		rangeWarningMinValue = min;
		rangeWarningMaxValue = max;
	}
	public void setRangeErrorValues(float min, float max){
		rangeErrorMinValue = min;
		rangeErrorMaxValue = max;
	}
	
	public void setTitles(String lowerTitle, String upperTitle, String unitTitle){
		this.lowerTitle = lowerTitle;
		this.upperTitle = upperTitle;
		this.unitTitle = unitTitle;
	}

	public void setFaceColor(int r, int g, int b){
		faceColor = Color.rgb(r, g, b);		
	}

	public void setScaleColor(int r, int g, int b){
		scaleColor = Color.rgb(r, g, b);		
	}
	
	public void setHandColor(int r, int g, int b){
		handColor = Color.rgb(r, g, b);		
	}
	
	public void setTitlesColor(int r, int g, int b){
		titlesColor = Color.rgb(r, g, b);		
	}

	public void setRangeOkColor(int r, int g, int b){
		rangeOkColor = Color.rgb(r, g, b);		
	}
	
	public void setRangeWarningColor(int r, int g, int b){
		rangeWarningColor = Color.rgb(r, g, b);		
	}
	public void setRangeErrorColor(int r, int g, int b){
		rangeErrorColor = Color.rgb(r, g, b);		
	}
	
	private void initBackground(){
		backgroundPaint = new Paint();
		backgroundPaint.setFilterBitmap(true);
	}
	
	private void initRim(){
		rimRect = new RectF(0.0f, 0.0f, 1.0f, 1.0f);

		faceRect = new RectF();
		faceRect.set(rimRect.left  + rimSize, rimRect.top    + rimSize, 
			         rimRect.right - rimSize, rimRect.bottom - rimSize);	

		rimPaint = new Paint();
		rimPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		rimPaint.setShader(new LinearGradient(0.40f, 0.0f, 0.60f, 1.0f, 
										   Color.rgb(0xf0, 0xf5, 0xf0),
										   Color.rgb(0x30, 0x31, 0x30),
										   Shader.TileMode.CLAMP));
		
		facePaint = new Paint();
		facePaint.setFilterBitmap(true);
		facePaint.setStyle(Paint.Style.FILL);
		facePaint.setColor(faceColor);
		
		rimCirclePaint = new Paint();
		rimCirclePaint.setAntiAlias(true);
		rimCirclePaint.setStyle(Paint.Style.STROKE);
		rimCirclePaint.setColor(Color.argb(0x4f, 0x33, 0x36, 0x33));
		rimCirclePaint.setStrokeWidth(0.005f);

		

		rimShadowPaint = new Paint();
		rimShadowPaint.setShader(new RadialGradient(0.5f, 0.5f, faceRect.width() / 2.0f, 
				                 new int[] { 0x00000000, 0x00000500, 0x50000500 },
				                 new float[] { 0.96f, 0.96f, 0.99f },
				                 Shader.TileMode.MIRROR));
		rimShadowPaint.setStyle(Paint.Style.FILL);
		
	}
	
	private void initScale(){
		
		scaleRect = new RectF();
		scaleRect.set(faceRect.left + scalePosition, faceRect.top + scalePosition,
					  faceRect.right - scalePosition, faceRect.bottom - scalePosition);
		scalePaint = new Paint();
		scalePaint.setStyle(Paint.Style.STROKE);
		scalePaint.setColor(scaleColor);
		scalePaint.setStrokeWidth(0.005f);
		scalePaint.setAntiAlias(true);
		scalePaint.setTextSize(0.070f);
		scalePaint.setTypeface(Typeface.SANS_SERIF);
		scalePaint.setTextScaleX(0.8f);
		scalePaint.setTextAlign(Paint.Align.CENTER);
	}
	
	private void initTitles(){
		
		titleRect = new RectF();
		titleRect.set(faceRect.left  + titlePosition, faceRect.top    + titlePosition,
					  faceRect.right - titlePosition, faceRect.bottom - titlePosition);

		unitRect = new RectF();
		unitRect.set(faceRect.left  + unitPosition, faceRect.top    + unitPosition,
					 faceRect.right - unitPosition, faceRect.bottom - unitPosition);
		unitPaint = new Paint();
		unitPaint.setColor(titlesColor);
		unitPaint.setAntiAlias(true);
		unitPaint.setTypeface(Typeface.DEFAULT_BOLD);
		unitPaint.setTextAlign(Paint.Align.CENTER);
		unitPaint.setTextSize(0.10f);
		unitPaint.setTextScaleX(0.8f);

		upperTitlePaint = new Paint();
		upperTitlePaint.setColor(titlesColor);
		upperTitlePaint.setAntiAlias(true);
		upperTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
		upperTitlePaint.setTextAlign(Paint.Align.CENTER);
		upperTitlePaint.setTextSize(0.04f);
		upperTitlePaint.setTextScaleX(0.8f);

		lowerTitlePaint = new Paint();
		lowerTitlePaint.setColor(titlesColor);
		lowerTitlePaint.setAntiAlias(true);
		lowerTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
		lowerTitlePaint.setTextAlign(Paint.Align.CENTER);
		lowerTitlePaint.setTextSize(0.04f);
		lowerTitlePaint.setTextScaleX(0.8f);
		
		unitPath = new Path();
		unitPath.addArc(unitRect, 180.0f, 180.0f);

		upperTitlePath = new Path();
		upperTitlePath.addArc(titleRect, 180.0f, 180.0f);

		lowerTitlePath = new Path();
		lowerTitlePath.addArc(titleRect, -180.0f, -180.0f);
		
	}
	
	
	
	private void initHand(){
		handPaint = new Paint();
		handPaint.setAntiAlias(true);
		handPaint.setColor(handColor);		
		handPaint.setShadowLayer(0.01f, -0.005f, -0.005f, 0x7f000000);
		handPaint.setStyle(Paint.Style.FILL);	

		handScrewPaint = new Paint();
		handScrewPaint.setAntiAlias(true);
		handScrewPaint.setColor(Color.rgb(255, 255, 0));
		handScrewPaint.setStyle(Paint.Style.FILL);
		
		handPath = new Path();                                              //   X      Y
		handPath.moveTo(0.5f, 0.5f + 0.2f);                                 // 0.500, 0.700
		handPath.lineTo(0.5f - 0.010f, 0.5f + 0.2f - 0.007f);               // 0.490, 0.630
		handPath.lineTo(0.5f - 0.002f, 0.5f - 0.40f);                       // 0.498, 0.100
		handPath.lineTo(0.5f + 0.002f, 0.5f - 0.40f);                       // 0.502, 0.100
		handPath.lineTo(0.5f + 0.010f, 0.5f + 0.2f - 0.007f);               // 0.510, 0.630
		handPath.lineTo(0.5f, 0.5f + 0.2f);                                 // 0.500, 0.700
		handPath.addCircle(0.5f, 0.5f, 0.025f, Path.Direction.CW);
	}
	
	private void initRanges(){
		rangeRect = new RectF();
		rangeRect.set(faceRect.left  + rangePosition, faceRect.top    + rangePosition,
				      faceRect.right - rangePosition, faceRect.bottom - rangePosition);
		
		valueRect = new RectF();
		valueRect.set(faceRect.left  + valuePosition, faceRect.top    + valuePosition,
				      faceRect.right - valuePosition, faceRect.bottom - valuePosition);
		
		rangeOkPaint = new Paint();
		rangeOkPaint.setStyle(Paint.Style.STROKE);
		rangeOkPaint.setColor(rangeOkColor);
		rangeOkPaint.setStrokeWidth(rangeStroke);
		rangeOkPaint.setAntiAlias(true);

		rangeWarningPaint = new Paint();
		rangeWarningPaint.setStyle(Paint.Style.STROKE);
		rangeWarningPaint.setColor(rangeWarningColor);
		rangeWarningPaint.setStrokeWidth(rangeStroke);
		rangeWarningPaint.setAntiAlias(true);

		rangeErrorPaint = new Paint();
		rangeErrorPaint.setStyle(Paint.Style.STROKE);
		rangeErrorPaint.setColor(rangeErrorColor);
		rangeErrorPaint.setStrokeWidth(rangeStroke);
		rangeErrorPaint.setAntiAlias(true);

		rangeAllPaint = new Paint();
		rangeAllPaint.setStyle(Paint.Style.STROKE);
		rangeAllPaint.setColor(0xcff8f8f8);
		rangeAllPaint.setStrokeWidth(rangeStroke);
		rangeAllPaint.setAntiAlias(true);
		rangeAllPaint.setShadowLayer(0.005f, -0.002f, -0.002f, 0x7f000000);

		valueOkPaint = new Paint();
		valueOkPaint.setStyle(Paint.Style.STROKE);
		valueOkPaint.setColor(rangeOkColor);
		valueOkPaint.setStrokeWidth(valueStroke);
		valueOkPaint.setAntiAlias(true);

		valueWarningPaint = new Paint();
		valueWarningPaint.setStyle(Paint.Style.STROKE);
		valueWarningPaint.setColor(rangeWarningColor);
		valueWarningPaint.setStrokeWidth(valueStroke);
		valueWarningPaint.setAntiAlias(true);

		valueErrorPaint = new Paint();
		valueErrorPaint.setStyle(Paint.Style.STROKE);
		valueErrorPaint.setColor(rangeErrorColor);
		valueErrorPaint.setStrokeWidth(valueStroke);
		valueErrorPaint.setAntiAlias(true);

		valueAllPaint = new Paint();
		valueAllPaint.setStyle(Paint.Style.STROKE);
		valueAllPaint.setColor(0xcff8f8f8);
		valueAllPaint.setStrokeWidth(valueStroke);
		valueAllPaint.setAntiAlias(true);
		valueAllPaint.setShadowLayer(0.005f, -0.002f, -0.002f, 0x7f000000);

	}
	private void initGraphics(){
		initBackground();
		initRim();
		initScale();
		initTitles();
		initHand();
		initRanges();
	}
	
	public void init() {
		initGraphics();
	}

	
	private void drawRim(Canvas canvas) {
		canvas.drawOval(rimRect, rimPaint);
		canvas.drawOval(rimRect, rimCirclePaint);
	}
	
	private void drawFace(Canvas canvas) {		
		canvas.drawOval(faceRect, facePaint);
		canvas.drawOval(faceRect, rimCirclePaint);
		canvas.drawOval(faceRect, rimShadowPaint);
	}
	
	private void drawScale(Canvas canvas) {
		canvas.drawOval(scaleRect, scalePaint);

		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		for (int i = 0; i < totalNotches; ++i) {
			float y1 = scaleRect.top;
			float y2 = y1 - 0.015f;
			float y3 = y1 - 0.025f;
			
			int value = notchToValue(i);

			if (i % (incrementPerLargeNotch/incrementPerSmallNotch) == 0) {
				if (value >= scaleMinValue && value <= scaleMaxValue) {
					canvas.drawLine(0.5f, y1, 0.5f, y3, scalePaint);

					String valueString = Integer.toString(value * notcheDisplayFactor);
					canvas.drawText(valueString, 0.5f, y3 - 0.015f, scalePaint);
				}
			}
			else{
				if (value >= scaleMinValue && value <= scaleMaxValue) {
					canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaint);
				}
			}
			
			canvas.rotate(degreesPerNotch, 0.5f, 0.5f);
		}
		canvas.restore();		
	}
	
	private void drawTitle(Canvas canvas) {
		canvas.drawTextOnPath(upperTitle, upperTitlePath, 0.0f, 0.02f, upperTitlePaint);				
		canvas.drawTextOnPath(lowerTitle, lowerTitlePath, 0.0f, 0.0f,  lowerTitlePaint);				
		canvas.drawTextOnPath(unitTitle,  unitPath,       0.0f, 0.0f,  unitPaint);				
	}
	
	private void drawScaleRanges(Canvas canvas) {
		degreeMinValue        = valueToAngle(scaleMinValue) + centerDegrees;
		degreeMaxValue        = valueToAngle(scaleMaxValue) + centerDegrees;
		degreeOkMinValue      = valueToAngle(rangeOkMinValue) + centerDegrees;
		degreeOkMaxValue      = valueToAngle(rangeOkMaxValue) + centerDegrees;
		degreeWarningMinValue = valueToAngle(rangeWarningMinValue) + centerDegrees;
		degreeWarningMaxValue = valueToAngle(rangeWarningMaxValue) + centerDegrees;
		degreeErrorMinValue   = valueToAngle(rangeErrorMinValue) + centerDegrees;
		degreeErrorMaxValue   = valueToAngle(rangeErrorMaxValue) + centerDegrees;

		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.drawArc(rangeRect, degreeMinValue,        degreeMaxValue -        degreeMinValue,        false, rangeAllPaint);
		canvas.drawArc(rangeRect, degreeOkMinValue,      degreeOkMaxValue      - degreeOkMinValue,      false, rangeOkPaint);
		canvas.drawArc(rangeRect, degreeWarningMinValue, degreeWarningMaxValue - degreeWarningMinValue, false, rangeWarningPaint);
		canvas.drawArc(rangeRect, degreeErrorMinValue,   degreeErrorMaxValue   - degreeErrorMinValue,   false, rangeErrorPaint);
		canvas.restore();		
	}
	
	private void drawGauge(Canvas canvas) {
			// When currentValue is not rotated, the tip of the hand points
			// to n -90 degrees.
		degreeMinValue        = valueToAngle(scaleMinValue) + centerDegrees;
		degreeMaxValue        = valueToAngle(scaleMaxValue) + centerDegrees;
		degreeOkMinValue      = valueToAngle(rangeOkMinValue) + centerDegrees;
		degreeOkMaxValue      = valueToAngle(rangeOkMaxValue) + centerDegrees;
		degreeWarningMinValue = valueToAngle(rangeWarningMinValue) + centerDegrees;
		degreeWarningMaxValue = valueToAngle(rangeWarningMaxValue) + centerDegrees;
		degreeErrorMinValue   = valueToAngle(rangeErrorMinValue) + centerDegrees;
		degreeErrorMaxValue   = valueToAngle(rangeErrorMaxValue) + centerDegrees;
			float angle = valueToAngle(currentValue) - 90;
	
			if(currentValue <= rangeOkMaxValue){
				canvas.drawArc(valueRect, degreeMinValue, angle - degreeMinValue, false, valueOkPaint);
			}
			if((currentValue > rangeOkMaxValue) && (currentValue <= rangeWarningMaxValue)){
				canvas.drawArc(valueRect, degreeMinValue, degreeOkMaxValue - degreeMinValue, false, valueOkPaint);
				canvas.drawArc(valueRect, degreeWarningMinValue, angle - degreeWarningMinValue, false, valueWarningPaint);
			}
			if((currentValue > rangeWarningMaxValue) && (currentValue <= rangeErrorMaxValue)){
				canvas.drawArc(valueRect, degreeMinValue, degreeOkMaxValue - degreeMinValue, false, valueOkPaint);
				canvas.drawArc(valueRect, degreeWarningMinValue, degreeWarningMaxValue - degreeWarningMinValue, false, valueWarningPaint);
				canvas.drawArc(valueRect, degreeErrorMinValue, angle - degreeErrorMinValue, false, valueErrorPaint);
			}
	}

	
	
	private void drawHand(Canvas canvas) {
			float angle = valueToAngle(currentValue);
			canvas.save(Canvas.MATRIX_SAVE_FLAG);
			canvas.rotate(angle, 0.5f, 0.5f);
			canvas.drawPath(handPath, handPaint);
			canvas.restore();
			canvas.drawCircle(0.5f, 0.5f, 0.01f, handScrewPaint);
	}
	
	private int notchToValue(int notch) {
		int rawValue = ((notch < totalNotches / 2) ? notch : (notch - totalNotches)) * incrementPerSmallNotch;
		int shiftedValue = rawValue + scaleCenterValue;
		return shiftedValue;
	}
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.d(TAG, "Size changed to " + w + "x" + h);
		regenerateBackground();
	}
	private void drawBackground(Canvas canvas) {
		if (background == null) {
			Log.w(TAG, "Background not created");
		} else {
			canvas.drawBitmap(background, 0, 0, backgroundPaint);
			
		}
	}
	
	public void regenerateBackground() {
		if (background != null) {
			background.recycle();
		}
		
		background = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		Canvas backgroundCanvas = new Canvas(background);
		float scale = getWidth();		
		backgroundCanvas.scale(scale, scale);
		
		drawRim(backgroundCanvas);
		drawFace(backgroundCanvas);
		drawScale(backgroundCanvas);
		drawScale(backgroundCanvas);
		if (showRange){
			drawScaleRanges(backgroundCanvas);
		}
		drawTitle(backgroundCanvas);	

	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d(TAG, "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
		Log.d(TAG, "Height spec: " + MeasureSpec.toString(heightMeasureSpec));
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int chosenWidth = chooseDimension(widthMode, widthSize);
		int chosenHeight = chooseDimension(heightMode, heightSize);
		int chosenDimension = Math.min(chosenWidth, chosenHeight);
		setMeasuredDimension(chosenDimension, chosenDimension);
	}
	
	private int chooseDimension(int mode, int size) {
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
			return size;
		} else { 
			return getPreferredSize();
		} 
	}
	
	private int getPreferredSize() {
		return 300;
	}
	
	private float valueToAngle(float value) {
		return  (value * degreesPerNotch) - (scaleCenterValue * degreesPerNotch) ;
		//return (value - scaleCenterValue) / 2.0f * degreesPerNotch;
	}
	
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Bundle bundle = (Bundle) state;
		Parcelable superState = bundle.getParcelable("superState");
		super.onRestoreInstanceState(superState);
		currentValue     = bundle.getFloat("currentValue");
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		Bundle state = new Bundle();
		state.putParcelable("superState", superState);
		state.putFloat("currentValue", currentValue);
		return state;
	}
	

	public void setValue(float value) {
		if(value < scaleMinValue) 
			value = scaleMinValue;
		else if (value > scaleMaxValue) 
			value = scaleMaxValue;
		currentValue = value;
		invalidate(); 
	}
	
	public float getValue(){
		return currentValue;
	}


	@Override
	protected void onDraw(Canvas canvas) {
		drawBackground(canvas);
		float scale = getWidth();		
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(scale, scale);
		if (showGauge){
			drawGauge(canvas);
		}

		drawHand(canvas);
		canvas.restore();
	}
}
