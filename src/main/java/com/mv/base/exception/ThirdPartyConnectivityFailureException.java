package com.mv.base.exception;

import java.util.HashMap;
import java.util.Map;

import com.mv.base.RawDataInformation;
import com.mv.base.RequestRestartable;


public class ThirdPartyConnectivityFailureException extends Exception
implements RawDataInformation, RequestRestartable {
	private static final long serialVersionUID = 1L;

	private Object requestPayload;

	public ThirdPartyConnectivityFailureException(Throwable cause) {
		super(cause);
	}

	public ThirdPartyConnectivityFailureException(Throwable cause, Object requestPayload) {
		super(cause);
		this.requestPayload = requestPayload;
	}

	@Override
	public Map<String, Object> getRawData() {
		Map<String, Object> rawData = new HashMap<String, Object>();

		if (requestPayload != null) {
			rawData.put("requestPayload", requestPayload);
		}

		Throwable cause = this.getCause();
		rawData.put("cause", cause.getMessage());
		rawData.put("stackTrace", cause.getStackTrace());

		return rawData;
	}

	@Override
	public boolean isRequestRestartable() {
		return true;
	}
}
