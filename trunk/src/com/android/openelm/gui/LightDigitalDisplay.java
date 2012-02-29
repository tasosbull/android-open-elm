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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class LightDigitalDisplay extends View {
	private static final String TAG = LightDigitalDisplay.class.getSimpleName();
	private static final float unitPosition = 0.300f;
	private static final float titlePosition = 0.1f;
	private static final float valuePosition = 0.2f;

	private static final float rimSize = 0.02f;
	private Bitmap background;
	private Paint backgroundPaint;
	private float value = 12534;
	private String unitTitle = "Unit Title";

	private RectF titleRect;
	private Paint titlePaint;
	private int titlesColor = Color.rgb(255, 255, 255);
	private Path titlePath;
	private Path valuePath;
	private RectF valueRect;
	private Paint valuePaint;
	//private int valueColor = Color.rgb(255, 255, 255);

	private RectF unitRect;
	private Paint unitPaint;

	private RectF rimRect;
	private Paint rimPaint;
	private RectF faceRect;
	private Paint facePaint;
	private int faceColor = Color.rgb(0, 0, 128);

	private void initBackground() {
		backgroundPaint = new Paint();
		backgroundPaint.setFilterBitmap(true);
	}

	public LightDigitalDisplay(Context context) {
		super(context);
		init();
	}

	public LightDigitalDisplay(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LightDigitalDisplay(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void setValue(float aValue) {
		this.value = aValue;
		invalidate();
	}

	public void setTitles(String unitTitle) {

		this.unitTitle = unitTitle;
	}

	public void setFaceColor(int r, int g, int b) {
		faceColor = Color.rgb(r, g, b);
	}

	private void initRim() {
		rimRect = new RectF(0.0f, 0.0f, 1.0f, 1.0f);

		faceRect = new RectF();
		faceRect.set(rimRect.left + rimSize, rimRect.top + rimSize,
				rimRect.right - rimSize, rimRect.bottom - rimSize);

		rimPaint = new Paint();
		rimPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		rimPaint.setShader(new LinearGradient(0.40f, 0.0f, 0.60f, 1.0f, Color
				.rgb(0xf0, 0xf5, 0xf0), Color.rgb(0x30, 0x31, 0x30),
				Shader.TileMode.CLAMP));
		unitRect = new RectF();
		unitRect.set(faceRect.left + unitPosition, faceRect.top + unitPosition,
				faceRect.right - unitPosition, faceRect.bottom - unitPosition);

		facePaint = new Paint();
		facePaint.setFilterBitmap(true);
		facePaint.setStyle(Paint.Style.FILL);
		facePaint.setColor(faceColor);
		unitPaint = new Paint();
		unitPaint.setColor(titlesColor);
		unitPaint.setAntiAlias(true);
		unitPaint.setTypeface(Typeface.DEFAULT_BOLD);
		unitPaint.setTextAlign(Paint.Align.CENTER);
		unitPaint.setTextSize(0.10f);
		unitPaint.setTextScaleX(0.8f);
		titleRect = new RectF();
		titleRect.set(faceRect.left + titlePosition, faceRect.top
				+ titlePosition, faceRect.right - titlePosition,
				faceRect.bottom - ((faceRect.bottom * 3) / 4));
		titlePaint = new Paint();
		titlePaint.setFilterBitmap(true);
		titlePaint.setStyle(Paint.Style.FILL);
		titlePaint.setColor(titlesColor);
		titlePaint.setAntiAlias(true);
		titlePaint.setTypeface(Typeface.DEFAULT_BOLD);
		titlePaint.setTextAlign(Paint.Align.LEFT);
		titlePaint.setTextSize(0.10f);
		titlePaint.setTextScaleX(0.8f);
		titlePath = new Path();
		titlePath.addRect(titleRect, Direction.CW);

		valueRect = new RectF();
		valueRect.set(faceRect.left + titlePosition, titleRect.top 
				 + titleRect.bottom + (valuePosition), titleRect.right,
				faceRect.bottom );
		valuePaint = new Paint();
		valuePaint.setFilterBitmap(true);
		valuePaint.setStyle(Paint.Style.FILL);
		valuePaint.setColor(titlesColor);
		valuePaint.setAntiAlias(true);
		valuePaint.setTypeface(Typeface.DEFAULT);
		valuePaint.setTextAlign(Paint.Align.LEFT);
		valuePaint.setTextSize(0.40f);
		valuePaint.setTextScaleX(0.5f);
		valuePath = new Path();
		valuePath.addRect(valueRect, Direction.CW);

	}

	private void drawRim(Canvas canvas) {
		canvas.drawRect(rimRect, rimPaint);
	}

	private void drawFace(Canvas canvas) {
		canvas.drawRect(faceRect, facePaint);

	}

	private void drawTitle(Canvas canvas) {

		// canvas.drawText(unitTitle, 5 ,5, 0.0f, 0.0f, titlePaint);
		canvas.drawTextOnPath(unitTitle, titlePath, 0.0f, 0.02f, titlePaint);
	}

	private void drawValue(Canvas canvas) {

		// canvas.drawText(unitTitle, 5 ,5, 0.0f, 0.0f, titlePaint);
		canvas.drawTextOnPath(Float.toString(value), valuePath, 0.0f, 0.02f,
				valuePaint);
	}

	private void initGraphics() {
		initRim();
		initBackground();

	}

	public void init() {
		initGraphics();
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

	private void drawBackground(Canvas canvas) {
		if (background == null) {
			Log.w(TAG, "Background not created");
		} else {
			canvas.drawBitmap(background, 0, 0, backgroundPaint);

		}
	}

	private void regenerateBackground() {
		if (background != null) {
			background.recycle();
		}

		background = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas backgroundCanvas = new Canvas(background);
		float scale = getWidth();
		backgroundCanvas.scale(scale, scale);
		drawRim(backgroundCanvas);
		drawFace(backgroundCanvas);
		drawTitle(backgroundCanvas);
		drawValue(backgroundCanvas);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.d(TAG, "Size changed to " + w + "x" + h);
		regenerateBackground();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBackground(canvas);
		float scale = getWidth();		
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(scale, scale);
		drawValue(canvas);
		canvas.restore();
	}

}
