package com.mv.hr.client;

import java.net.URI;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;

import com.mv.base.exception.ThirdPartyBadResponseException;
import com.mv.base.exception.ThirdPartyConnectivityFailureException;

public abstract class ActionPerformer {
	private static final Logger LOG = Logger.getLogger(ActionPerformer.class);

	URI urlPath;
	Builder request;

	public ActionPerformer(Client webClient, UriBuilder uriBuilder, Object... uriParams) {
		this.urlPath = uriBuilder.build(uriParams);
		this.request = getRequest(webClient);

	}

	private Builder getRequest(Client webClient) {
		WebTarget webResource = webClient.target(urlPath);
		return webResource.request(MediaType.APPLICATION_JSON_TYPE);
	}

	public <T> T getResponse(Class<T> resultClass) throws ThirdPartyBadResponseException, ThirdPartyConnectivityFailureException {
		Response response = this.act();
		assertResponseValidity(response);
		return parseResponse(response, resultClass);
	}

	abstract Response act() throws ThirdPartyConnectivityFailureException;

	private <T> T parseResponse(Response response, Class<T> resultClass) throws ThirdPartyBadResponseException {
		try {
			response.bufferEntity();
			return response.readEntity(resultClass);
		} catch (ProcessingException e) {
			String message = e.getMessage();
			LOG.error("Failed parsing the response from " + urlPath + ", message: " + message);
			throw new ThirdPartyBadResponseException(message, response, e);
		}
	}

	private void assertResponseValidity(Response response) throws ThirdPartyBadResponseException {
		if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			String responseString = response.readEntity(String.class);
			String errorMessage = urlPath + " returned unsuccessful response, status: " + response.getStatus();

			LOG.error(errorMessage + "; response: " + responseString);
			throw new ThirdPartyBadResponseException(errorMessage, response);
		}
	}
}
