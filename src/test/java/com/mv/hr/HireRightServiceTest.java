package com.mv.hr;

import com.mv.hr.dto.CandidateInviteResponseDTO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.junit.Assert.assertThat;

public class HireRightServiceTest {
    private static HireRightTestConfiguration configuration;

    private HireRightService service;

    @BeforeClass
    public static void createConfiguration() throws Exception {
        configuration = new HireRightTestConfiguration();
    }

    @Before
    public void setUp() {
        service = new HireRightService(configuration);
    }

    @Test
    public void startCandidateInviteTest() throws Exception {
        CandidateInviteFactory.ExtendedCandidateInviteDTO inviteDTO = CandidateInviteFactory.randomInvite();

        CandidateInviteResponseDTO responseDTO = service.startCandidateInvite(inviteDTO, inviteDTO.getPassportReference());

        assertThat(responseDTO.PassportReference, is(equalTo(inviteDTO.getPassportReference())));
        assertThat(responseDTO.ExternalReferenceId, is(equalTo(inviteDTO.getExternalReferenceId())));

        assertThat(responseDTO.InvestigationReference, is(not(isEmptyOrNullString())));
        assertThat(responseDTO.CaseNumber, is(not(isEmptyOrNullString())));
        assertThat(responseDTO.CandidateInviteURL, is(not(isEmptyOrNullString())));
        assertThat(responseDTO.CandidateInviteCode, is(not(isEmptyOrNullString())));
    }
}