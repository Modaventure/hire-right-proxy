package com.mv.hr;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.mv.base.exception.MissingConfigurationException;
import com.mv.base.exception.ReadOnlyModeViolationException;
import com.mv.base.exception.ThirdPartyBadResponseException;
import com.mv.base.exception.ThirdPartyConnectivityFailureException;
import com.mv.hr.client.GetActionPerformer;
import com.mv.hr.client.PostActionPerformer;
import com.mv.hr.config.HireRightApiConfiguration;
import com.mv.hr.dto.AdditionalServiceRequestDTO;
import com.mv.hr.dto.AdditionalServiceResponseDTO;
import com.mv.hr.dto.CandidateInviteDTO;
import com.mv.hr.dto.CandidateInviteResponseDTO;
import com.mv.hr.dto.CandidateReportDTO;
import com.mv.hr.dto.DocumentDTO;
import com.mv.hr.dto.DocumentListDTO;
import com.mv.hr.dto.StatusNotificationDTO;

public class HireRightService {
	private HireRightApiConfiguration configuration;
	private String hireRightProfileId, hireRightBaseApiUrl;
	private UriBuilder builderUrlStartCandidateInvite;
	private UriBuilder builderUrlGetCandidateStatus;
	private UriBuilder builderUrlGetCandidateReport;
	private UriBuilder builderUrlGetDocumentList;
	private UriBuilder builderUrlGetDocument;
	private UriBuilder builderUrlAddAdditionalService;
	private Client webClient;

	public HireRightService(HireRightApiConfiguration configuration) {
		this.configuration = configuration;

		hireRightProfileId = configuration.getProfile();
		hireRightBaseApiUrl = configuration.getUrl();

		String hireRightUsername = configuration.getUsername();
		String hireRightPassword = configuration.getPassword();

		if (StringUtils.isEmpty(hireRightBaseApiUrl) || StringUtils.isEmpty(hireRightProfileId) ||
				StringUtils.isEmpty(hireRightUsername) || StringUtils.isEmpty(hireRightPassword)) {
			throw new MissingConfigurationException();
		}

		HttpAuthenticationFeature auth = HttpAuthenticationFeature.basicBuilder()
				.credentials(hireRightUsername, hireRightPassword)
				.build();


		ClientConfig clientConfig = new ClientConfig();
		int readTimeout = configuration.getReadTimeout();
		if (readTimeout > 0) {
			clientConfig = clientConfig.property(ClientProperties.READ_TIMEOUT, readTimeout);
		}

		webClient = ClientBuilder.newClient(clientConfig);
		webClient.register(auth);
		webClient.register(JacksonFeature.class);

		builderUrlStartCandidateInvite = getUriBuilder("InviteCandidate/{profileId}/{passportReference}/");
		builderUrlGetCandidateStatus = getUriBuilder("GetCandidateStatus/{profileId}/{passportReference}/{investigationReference}");
		builderUrlGetCandidateReport = getUriBuilder("GetCandidateReport/{profileId}/{passportReference}/{investigationReference}");
		builderUrlGetDocumentList = getUriBuilder("GetDocumentList/{profileId}/{passportReference}/{investigationReference}");
		builderUrlGetDocument = getUriBuilder("GetDocument/{profileId}/{passportReference}/{investigationReference}/{documentReference}");
		builderUrlAddAdditionalService = getUriBuilder("AddAdditionalService/{profileId}/{passportReference}/{investigationReference}");
	}

	private UriBuilder getUriBuilder(String path) {
		return UriBuilder.fromPath(hireRightBaseApiUrl).path(path).resolveTemplate("profileId", hireRightProfileId);
	}

	public CandidateInviteResponseDTO startCandidateInvite(CandidateInviteDTO candidateInvite, String passportReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		assertWriteMode();

		PostActionPerformer<CandidateInviteDTO> postPerformer = new PostActionPerformer<CandidateInviteDTO>(webClient,
				candidateInvite, builderUrlStartCandidateInvite, passportReference);

		return postPerformer.getResponse(CandidateInviteResponseDTO.class);
	}

	public StatusNotificationDTO getInvestigationStatus(String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		GetActionPerformer getPerformer = new GetActionPerformer(webClient,
				builderUrlGetCandidateStatus, passportReference, investigationReference);

		return getPerformer.getResponse(StatusNotificationDTO.class);
	}

	public CandidateReportDTO getCandidateReport(String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		GetActionPerformer getPerformer = new GetActionPerformer(webClient,
				builderUrlGetCandidateReport, passportReference, investigationReference);

		return getPerformer.getResponse(CandidateReportDTO.class);
	}

	public DocumentListDTO getDocumentList(String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		GetActionPerformer getPerformer = new GetActionPerformer(webClient,
				builderUrlGetDocumentList, passportReference, investigationReference);

		return getPerformer.getResponse(DocumentListDTO.class);
	}

	public DocumentDTO getDocument(String passportReference, String investigationReference, String documentReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		GetActionPerformer getPerformer = new GetActionPerformer(webClient,
				builderUrlGetDocument, passportReference, investigationReference, documentReference);

		return getPerformer.getResponse(DocumentDTO.class);
	}

	public AdditionalServiceResponseDTO addAdditionalService(AdditionalServiceRequestDTO request, String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		assertWriteMode();

		PostActionPerformer<AdditionalServiceRequestDTO> performer = new PostActionPerformer<AdditionalServiceRequestDTO>(webClient,
				request, builderUrlAddAdditionalService, passportReference, investigationReference);

		return performer.getResponse(AdditionalServiceResponseDTO.class);
	}

	private void assertWriteMode() {
		if (configuration.isInReadOnlyMode()) {
			throw new ReadOnlyModeViolationException();
		}
	}
}
