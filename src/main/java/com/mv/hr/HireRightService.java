package com.mv.hr;

import java.net.URI;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.stereotype.Service;

import com.mv.base.exception.MissingConfigurationException;
import com.mv.base.exception.ThirdPartyBadResponseException;
import com.mv.base.exception.ThirdPartyConnectivityFailureException;
import com.mv.hr.config.HireRightConfiguration;
import com.mv.hr.dto.AdditionalServiceRequestDTO;
import com.mv.hr.dto.AdditionalServiceResponseDTO;
import com.mv.hr.dto.CandidateInviteDTO;
import com.mv.hr.dto.CandidateInviteResponseDTO;
import com.mv.hr.dto.CandidateReportDTO;
import com.mv.hr.dto.DocumentDTO;
import com.mv.hr.dto.DocumentListDTO;
import com.mv.hr.dto.StatusNotificationDTO;

@Service
public class HireRightService {
	private static final Logger LOG = Logger.getLogger(HireRightService.class);

	String hireRightProfileId, hireRightBaseApiUrl;
	private UriBuilder builderUrlStartCandidateInvite;
	private UriBuilder builderUrlGetCandidateStatus;
	private UriBuilder builderUrlGetCandidateReport;
	private UriBuilder builderUrlGetDocumentList;
	private UriBuilder builderUrlGetDocument;
	private UriBuilder builderUrlAddAdditionalService;
	private Client webClient;

	public HireRightService(HireRightConfiguration configuration) {
		hireRightProfileId = configuration.getHireRightApiProfile();
		hireRightBaseApiUrl = configuration.getHireRightApiUrl();

		String hireRightUsername = configuration.getHireRightApiUsername();
		String hireRightPassword = configuration.getHireRightApiPassword();

		if (StringUtils.isEmpty(hireRightBaseApiUrl) || StringUtils.isEmpty(hireRightProfileId) ||
				StringUtils.isEmpty(hireRightUsername) || StringUtils.isEmpty(hireRightPassword)) {
			throw new MissingConfigurationException();
		}

		builderUrlStartCandidateInvite = getUriBuilder("InviteCandidate/{profileId}/{passportReference}/");
		builderUrlGetCandidateStatus = getUriBuilder("GetCandidateStatus/{profileId}/{passportReference}/{investigationReference}");
		builderUrlGetCandidateReport = getUriBuilder("GetCandidateReport/{profileId}/{passportReference}/{investigationReference}");
		builderUrlGetDocumentList = getUriBuilder("GetDocumentList/{profileId}/{passportReference}/{investigationReference}");
		builderUrlGetDocument = getUriBuilder("GetDocument/{profileId}/{passportReference}/{investigationReference}/{documentReference}");
		builderUrlAddAdditionalService = getUriBuilder("AddAdditionalService/{profileId}/{passportReference}/{investigationReference}");

		HttpAuthenticationFeature auth = HttpAuthenticationFeature.basicBuilder()
				.credentials(hireRightUsername, hireRightPassword)
				.build();


		ClientConfig clientConfig = new ClientConfig();
		int readTimeout = configuration.getHireRightApiReadTimeout();
		if (readTimeout > 0) {
			clientConfig = clientConfig.property(ClientProperties.READ_TIMEOUT, readTimeout);
		}

		webClient = ClientBuilder.newClient(clientConfig);
		webClient.register(auth);
		webClient.register(JacksonFeature.class);
	}

	private UriBuilder getUriBuilder(String path) {
		return UriBuilder.fromPath(hireRightBaseApiUrl).path(path).resolveTemplate("profileId", hireRightProfileId);
	}

	public CandidateInviteResponseDTO startCandidateInvite(CandidateInviteDTO candidateInvite, String passportReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		ResponseWrapper responseWrapper = post(candidateInvite, builderUrlStartCandidateInvite, passportReference);
		CandidateInviteResponseDTO responseDTO = parseResponse(responseWrapper, CandidateInviteResponseDTO.class);
		return responseDTO;
	}

