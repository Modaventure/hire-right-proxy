package com.mv.hr.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class StatusNotificationDTO extends CandidateInviteResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String DivisionName;
	public String ExternalPackageReference;
	public String ServiceLevelName;
	public String OrderStatus;
	public boolean IsComplete;
	public Date DateCreated;
	public Date ScreeningStartDate;
	public Date DateDelivered;
	public int PercentageComplete;
	public int TaskCount;
	public int DiscrepancyCount;
	public List<ScreenTaskDTO> Screenings;
}