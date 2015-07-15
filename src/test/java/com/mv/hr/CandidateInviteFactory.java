package com.mv.hr;

import java.util.UUID;

import com.mv.hr.dto.CandidateInviteDTO;

public class CandidateInviteFactory {
	static public class ExtendedCandidateInviteDTO extends CandidateInviteDTO {
		private static final long serialVersionUID = 1L;

		private transient UUID id;
		private transient String passportReference;

		public UUID getId() {
			return id;
		}

		public void setId(UUID id) {
			this.id = id;
		}

		public String getPassportReference() {
			return passportReference;
		}

		public void setPassportReference(String passportReference) {
			this.passportReference = passportReference;
		}
	}

	static public ExtendedCandidateInviteDTO randomInvite() {
		UUID random = UUID.randomUUID();
		String randomString = random.toString().replaceAll("-", "");

		ExtendedCandidateInviteDTO inviteDTO = new ExtendedCandidateInviteDTO();
		inviteDTO.setId(random);
		inviteDTO.setPassportReference(randomString);

		inviteDTO.Email = "test@hiredbyme.com";
		inviteDTO.Forename = "Testy";
		inviteDTO.Surname = "Testisov";
		inviteDTO.PONumber = "1113";
		inviteDTO.ExternalPackageReference = "package1";
		inviteDTO.ExternalReferenceId = randomString;
		inviteDTO.DivisionName = "HiredByMe";
		inviteDTO.NotSubmittedDays = 10;
		inviteDTO.ReturnToClient = false;
		inviteDTO.RestrictedClientAccess = false;

		return inviteDTO;
	}
}
