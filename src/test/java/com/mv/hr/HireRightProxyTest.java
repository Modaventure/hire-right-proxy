package com.mv.hr;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mv.hr.dto.AdditionalServiceRequestDTO;
import com.mv.hr.dto.AdditionalServiceResponseDTO;
import com.mv.hr.dto.CandidateInviteResponseDTO;
import com.mv.hr.dto.CandidateReportDTO;
import com.mv.hr.dto.DocumentListDTO;
import com.mv.hr.dto.StatusNotificationDTO;

public class HireRightProxyTest {
	private static HireRightTestConfiguration configuration;

	private HireRightRequestFactory requestFactory;

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
		requestFactory = new HireRightRequestFactory(configuration);

		inviteDTO = CandidateInviteFactory.randomInvite();
		passportReference = inviteDTO.getPassportReference();

		inviteResponseDTO = requestFactory.startCandidateInvite(inviteDTO, passportReference).execute();
		investigationReference = inviteResponseDTO.InvestigationReference;
	}

	@Test
	public void startCandidateInviteTest() throws Exception {
		assertThat(inviteResponseDTO.PassportReference, is(equalTo(inviteDTO.getPassportReference())));
		assertThat(inviteResponseDTO.ExternalReferenceId, is(equalTo(inviteDTO.ExternalReferenceId)));

		assertThat(inviteResponseDTO.InvestigationReference, is(not(isEmptyOrNullString())));
		assertThat(inviteResponseDTO.CaseNumber, is(not(isEmptyOrNullString())));
		assertThat(inviteResponseDTO.CandidateInviteURL, is(not(isEmptyOrNullString())));
		assertThat(inviteResponseDTO.CandidateInviteCode, is(not(isEmptyOrNullString())));
	}

	@Test
	public void testGetNewInvestigationStatus() throws Exception {
		StatusNotificationDTO statusNotificationDTO = requestFactory.getInvestigationStatus(passportReference, investigationReference).execute();

		assertThat(statusNotificationDTO.ExternalPackageReference, is(inviteDTO.ExternalPackageReference));
		assertThat(statusNotificationDTO.IsComplete, is(false));

		// it will return null for unknown devision names (HiredByMe is known)
		assertThat(statusNotificationDTO.DivisionName, is(inviteDTO.DivisionName));
		assertThat(statusNotificationDTO.ServiceLevelName, is(not(isEmptyOrNullString())));
		assertThat(statusNotificationDTO.OrderStatus, is(not(isEmptyOrNullString())));

		assertThat(statusNotificationDTO.DateCreated, is(notNullValue()));
		assertThat(statusNotificationDTO.ScreeningStartDate, is(notNullValue()));

		// not null only for completed investigations
		assertThat(statusNotificationDTO.DateDelivered, is(nullValue()));

		assertThat(statusNotificationDTO.PercentageComplete, is(greaterThanOrEqualTo(0)));
		assertThat(statusNotificationDTO.TaskCount, is(greaterThanOrEqualTo(0)));
		assertThat(statusNotificationDTO.DiscrepancyCount, is(greaterThanOrEqualTo(0)));
	}

	@Test
	public void testGetCandidateReport() throws Exception {
		CandidateReportDTO candidateReport = requestFactory.getCandidateReport(passportReference, investigationReference).execute();

		assertThat(candidateReport.PassportReference, is(inviteDTO.getPassportReference()));
		assertThat(candidateReport.ExternalReferenceId, is(inviteDTO.ExternalReferenceId));
		assertThat(candidateReport.InvestigationReference, is(investigationReference));

		assertThat(candidateReport.CaseNumber, is(not(isEmptyOrNullString())));
		assertThat(candidateReport.MimeType, is(not(isEmptyOrNullString())));
		assertThat(candidateReport.FileType, is(not(isEmptyOrNullString())));
		assertThat(candidateReport.ContentEncodingMethod, is(not(isEmptyOrNullString())));
		assertThat(candidateReport.Content, is(not(isEmptyOrNullString())));
	}

	@Test
	public void testGetDocumentList() throws Exception {
		DocumentListDTO documentList = requestFactory.getDocumentList(passportReference, investigationReference).execute();
		assertThat(documentList.Documents, is(empty()));
	}

	@Test
	public void testAddAdditionalService() throws Exception {
		AdditionalServiceRequestDTO request = new AdditionalServiceRequestDTO();
		request.ScreeningServiceName = "CIFAS";
		request.HasExtendedData = false;

		AdditionalServiceResponseDTO response = requestFactory.addAdditionalService(request , passportReference, investigationReference).execute();
		assertThat(response, is(notNullValue()));
		assertThat(response.CaseNumber, is(not(isEmptyOrNullString())));
		assertThat(response.InvestigationReference, is(not(isEmptyOrNullString())));
		assertThat(response.IsLinkedInvestigation, is(equalTo(true)));
		assertThat(response.ParentCaseNumber, is(equalTo(inviteResponseDTO.CaseNumber)));
		assertThat(response.ParentInvestigationReference, is(equalTo(investigationReference)));
	}
}