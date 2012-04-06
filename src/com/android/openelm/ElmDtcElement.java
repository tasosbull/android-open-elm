package com.android.openelm;

import java.util.List;

public class ElmDtcElement {

	private String id;
	
	private String descr;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	

	public static String GetDescrById(List<ElmDtcElement> elements, String id){
		for (int i = 0; i < elements.size(); i++){
			ElmDtcElement elem = elements.get(i);
			if(elem.getId().equals(id))
				return elem.getDescr();
		}
		return null;
	}
	
}
