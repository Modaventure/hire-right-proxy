package com.mv.hr.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class PostAction<T, S> extends PayloadAction<T, S> {
	private static final String METHOD_NAME = "POST";

	public static <T, S> PostAction<T, S> createPostAction(Client webClient, Class<T> resultClass, Class<S> payloadClass) {
		return new PostAction<>(webClient, resultClass);
	}

	public PostAction(Client webClient, Class<T> resultClass) {
		super(webClient, resultClass);
	}

	@Override
	protected Response payloadAct(Entity<S> requestEntity) {
		return getRequest().post(requestEntity);
	}

	@Override
	protected String getMethodName() {
		return METHOD_NAME;
	}
}
