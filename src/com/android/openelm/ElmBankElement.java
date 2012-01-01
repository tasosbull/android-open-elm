package com.android.openelm;

public class ElmBankElement  implements Comparable<ElmBankElement>{
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

	public ElmBankElement(){
    	
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
        if (another == null) return 1;
        Integer _id = another.id;
        return _id.compareTo(this.getId());
    }
}
