package com.mv.hr.client;

import com.mv.base.exception.ThirdPartyBadResponseException;
import com.mv.base.exception.ThirdPartyConnectivityFailureException;

public class PooledActionPerformer implements HttpActionPerformer {
	private ActionPool pool;
	private HttpActionPerformer actionPerformer;

	public PooledActionPerformer(ActionPool pool, HttpActionPerformer actionPerformer) {
		this.pool = pool;
		this.actionPerformer = actionPerformer;
	}

	@Override
	public <T> T getResponse(Class<T> resultClass)
			throws ThirdPartyBadResponseException, ThirdPartyConnectivityFailureException {
		return pool.execute(actionPerformer, resultClass);
	}
}
