package com.mv.hr.client;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.mv.base.exception.ThirdPartyConnectivityFailureException;

public class GetActionPerformer extends HttpActionPerformerBase {
	private static final Logger LOG = Logger.getLogger(GetActionPerformer.class);

	public GetActionPerformer(Client webClient) {
		super(webClient);
	}

	@Override
	protected Response act() throws ThirdPartyConnectivityFailureException {
		try {
			return request.get();
		} catch (ProcessingException e) {
			LOG.error("Connection failure when GETting " + urlPath + ", message: " + e.getMessage());
			throw new ThirdPartyConnectivityFailureException(e);
		}
	}
}
