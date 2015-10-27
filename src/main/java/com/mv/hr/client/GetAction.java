package com.mv.hr.client;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.mv.base.exception.ThirdPartyConnectivityFailureException;

public class GetAction<T> extends HttpActionBase<T> {
	private static final Logger LOG = Logger.getLogger(GetAction.class);

	public static <T> GetAction<T> createGetAction(Client webClient, Class<T> resultClass) {
		return new GetAction<>(webClient, resultClass);
	}

	public GetAction(Client webClient, Class<T> resultClass) {
		super(webClient, resultClass);
	}

	@Override
	protected Response act() throws ThirdPartyConnectivityFailureException {
		try {
			return getRequest().get();
		} catch (ProcessingException e) {
			LOG.error("Connection failure when GETting " + urlPath + ", message: " + e.getMessage());
			throw new ThirdPartyConnectivityFailureException(e);
		}
	}
}