	public StatusNotificationDTO getInvestigationStatus(String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		ResponseWrapper responseWrapper = get(builderUrlGetCandidateStatus, passportReference, investigationReference);
		StatusNotificationDTO statusDTO = parseResponse(responseWrapper, StatusNotificationDTO.class);
		return statusDTO;
	}

	public CandidateReportDTO getCandidateReport(String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		ResponseWrapper responseWrapper = get(builderUrlGetCandidateReport, passportReference, investigationReference);
		CandidateReportDTO candidateReportDTO = parseResponse(responseWrapper, CandidateReportDTO.class);
		return candidateReportDTO;
	}

	public DocumentListDTO getDocumentList(String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		ResponseWrapper responseWrapper = get(builderUrlGetDocumentList, passportReference, investigationReference);
		DocumentListDTO documentListDTO = parseResponse(responseWrapper, DocumentListDTO.class);
		return documentListDTO;
	}

	public DocumentDTO getDocument(String passportReference, String investigationReference, String documentReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		ResponseWrapper responseWrapper = get(builderUrlGetDocument, passportReference, investigationReference, documentReference);
		DocumentDTO documentDTO = parseResponse(responseWrapper, DocumentDTO.class);
		return documentDTO;
	}

	public AdditionalServiceResponseDTO addAdditionalService(AdditionalServiceRequestDTO request, String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		ResponseWrapper responseWrapper = post(request, builderUrlAddAdditionalService, passportReference, investigationReference);
		AdditionalServiceResponseDTO response = parseResponse(responseWrapper, AdditionalServiceResponseDTO.class);
		return response;
	}


	private void assertResponseValidity(ResponseWrapper responseWrapper) throws ThirdPartyBadResponseException {
		Response response = responseWrapper.response;
		if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			String errorMessage = responseWrapper.uri + " returned unsuccessful response, status: " + response.getStatus();
			LOG.error(errorMessage);
			throw new ThirdPartyBadResponseException(errorMessage, response);
		}
	}

	private <T> T parseResponse(ResponseWrapper responseWrapper, Class<T> resultClass) throws ThirdPartyBadResponseException {
		Response response = responseWrapper.response;
		try {
			response.bufferEntity();
			return response.readEntity(resultClass);
		} catch (ProcessingException e) {
			String message = e.getMessage();
			LOG.error("Failed parsing the response from " + responseWrapper.uri + ", message: " + message);
			throw new ThirdPartyBadResponseException(message, response, e);
		}
	}

	private ResponseWrapper get(UriBuilder uriBuilder, Object... uriParams) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		ResponseWrapper wrapper = new ResponseWrapper();
		URI urlPath = uriBuilder.build(uriParams);
		wrapper.uri = urlPath;
		Builder request = getRequest(urlPath);

		try {
			wrapper.response = request.get();
		} catch (ProcessingException e) {
			LOG.error("Connection failure when GETting " + urlPath + ", message: " + e.getMessage());
			throw new ThirdPartyConnectivityFailureException(e);
		}

		assertResponseValidity(wrapper);
		return wrapper;
	}

	private <T> ResponseWrapper post(T data, UriBuilder uriBuilder, Object... uriParams) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		ResponseWrapper wrapper = new ResponseWrapper();
		URI urlPath = uriBuilder.build(uriParams);
		wrapper.uri = urlPath;
		Builder request = getRequest(urlPath);
		Entity<T> requestEntity = Entity.entity(data, MediaType.APPLICATION_JSON_TYPE);

		try {
			wrapper.response = request.post(requestEntity);
		} catch (ProcessingException e) {
			LOG.error("Connection failure when POSTing to " + urlPath + ", message: " + e.getMessage());
			throw new ThirdPartyConnectivityFailureException(e, data);
		}

		assertResponseValidity(wrapper);
		return wrapper;
	}

	private Builder getRequest(URI urlPath) {
		WebTarget webResource = webClient.target(urlPath);
		return webResource.request(MediaType.APPLICATION_JSON_TYPE);
	}

	// response.getLocation() seems to return null so we need this to log the url of the request
	private class ResponseWrapper {
		public Response response;
		public URI uri;
	}
}
