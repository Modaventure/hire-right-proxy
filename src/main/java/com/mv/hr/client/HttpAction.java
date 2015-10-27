package com.mv.hr.client;

import java.net.URI;

import com.mv.base.exception.ThirdPartyBadResponseException;
import com.mv.base.exception.ThirdPartyConnectivityFailureException;

public interface HttpAction<T> {
	URI getUrlPath();
	T execute() throws ThirdPartyBadResponseException, ThirdPartyConnectivityFailureException;
}
