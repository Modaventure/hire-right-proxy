package com.mv.hr.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class PutAction<T, S> extends PayloadAction<T, S> {
	private static final String METHOD_NAME = "PUT";

	public static <T, S> PutAction<T, S> createPutAction(Client webClient, Class<T> resultClass, Class<S> payloadClass) {
		return new PutAction<>(webClient, resultClass);
	}

	public PutAction(Client webClient, Class<T> resultClass) {
		super(webClient, resultClass);
	}

	@Override
	protected Response payloadAct(Entity<S> requestEntity) {
		return getRequest().put(requestEntity);
	}

	@Override
	protected String getMethodName() {
		return METHOD_NAME;
	}
}
