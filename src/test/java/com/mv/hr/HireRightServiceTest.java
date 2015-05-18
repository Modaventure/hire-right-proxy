package com.mv.hr;

import com.mv.hr.dto.CandidateInviteResponseDTO;
import com.mv.hr.dto.CandidateReportDTO;
import com.mv.hr.dto.StatusNotificationDTO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class HireRightServiceTest {
    private static HireRightTestConfiguration configuration;

    private HireRightService service;

    private CandidateInviteFactory.ExtendedCandidateInviteDTO inviteDTO;
    private CandidateInviteResponseDTO inviteResponseDTO;

    private String passportReference;
    private String investigationReference;

    @BeforeClass
    public static void createConfiguration() throws Exception {
        configuration = new HireRightTestConfiguration();
    }

    @Before
    public void setUp() throws Exception {
        service = new HireRightService(configuration);

        inviteDTO = CandidateInviteFactory.randomInvite();
        passportReference = inviteDTO.getPassportReference();

        inviteResponseDTO = service.startCandidateInvite(inviteDTO, passportReference);
        investigationReference = inviteResponseDTO.InvestigationReference;
    }

    @Test
    public void startCandidateInviteTest() throws Exception {
        assertThat(inviteResponseDTO.PassportReference, is(equalTo(inviteDTO.getPassportReference())));
        assertThat(inviteResponseDTO.ExternalReferenceId, is(equalTo(inviteDTO.getExternalReferenceId())));

        assertThat(inviteResponseDTO.InvestigationReference, is(not(isEmptyOrNullString())));
        assertThat(inviteResponseDTO.CaseNumber, is(not(isEmptyOrNullString())));
        assertThat(inviteResponseDTO.CandidateInviteURL, is(not(isEmptyOrNullString())));
        assertThat(inviteResponseDTO.CandidateInviteCode, is(not(isEmptyOrNullString())));
    }

    @Test
    public void testGetInvestigationStatus() throws Exception {
        StatusNotificationDTO statusNotificationDTO = service.getInvestigationStatus(passportReference, investigationReference);

        assertThat(statusNotificationDTO.ExternalPackageReference, is(inviteDTO.getExternalPackageReference()));
        assertThat(statusNotificationDTO.IsComplete, is(false));

        assertThat(statusNotificationDTO.DivisionName, is(not(isEmptyOrNullString())));
        assertThat(statusNotificationDTO.ServiceLevelName, is(not(isEmptyOrNullString())));
        assertThat(statusNotificationDTO.OrderStatus, is(not(isEmptyOrNullString())));

        assertThat(statusNotificationDTO.DateCreated, is(notNullValue()));
        assertThat(statusNotificationDTO.ScreeningStartDate, is(notNullValue()));
        assertThat(statusNotificationDTO.DateDelivered, is(notNullValue()));

        assertThat(statusNotificationDTO.PercentageComplete, is(greaterThanOrEqualTo(0)));
        assertThat(statusNotificationDTO.TaskCount, is(greaterThanOrEqualTo(0)));
        assertThat(statusNotificationDTO.DiscrepancyCount, is(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testGetCandidateReport() throws Exception {
        CandidateReportDTO candidateReport = service.getCandidateReport(passportReference, investigationReference);

        assertThat(candidateReport.PassportReference, is(inviteDTO.getPassportReference()));
        assertThat(candidateReport.ExternalReferenceId, is(inviteDTO.getExternalReferenceId()));
        assertThat(candidateReport.InvestigationReference, is(investigationReference));

        assertThat(candidateReport.CaseNumber, is(not(isEmptyOrNullString())));
        assertThat(candidateReport.MimeType, is(not(isEmptyOrNullString())));
        assertThat(candidateReport.FileType, is(not(isEmptyOrNullString())));
        assertThat(candidateReport.ContentEncodingMethod, is(not(isEmptyOrNullString())));
        assertThat(candidateReport.Content, is(not(isEmptyOrNullString())));
    }
}