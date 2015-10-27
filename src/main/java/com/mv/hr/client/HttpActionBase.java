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

public abstract class HttpActionBase<T> implements HttpAction<T> {
	private static final Logger LOG = Logger.getLogger(HttpActionBase.class);

	protected URI urlPath;
	protected Client webClient;
	protected Class<T> resultClass;

	public HttpActionBase(Client webClient, Class<T> resultClass) {
		this.webClient = webClient;
		this.resultClass = resultClass;
	}

	@Override
	public URI getUrlPath() {
		return urlPath;
	}

	public HttpActionBase<T> setUrlPath(UriBuilder uriBuilder, Object... uriParams) {
		this.urlPath = uriBuilder.build(uriParams);
		return this;
	}

	@Override
	public T execute() throws ThirdPartyBadResponseException, ThirdPartyConnectivityFailureException {
		Response response = this.act();
		assertResponseValidity(response);
		return parseResponse(response);
	}

	protected Builder getRequest() {
		WebTarget webResource = webClient.target(urlPath);
		return webResource.request(MediaType.APPLICATION_JSON_TYPE);
	}

	protected abstract Response act() throws ThirdPartyConnectivityFailureException;

	protected T parseResponse(Response response) throws ThirdPartyBadResponseException {
		try {
			return response.readEntity(resultClass);
		} catch (ProcessingException e) {
			String message = e.getMessage();

			Throwable cause = e.getCause();
			if (cause != null) {
				message += "; " + cause.getMessage();
			}

			logDebugInfo(response, message);

			throw new ThirdPartyBadResponseException(message, response, e);
		}
	}

	protected void logDebugInfo(Response response, String message) {
		String headers = response.getHeaders().toString();
		int status = response.getStatus();

		String body;
		try {
			body = response.readEntity(String.class);
		} catch (Exception e){
			body = "Can't read the body";
		}

		LOG.error("Failed parsing the response from " + urlPath + ", message: " + message);
		LOG.debug(urlPath);
		LOG.debug(status);
		LOG.debug(headers);
		LOG.debug(body);
	}

	protected void assertResponseValidity(Response response) throws ThirdPartyBadResponseException {
		String errorMessage = null;

		try {
			response.bufferEntity();
		} catch (ProcessingException e) {
			errorMessage = urlPath + " returned malformed response";
		}

		if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			errorMessage = urlPath + " returned unsuccessful response, status: " + response.getStatus();
			String responseString = response.readEntity(String.class);
			errorMessage += "; response: " + responseString;
		}

		if (errorMessage != null) {
			LOG.error(errorMessage);
			throw new ThirdPartyBadResponseException(errorMessage, response);
		}
	}
}
