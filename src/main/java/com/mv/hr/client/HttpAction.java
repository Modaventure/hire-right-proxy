package com.mv.hr.client;

import java.net.URI;

import com.mv.base.exception.ThirdPartyBadResponseException;
import com.mv.base.exception.ThirdPartyConnectivityFailureException;

public interface HttpAction {
	URI getUrlPath();
	<T> T getResponse(Class<T> resultClass) throws ThirdPartyBadResponseException, ThirdPartyConnectivityFailureException;
}
