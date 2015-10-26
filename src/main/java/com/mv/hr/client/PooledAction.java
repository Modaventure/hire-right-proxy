package com.mv.hr.client;

import java.net.URI;

import com.mv.base.exception.ThirdPartyBadResponseException;
import com.mv.base.exception.ThirdPartyConnectivityFailureException;

public class PooledAction implements HttpAction {
	private ActionPool pool;
	private HttpAction action;

	public PooledAction(ActionPool pool, HttpAction actionPerformer) {
		this.pool = pool;
		this.action = actionPerformer;
	}

	@Override
	public <T> T getResponse(Class<T> resultClass)
			throws ThirdPartyBadResponseException, ThirdPartyConnectivityFailureException {
		return pool.execute(action, resultClass);
	}

	@Override
	public URI getUrlPath() {
		return action.getUrlPath();
	}
}
