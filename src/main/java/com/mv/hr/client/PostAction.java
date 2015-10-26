package com.mv.hr.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class PostAction<T> extends PayloadAction<T> {
	private static final String METHOD_NAME = "POST";

	public PostAction(Client webClient) {
		super(webClient);
	}

	@Override
	protected Response payloadAct(Entity<T> requestEntity) {
		return request.post(requestEntity);
	}

	@Override
	protected String getMethodName() {
		return METHOD_NAME;
	}
}
