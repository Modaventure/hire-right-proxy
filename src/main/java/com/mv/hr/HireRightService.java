package com.mv.hr;

import com.mv.base.exception.MissingConfigurationException;
import com.mv.base.exception.ThirdPartyBadResponseException;
import com.mv.base.exception.ThirdPartyConnectivityFailureException;
import com.mv.hr.config.HireRightConfiguration;
import com.mv.hr.dto.CandidateInviteDTO;
import com.mv.hr.dto.CandidateInviteResponseDTO;
import com.mv.hr.dto.CandidateReportDTO;
import com.mv.hr.dto.StatusNotificationDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class HireRightService {
	private static final Logger LOG = Logger.getLogger(HireRightService.class);

	private UriBuilder builderUrlStartCandidateInvite;
	private UriBuilder builderUrlGetCandidateStatus;
	private UriBuilder builderUrlGetCandidateReport;

	private Client webClient;

	public HireRightService(HireRightConfiguration configuration) {
		String hireRightBaseApiUrl = configuration.getHireRightApiUrl();
		String hireRightProfileId = configuration.getHireRightApiProfile();
		String hireRightUsername = configuration.getHireRightApiUsername();
		String hireRightPassword = configuration.getHireRightApiPassword();
		
		if (StringUtils.isEmpty(hireRightBaseApiUrl) || StringUtils.isEmpty(hireRightProfileId) ||
				StringUtils.isEmpty(hireRightUsername) || StringUtils.isEmpty(hireRightPassword)) {
			throw new MissingConfigurationException();
		}
		
		builderUrlStartCandidateInvite = UriBuilder.fromPath(hireRightBaseApiUrl)
				.path("InviteCandidate/{profileId}/{passportReference}/");
		builderUrlStartCandidateInvite = builderUrlStartCandidateInvite.resolveTemplate("profileId", hireRightProfileId);
		
		builderUrlGetCandidateStatus = UriBuilder.fromPath(hireRightBaseApiUrl)
				.path("GetCandidateStatus/{profileId}/{passportReference}/{investigationReference}");
		builderUrlGetCandidateStatus = builderUrlGetCandidateStatus.resolveTemplate("profileId", hireRightProfileId);
		
		builderUrlGetCandidateReport = UriBuilder.fromPath(hireRightBaseApiUrl)
				.path("GetCandidateReport/{profileId}/{passportReference}/{investigationReference}");
		builderUrlGetCandidateReport = builderUrlGetCandidateReport.resolveTemplate("profileId", hireRightProfileId);
		
		HttpAuthenticationFeature auth = HttpAuthenticationFeature.basicBuilder()
			.nonPreemptive()
			.credentials(hireRightUsername, hireRightPassword)
			.build();
		
		webClient = ClientBuilder.newClient();
		webClient.register(auth);
		webClient.register(JacksonFeature.class);
	}
	
	public CandidateInviteResponseDTO startCandidateInvite(CandidateInviteDTO candidateInvite, String passportReference)
			throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		Response response = postCandidateInviteRequest(candidateInvite, passportReference);
		assertResponseValidity(response, passportReference);
		CandidateInviteResponseDTO responseDTO = parseHireRightResponse(response, passportReference, CandidateInviteResponseDTO.class);

		return responseDTO;
	}
	
	private Response postCandidateInviteRequest(CandidateInviteDTO candidateInvite, String passportReference) throws ThirdPartyConnectivityFailureException {
		Entity<CandidateInviteDTO> requestEntity = Entity.entity(candidateInvite, MediaType.APPLICATION_JSON_TYPE);
		Builder request = getCandidateInviteRequest(passportReference);
		
		try {
			return request.post(requestEntity);
		} catch (ProcessingException e) {
			LOG.error("Connection failure when starting an investigation for user " + passportReference + ", message: " + e.getMessage());
			throw new ThirdPartyConnectivityFailureException(e, candidateInvite);
		}
	}
	
	private Builder getCandidateInviteRequest(String passportReference) {
		URI urlPath = builderUrlStartCandidateInvite.build(passportReference);
		WebTarget webResource = webClient.target(urlPath);
		return webResource.request(MediaType.APPLICATION_JSON_TYPE);
	}
	
	private void assertResponseValidity(Response response, String identifier) throws ThirdPartyBadResponseException {
		if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			String errorMessage = "HireRight returned unsuccessful response for entity " + identifier + ", status: " + response.getStatus();
			LOG.error(errorMessage);
			throw new ThirdPartyBadResponseException(errorMessage, response);
		}
	}
	
	private <T> T parseHireRightResponse(Response response, String identifier, Class<T> resultClass) throws ThirdPartyBadResponseException {
		response.bufferEntity();
		
		try {
			return response.readEntity(resultClass);
		} catch (ProcessingException e) {
			LOG.error("Failed parsing HireRight's response for entity " + identifier + ", message: " + e.getMessage());
			throw new ThirdPartyBadResponseException(e.getMessage(), response, e);
		}
	}
	
	public StatusNotificationDTO getInvestigationStatus(String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		Response response = getStatusResponse(passportReference, investigationReference);
		assertResponseValidity(response, investigationReference);
		StatusNotificationDTO statusDTO = parseHireRightResponse(response, investigationReference, StatusNotificationDTO.class);

		return statusDTO;
	}

	private Response getStatusResponse(String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException {
		Builder request = getStatusRequest(passportReference, investigationReference);
		
		try {
			return request.get();
		} catch (ProcessingException e) {
			LOG.error("Connection failure when getting investigation status for user " + passportReference + ", message: " + e.getMessage());
			throw new ThirdPartyConnectivityFailureException(e);
		}
	}
	
	private Builder getStatusRequest(String passportReference, String investigationReference) {
		URI urlPath = builderUrlGetCandidateStatus.build(passportReference, investigationReference);
		WebTarget webResource = webClient.target(urlPath);
		return webResource.request(MediaType.APPLICATION_JSON_TYPE);
	}
	
	public CandidateReportDTO getCandidateReport(String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException, ThirdPartyBadResponseException {
		Response response = getCandidateReportResponse(passportReference, investigationReference);
		assertResponseValidity(response, investigationReference);
		CandidateReportDTO candidateReportDTO = parseHireRightResponse(response, investigationReference, CandidateReportDTO.class);
		
		return candidateReportDTO;
	}

	private Response getCandidateReportResponse(String passportReference, String investigationReference) throws ThirdPartyConnectivityFailureException {
		Builder request = getCandidateReportRequest(passportReference, investigationReference);
		
		try {
			return request.get();
		} catch (ProcessingException e) {
			LOG.error("Connection failure when requesting a report for user " + passportReference + ", message: " + e.getMessage());
			throw new ThirdPartyConnectivityFailureException(e);
		}
	}

	private Builder getCandidateReportRequest(String passportReference, String investigationReference) {
		URI urlPath = builderUrlGetCandidateReport.build(passportReference, investigationReference);
		WebTarget webResource = webClient.target(urlPath);
		return webResource.request(MediaType.APPLICATION_JSON_TYPE);
	}
}
