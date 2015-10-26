package com.mv.hr.client;

import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;

import com.mv.base.exception.ThirdPartyBadResponseException;
import com.mv.base.exception.ThirdPartyConnectivityFailureException;

public class ActionPool {
	private static final Logger LOG = Logger.getLogger(ActionPool.class);

	private Semaphore semaphore;

	public ActionPool(int poolSize) {
		if (poolSize > 0) {
			semaphore = new Semaphore(poolSize, true);
		}
	}

	public <T> T execute(HttpAction action, Class<T> resultClass)
			throws ThirdPartyBadResponseException, ThirdPartyConnectivityFailureException {

		if (semaphore != null) {
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				LOG.error("Thread has been asked to interrupt while waiting for a place in the pool. Returning null as a response.", e);
				Thread.currentThread().interrupt(); // Restore the interrupted status of the thread
				return null;
			}
		}

		try {
			return action.getResponse(resultClass);
		} finally {
			if (semaphore != null) {
				semaphore.release();
			}
		}
	}
}
