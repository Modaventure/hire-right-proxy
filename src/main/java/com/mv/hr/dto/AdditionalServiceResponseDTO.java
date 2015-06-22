package com.mv.hr.dto;

import java.io.Serializable;

public class AdditionalServiceResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	public String InvestigationReference;
	public String CaseNumber;
	public boolean IsLinkedInvestigation;
	public String ParentInvestigationReference;
	public String ParentCaseNumber;
}
