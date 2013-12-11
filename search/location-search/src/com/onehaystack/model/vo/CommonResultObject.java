package com.onehaystack.model.vo;

import java.util.ArrayList;
import java.util.List;

public class CommonResultObject {
	public double sourceLat;
	public double sourceLng;
	public List<Result> results = new ArrayList<Result>();
	
	
	
	public void setResult(List<Result> r){
		results = r;
	}	
	
}
