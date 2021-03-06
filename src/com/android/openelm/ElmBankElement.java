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

public class ElmBankElement implements Comparable<ElmBankElement> {
	public static double NOVAL = -123456;
	private int id;
	private boolean active;
	private String pid;
	private String mode;
	private String description;
	private String shortDescription;
	private String units;
	private int numbytes;
	private double minval;
	private double warning;
	private double error;
	private double maxval;
	private String formula;
	private double peakvalue;
	private GaugeSchema gauge;
	private double valuefactor;
	public double currentValue;

	public double getValuefactor() {
		return valuefactor;
	}

	public void setValuefactor(double valuefactor) {
		this.valuefactor = valuefactor;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public GaugeSchema getGauge() {
		return gauge;
	}

	public void setGauge(GaugeSchema gauge) {
		this.gauge = gauge;
	}

	public void setWarning(double warning) {
		this.warning = warning;
	}

	public ElmBankElement() {

		warning = NOVAL;
		error = NOVAL;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public int getNumbytes() {
		return numbytes;
	}

	public void setNumbytes(int numbytes) {
		this.numbytes = numbytes;
	}

	public double getMinval() {
		return minval;
	}

	public void setMinval(double minval) {
		this.minval = minval;
	}

	public double getMaxval() {
		return maxval;
	}

	public void setMaxval(double maxval) {
		this.maxval = maxval;
	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public double getPeakvalue() {
		return peakvalue;
	}

	public void setPeakvalue(double peakvalue) {
		this.peakvalue = peakvalue;
	}

	public double getWarning() {
		return warning;
	}

	public void setWarningl(double warning) {
		this.warning = warning;
	}

	public int compareTo(ElmBankElement another) {
		if (another == null)
			return 1;
		Integer _id = another.id;
		if(this.getId() < _id) return -1;
		else if (this.getId() == _id) return 0;
		else return 1;
		//return _id.compareTo(this.getId());
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\nid: " + Integer.toString(id));
		stringBuffer.append("\nactive: " + Boolean.toString(active));
		stringBuffer.append("\npid: " + pid);
		stringBuffer.append("\nmode: " + mode);
		stringBuffer.append("\ndescription: " + description);
		stringBuffer.append("\nshortDescription: " + shortDescription);
		stringBuffer.append("\nunits: " + units);
		stringBuffer.append("\nnumBytes: " + Integer.toString(numbytes));
		stringBuffer.append("\nminval: " + Double.toString(minval));
		stringBuffer.append("\nwarning: " + Double.toString(warning));
		stringBuffer.append("\nerror: " + Double.toString(error));
		stringBuffer.append("\nmaxval: " + Double.toString(maxval));
		stringBuffer.append("\nformula: " + formula);
		stringBuffer.append("\npeakvalue: " + Double.toString(peakvalue));
		stringBuffer.append("\nvaluefactor: " + Double.toString(valuefactor));
		if (gauge != null) {
			stringBuffer.append("\ngauge analog: "
					+ Boolean.toString(gauge.isAnalog()));
			stringBuffer.append("\ngauge totalnotches: "
					+ Integer.toString(gauge.getTotalNotches()));
			stringBuffer.append("\ngauge incrementLarge: "
					+ Integer.toString(gauge.getIncrementLarge()));
			stringBuffer.append("\ngauge incrementSmall: "
					+ Integer.toString(gauge.getIncrementSmall()));
			stringBuffer.append("\ngauge factor: "
					+ Integer.toString(gauge.getFactor()));
			stringBuffer.append("\ngauge scaleMin: "
					+ Integer.toString(gauge.getScaleMin()));
			stringBuffer.append("\ngauge scaleMax: "
					+ Integer.toString(gauge.getScaleMax()));
			stringBuffer.append("\ngauge gaugeVisible: "
					+ Boolean.toString(gauge.isGaugeVisible()));
			stringBuffer.append("\ngauge rangeVisible: "
					+ Boolean.toString(gauge.isRangeVisible()));
			stringBuffer.append("\ngauge faceRed: "
					+ Integer.toString(gauge.getFaceRed()));
			stringBuffer.append("\ngauge faceGreen: "
					+ Integer.toString(gauge.getFaceGreen()));
			stringBuffer.append("\ngauge faceBlue: "
					+ Integer.toString(gauge.getFaceBlue()));
			stringBuffer.append("\ngauge ScaleRed: "
					+ Integer.toString(gauge.getScaleRed()));
			stringBuffer.append("\ngauge ScaleGreen: "
					+ Integer.toString(gauge.getScaleGreen()));
			stringBuffer.append("\ngauge ScaleBlue: "
					+ Integer.toString(gauge.getScaleBlue()));
			stringBuffer.append("\ngauge HandRed: "
					+ Integer.toString(gauge.getHandRed()));
			stringBuffer.append("\ngauge HandGreen: "
					+ Integer.toString(gauge.getHandGreen()));
			stringBuffer.append("\ngauge HandBlue: "
					+ Integer.toString(gauge.getHandBlue()));
			stringBuffer.append("\ngauge TitlesRed: "
					+ Integer.toString(gauge.getTitlesRed()));
			stringBuffer.append("\ngauge TitlesGreen: "
					+ Integer.toString(gauge.getTitlesGreen()));
			stringBuffer.append("\ngauge TitlesBlue: "
					+ Integer.toString(gauge.getTitlesBlue()));
			stringBuffer.append("\ngauge titleLower: " + gauge.getTitleLower());
			stringBuffer.append("\ngauge titleUpper: " + gauge.getTitleUpper());
			stringBuffer.append("\ngauge titleUnit: " + gauge.getTitleUnit());
		}

		return stringBuffer.toString();
	}

}
