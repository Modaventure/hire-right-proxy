package com.mv.hr.client;

import org.apache.log4j.Logger;

import com.mv.base.exception.ThirdPartyBadResponseException;
import com.mv.base.exception.ThirdPartyConnectivityFailureException;

/**
 * Retries synchronously an http action when it fails
 */
public class RetryHttpActionExecutor {
	private static final Logger LOG = Logger.getLogger(RetryHttpActionExecutor.class);

	private int tries;
	private int maxTries;
	private long millisecondsBetweenTries;

	public RetryHttpActionExecutor(int maxTries, long millisecondsBetweenTries) {
		this.maxTries = maxTries;
		this.millisecondsBetweenTries = millisecondsBetweenTries;
	}

	public <T> T execute(HttpActionPerformer actionPerformer, Class<T> resultClass)
			throws ThirdPartyBadResponseException, ThirdPartyConnectivityFailureException {

		tries++;

		try {
			return actionPerformer.getResponse(resultClass);
		} catch (ThirdPartyBadResponseException | ThirdPartyConnectivityFailureException e) {
			if (tries == maxTries) {
				throw e;
			}

			LOG.warn(actionPerformer.getUrlPath() + " failed. About to try again for " + (tries + 1) + " out of " + maxTries + " times...");

			try {
				Thread.sleep(millisecondsBetweenTries);
			} catch (InterruptedException ie) {
				LOG.error("Thread has been asked to interrupt while waiting between retries. Returning null as a response.", ie);
				Thread.currentThread().interrupt(); // Restore the interrupted status of the thread
				return null;
			}

			LOG.debug("After waiting " + millisecondsBetweenTries + " ms " + actionPerformer.getUrlPath() + " is about to be performed...");

			return execute(actionPerformer, resultClass);
		}
	}
}
