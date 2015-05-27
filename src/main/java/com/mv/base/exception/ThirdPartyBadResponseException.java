package com.mv.base.exception;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.mv.base.RawDataInformation;
import com.mv.base.RequestRestartable;

public class ThirdPartyBadResponseException extends Exception implements RawDataInformation, RequestRestartable {
	private static final long serialVersionUID = 1L;

	private Response response;

	public ThirdPartyBadResponseException(String message, Response response) {
		super(message);
		this.response = response;
	}

	public ThirdPartyBadResponseException(String message, Response response, Throwable cause) {
		super(message, cause);
		this.response = response;
	}

	public Response getResponse() {
		return response;
	}

	@Override
	public Map<String, Object> getRawData() {
		Map<String, Object> rawResponse = new HashMap<String, Object>();

		rawResponse.put("headers", response.getHeaders());
		rawResponse.put("status", response.getStatus());

		Object body = null;
		try {
			body = response.readEntity(String.class);
		} catch (Exception e) {
			body = "Failed to read the raw body";
		}

		rawResponse.put("body", body);

		return rawResponse;
	}

	@Override
	public boolean isRequestRestartable() {
		return false;
	}
}
