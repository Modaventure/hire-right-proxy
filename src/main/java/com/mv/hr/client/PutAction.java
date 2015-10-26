package com.mv.hr.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class PutAction<T> extends PayloadAction<T> {
	private static final String METHOD_NAME = "PUT";

	public PutAction(Client webClient) {
		super(webClient);
	}

	@Override
	protected Response payloadAct(Entity<T> requestEntity) {
		return request.put(requestEntity);
	}

	@Override
	protected String getMethodName() {
		return METHOD_NAME;
	}
}
