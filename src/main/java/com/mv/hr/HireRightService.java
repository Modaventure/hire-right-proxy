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
import com.mv.hr.client.ActionPool;
import com.mv.hr.client.GetAction;
import com.mv.hr.client.HttpAction;
import com.mv.hr.client.PooledAction;
import com.mv.hr.client.PostAction;
import com.mv.hr.client.RetryingHttpActionExecutor;
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
	private ActionPool getRequestsActionPool;

	public HireRightService(HireRightApiConfiguration configuration) {
		this.configuration = configuration;

		getRequestsActionPool = new ActionPool(configuration.getMaxSimultaneousGetCalls());

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

		HttpAction postAction = new PostAction<CandidateInviteDTO>(webClient)
				.setPayload(candidateInvite)
				.setUrlPath(builderUrlStartCandidateInvite, passportReference);

		return getPostCallExecutor().execute(postAction, CandidateInviteResponseDTO.class);
	}

	public StatusNotificationDTO getInvestigationStatus(String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		HttpAction getAction = new GetAction(webClient)
				.setUrlPath(builderUrlGetCandidateStatus, passportReference, investigationReference);

		return getRequestsActionPool.execute(getAction, StatusNotificationDTO.class);
	}

	public CandidateReportDTO getCandidateReport(String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		HttpAction getAction = new GetAction(webClient)
				.setUrlPath(builderUrlGetCandidateReport, passportReference, investigationReference);

		PooledAction pooledActionWrapper = new PooledAction(getRequestsActionPool, getAction);
		return getGetCallExecutor().execute(pooledActionWrapper, CandidateReportDTO.class);
	}

	public DocumentListDTO getDocumentList(String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		HttpAction getAction = new GetAction(webClient)
				.setUrlPath(builderUrlGetDocumentList, passportReference, investigationReference);

		PooledAction pooledActionWrapper = new PooledAction(getRequestsActionPool, getAction);
		return getGetCallExecutor().execute(pooledActionWrapper, DocumentListDTO.class);
	}

	public DocumentDTO getDocument(String passportReference, String investigationReference, String documentReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		HttpAction getAction = new GetAction(webClient)
				.setUrlPath(builderUrlGetDocument, passportReference, investigationReference, documentReference);

		PooledAction pooledActionWrapper = new PooledAction(getRequestsActionPool, getAction);
		return getGetCallExecutor().execute(pooledActionWrapper, DocumentDTO.class);
	}

	public AdditionalServiceResponseDTO addAdditionalService(AdditionalServiceRequestDTO request, String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		assertWriteMode();

		HttpAction postAction = new PostAction<AdditionalServiceRequestDTO>(webClient)
				.setPayload(request)
				.setUrlPath(builderUrlAddAdditionalService, passportReference, investigationReference);

		return getPostCallExecutor().execute(postAction, AdditionalServiceResponseDTO.class);
	}

	private void assertWriteMode() {
		if (configuration.isInReadOnlyMode()) {
			throw new ReadOnlyModeViolationException();
		}
	}

	private RetryingHttpActionExecutor getPostCallExecutor() {
		return new RetryingHttpActionExecutor(
				configuration.getMaxPostCallRetries(),
				configuration.getMillisecondsBetweenRetries());
	}

	private RetryingHttpActionExecutor getGetCallExecutor() {
		return new RetryingHttpActionExecutor(
				configuration.getMaxGetCallRetries(),
				configuration.getMillisecondsBetweenRetries());
	}
}
