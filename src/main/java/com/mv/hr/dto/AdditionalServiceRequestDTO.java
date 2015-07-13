package com.mv.hr.dto;

import java.io.Serializable;

public class AdditionalServiceRequestDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	public String ScreeningServiceName;
	public boolean HasExtendedData;
	public AddOnExtendedRequestData ExtendedRequestData;
}
