package com.mv.hr.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

public class PutActionPerformer<T> extends PayloadActionPerformer<T> {
	private static final String METHOD_NAME = "PUT";

	public PutActionPerformer(Client webClient, T payload,
			UriBuilder uriBuilder, Object... uriParams) {
		super(webClient, payload, uriBuilder, uriParams);
	}

	@Override
	Response payloadAct(Entity<T> requestEntity) {
		return request.put(requestEntity);
	}

	@Override
	String getMethodName() {
		return METHOD_NAME;
	}
}
