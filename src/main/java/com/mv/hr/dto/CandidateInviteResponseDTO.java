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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((PassportReference == null) ? 0 : PassportReference.hashCode());
		result = prime * result + ((ExternalReferenceId == null) ? 0 : ExternalReferenceId.hashCode());
		result = prime * result + ((InvestigationReference == null) ? 0 : InvestigationReference.hashCode());
		result = prime * result + ((CaseNumber == null) ? 0 : CaseNumber.hashCode());
		result = prime * result + ((CandidateInviteURL == null) ? 0 : CandidateInviteURL.hashCode());
		result = prime * result + ((CandidateInviteCode == null) ? 0 : CandidateInviteCode.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CandidateInviteResponseDTO)) {
			return false;
		}
		CandidateInviteResponseDTO other = (CandidateInviteResponseDTO) obj;

		return PassportReference.equals(other.PassportReference)
				&& ExternalReferenceId.equals(other.ExternalReferenceId)
				&& InvestigationReference.equals(other.InvestigationReference)
				&& CaseNumber.equals(other.CaseNumber)
				&& CandidateInviteURL.equals(other.CandidateInviteURL)
				&& CandidateInviteCode.equals(other.CandidateInviteCode)
				;
	}
}
