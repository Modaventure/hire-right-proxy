package com.mv.hr.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

public class PostActionPerformer<T> extends PayloadActionPerformer<T> {
	private static final String METHOD_NAME = "POST";

	public PostActionPerformer(Client webClient, T payload,
			UriBuilder uriBuilder, Object... uriParams) {
		super(webClient, payload, uriBuilder, uriParams);
	}

	@Override
	Response payloadAct(Entity<T> requestEntity) {
		return request.post(requestEntity);
	}

	@Override
	String getMethodName() {
		return METHOD_NAME;
	}
}
