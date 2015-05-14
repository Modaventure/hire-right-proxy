package com.mv.hr.dto;

import java.io.Serializable;
import java.util.Date;

public class CandidateInviteDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String externalReferenceId;
	private String email;
	private String forename;
	private String surname;
	private String externalPackageReference;
	private String divisionName;
	private String pONumber;
	private String costCode;
	private Date intendedStartDate;
	private int notSubmittedDays;
	private boolean returnToClient;
	private boolean restrictedClientAccess;

	public String getExternalReferenceId() {
		return externalReferenceId;
	}

	public void setExternalReferenceId(String externalReferenceId) {
		this.externalReferenceId = externalReferenceId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getExternalPackageReference() {
		return externalPackageReference;
	}

	public void setExternalPackageReference(String externalPackageReference) {
		this.externalPackageReference = externalPackageReference;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getpONumber() {
		return pONumber;
	}

	public void setpONumber(String pONumber) {
		this.pONumber = pONumber;
	}

	public String getCostCode() {
		return costCode;
	}

	public void setCostCode(String costCode) {
		this.costCode = costCode;
	}

	public Date getIntendedStartDate() {
		return intendedStartDate;
	}

	public void setIntendedStartDate(Date intendedStartDate) {
		this.intendedStartDate = intendedStartDate;
	}

	public int getNotSubmittedDays() {
		return notSubmittedDays;
	}

	public void setNotSubmittedDays(int notSubmittedDays) {
		this.notSubmittedDays = notSubmittedDays;
	}

	public boolean isReturnToClient() {
		return returnToClient;
	}

	public void setReturnToClient(boolean returnToClient) {
		this.returnToClient = returnToClient;
	}

	public boolean isRestrictedClientAccess() {
		return restrictedClientAccess;
	}

	public void setRestrictedClientAccess(boolean restrictedClientAccess) {
		this.restrictedClientAccess = restrictedClientAccess;
	}
}
