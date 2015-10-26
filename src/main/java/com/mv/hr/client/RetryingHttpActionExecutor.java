package com.mv.hr.client;

import org.apache.log4j.Logger;

import com.mv.base.exception.ThirdPartyBadResponseException;
import com.mv.base.exception.ThirdPartyConnectivityFailureException;

/**
 * Retries synchronously an http action when it fails
 */
public class RetryingHttpActionExecutor {
	private static final Logger LOG = Logger.getLogger(RetryingHttpActionExecutor.class);

	private int tries;
	private int maxTries;
	private long millisecondsBetweenTries;

	public RetryingHttpActionExecutor(int maxTries, long millisecondsBetweenTries) {
		this.maxTries = maxTries;
		this.millisecondsBetweenTries = millisecondsBetweenTries;
	}

	public <T> T execute(HttpAction action, Class<T> resultClass)
			throws ThirdPartyBadResponseException, ThirdPartyConnectivityFailureException {

		tries++;

		try {
			return action.getResponse(resultClass);
		} catch (ThirdPartyBadResponseException | ThirdPartyConnectivityFailureException e) {
			if (tries == maxTries) {
				throw e;
			}

			LOG.warn(action.getUrlPath() + " failed. About to try again for " + (tries + 1) + " out of " + maxTries + " times...");

			if (millisecondsBetweenTries > 0) {
				try {
					Thread.sleep(millisecondsBetweenTries);
				} catch (InterruptedException ie) {
					LOG.error("Thread has been asked to interrupt while waiting between retries. Returning null as a response.", ie);
					Thread.currentThread().interrupt(); // Restore the interrupted status of the thread
					return null;
				}
			}

			LOG.debug("After waiting " + millisecondsBetweenTries + " ms " + action.getUrlPath() + " is about to be retried...");

			return execute(action, resultClass);
		}
	}
}
