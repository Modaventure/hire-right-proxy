package com.mv.hr.client;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;

import com.mv.base.exception.ThirdPartyConnectivityFailureException;

public abstract class PayloadActionPerformer<T> extends ActionPerformer {
	private static final Logger LOG = Logger.getLogger(PayloadActionPerformer.class);
	protected T payload;

	public PayloadActionPerformer(Client webClient, T payload, UriBuilder uriBuilder,
			Object... uriParams) {
		super(webClient, uriBuilder, uriParams);

		this.payload = payload;
	}

	@Override
	protected Response act() throws ThirdPartyConnectivityFailureException {
		Entity<T> requestEntity = Entity.entity(payload, MediaType.APPLICATION_JSON_TYPE);

		try {
			return payloadAct(requestEntity);
		} catch (ProcessingException e) {
			LOG.error("Connection failure when " + this.getMethodName() + "ing to " + urlPath + ", message: " + e.getMessage());
			throw new ThirdPartyConnectivityFailureException(e, payload);
		}

	}

	protected abstract Response payloadAct(Entity<T> requestEntity);
	protected abstract String getMethodName();
}
