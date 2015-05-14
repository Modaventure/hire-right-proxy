package com.mv.hr.dto;

import java.io.Serializable;
import java.util.Date;

public class ScreenTaskDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String TaskReference;
	public String TaskNumber;
	public String ScreeningType;
	public String ScreeningServiceName;
	public String OrderStatus;
	public boolean IsComplete;
	public String ResultStatus;
	public int DiscrepancyCount;
	public Date DateCreated;
	public Date DateCompleted;
}
