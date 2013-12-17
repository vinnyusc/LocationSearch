package com.onehaystack.model;

import java.util.List;

import com.onehaystack.model.vo.DoctorObject;

public interface DoctorInterface {
	
	List<DoctorObject> getDoctorInfo(DoctorObject doctor);
	
}
