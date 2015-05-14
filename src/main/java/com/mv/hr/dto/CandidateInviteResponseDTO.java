package com.mv.hr.dto;

import java.io.Serializable;

public class CandidateInviteResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String PassportReference;
	public String ExternalReferenceId;
	public String InvestigationReference;
	public String CaseNumber;
	public String CandidateInviteURL;
	public String CandidateInviteCode;
}
