package com.mv.hr.dto;

import java.io.Serializable;
import java.util.Date;

public class CandidateInviteDTO implements Serializable {
	public static final long serialVersionUID = 1L;

	public String ExternalReferenceId;
	public String Email;
	public String Forename;
	public String Surname;
	public String ExternalPackageReference;
	public String DivisionName;
	public String PONumber;
	public String CostCode;
	public Date IntendedStartDate;
	public int NotSubmittedDays;
	public boolean ReturnToClient;
	public boolean RestrictedClientAccess;
}
