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

package com.android.openelm;

public class GaugeSchema {
	
	private boolean analog;
	private int totalNotches;
	private int incrementLarge;
	private int incrementSmall;
	private int factor;
	private int scaleMin;
	private int scaleCenter;
	private int scaleMax;
	private boolean gaugeVisible;
	private boolean rangeVisible;
/*	private int okMin;
	private int okMax;
	private int warningMin;
	private int warningMax;
	private int errorMin;
	private int errorMax;
*/	private int faceRed;
	private int faceGreen;
	private int faceBlue;
	private int scaleRed;
	private int scaleGreen;
	private int scaleBlue;
	private int handRed;
	private int handGreen;
	private int handBlue;
	private int titlesRed;
	private int titlesGreen;
	private int titlesBlue;
	private String titleLower;
	private String titleUpper;
	private String titleUnit;
	public boolean isAnalog() {
		return analog;
	}
	public void setAnalog(boolean analog) {
		this.analog = analog;
	}
	public int getTotalNotches() {
		return totalNotches;
	}
	public void setTotalNotches(int totalNotches) {
		this.totalNotches = totalNotches;
	}
	public int getIncrementLarge() {
		return incrementLarge;
	}
	public void setIncrementLarge(int incrementLarge) {
		this.incrementLarge = incrementLarge;
	}
	public int getIncrementSmall() {
		return incrementSmall;
	}
	public void setIncrementSmall(int incrementSmall) {
		this.incrementSmall = incrementSmall;
	}
	public int getFactor() {
		return factor;
	}
	public void setFactor(int factor) {
		this.factor = factor;
	}
	public int getScaleMin() {
		return scaleMin;
	}
	public void setScaleMin(int scaleMin) {
		this.scaleMin = scaleMin;
	}
	public int getScaleCenter() {
		return scaleCenter;
	}
	public void setScaleCenter(int scaleCenter) {
		this.scaleCenter = scaleCenter;
	}
	public int getScaleMax() {
		return scaleMax;
	}
	public void setScaleMax(int scaleMax) {
		this.scaleMax = scaleMax;
	}
	public boolean isGaugeVisible() {
		return gaugeVisible;
	}
	public void setGaugeVisible(boolean gaugeVisible) {
		this.gaugeVisible = gaugeVisible;
	}
	public boolean isRangeVisible() {
		return rangeVisible;
	}
	public void setRangeVisible(boolean rangeVisible) {
		this.rangeVisible = rangeVisible;
	}
/*	public int getOkMin() {
		return okMin;
	}
	public void setOkMin(int okMin) {
		this.okMin = okMin;
	}
	public int getOkMax() {
		return okMax;
	}
	public void setOkMax(int okMax) {
		this.okMax = okMax;
	}
	public int getWarningMin() {
		return warningMin;
	}
	public void setWarningMin(int warningMin) {
		this.warningMin = warningMin;
	}
	public int getWarningMax() {
		return warningMax;
	}
	public void setWarningMax(int warningMax) {
		this.warningMax = warningMax;
	}
	public int getErrorMin() {
		return errorMin;
	}
	public void setErrorMin(int errorMin) {
		this.errorMin = errorMin;
	}
	public int getErrorMax() {
		return errorMax;
	}
	public void setErrorMax(int errorMax) {
		this.errorMax = errorMax;
	}*/
	public int getFaceRed() {
		return faceRed;
	}
	public void setFaceRed(int faceRed) {
		this.faceRed = faceRed;
	}
	public int getFaceGreen() {
		return faceGreen;
	}
	public void setFaceGreen(int faceGreen) {
		this.faceGreen = faceGreen;
	}
	public int getFaceBlue() {
		return faceBlue;
	}
	public void setFaceBlue(int faceBlue) {
		this.faceBlue = faceBlue;
	}
	public int getScaleRed() {
		return scaleRed;
	}
	public void setScaleRed(int scaleRed) {
		this.scaleRed = scaleRed;
	}
	public int getScaleGreen() {
		return scaleGreen;
	}
	public void setScaleGreen(int scaleGreen) {
		this.scaleGreen = scaleGreen;
	}
	public int getScaleBlue() {
		return scaleBlue;
	}
	public void setScaleBlue(int scaleBlue) {
		this.scaleBlue = scaleBlue;
	}
	public int getHandRed() {
		return handRed;
	}
	public void setHandRed(int handRed) {
		this.handRed = handRed;
	}
	public int getHandGreen() {
		return handGreen;
	}
	public void setHandGreen(int handGreen) {
		this.handGreen = handGreen;
	}
	public int getHandBlue() {
		return handBlue;
	}
	public void setHandBlue(int handBlue) {
		this.handBlue = handBlue;
	}
	public int getTitlesRed() {
		return titlesRed;
	}
	public void setTitlesRed(int titlesRed) {
		this.titlesRed = titlesRed;
	}
	public int getTitlesGreen() {
		return titlesGreen;
	}
	public void setTitlesGreen(int titlesGreen) {
		this.titlesGreen = titlesGreen;
	}
	public int getTitlesBlue() {
		return titlesBlue;
	}
	public void setTitlesBlue(int titlesBlue) {
		this.titlesBlue = titlesBlue;
	}
	public String getTitleLower() {
		return titleLower;
	}
	public void setTitleLower(String titleLower) {
		this.titleLower = titleLower;
	}
	public String getTitleUpper() {
		return titleUpper;
	}
	public void setTitleUpper(String titleUpper) {
		this.titleUpper = titleUpper;
	}
	public String getTitleUnit() {
		return titleUnit;
	}
	public void setTitleUnit(String titleUnit) {
		this.titleUnit = titleUnit;
	}
	
	

}
