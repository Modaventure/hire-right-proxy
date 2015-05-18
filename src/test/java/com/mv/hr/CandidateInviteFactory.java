package com.mv.hr;

import com.mv.hr.dto.CandidateInviteDTO;

import java.util.UUID;

public class CandidateInviteFactory {
    static public class ExtendedCandidateInviteDTO extends CandidateInviteDTO {
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
        String randomString = random.toString();

        ExtendedCandidateInviteDTO inviteDTO = new ExtendedCandidateInviteDTO();
        inviteDTO.setId(random);
        inviteDTO.setPassportReference(randomString);

        inviteDTO.setEmail("testy.testisov@test.com");
        inviteDTO.setForename("Testy");
        inviteDTO.setSurname("Testisov");
        inviteDTO.setpONumber("1113");
        inviteDTO.setExternalPackageReference("package1");
        inviteDTO.setExternalReferenceId("someUniqueId");
        inviteDTO.setDivisionName("HBMFromTests");
        inviteDTO.setNotSubmittedDays(10);
        inviteDTO.setReturnToClient(false);
        inviteDTO.setRestrictedClientAccess(false);

        return inviteDTO;
    }
}
