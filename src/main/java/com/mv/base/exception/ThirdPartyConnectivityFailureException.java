package com.mv.base.exception;

import java.util.HashMap;
import java.util.Map;

import com.mv.base.JsonDataInformation;
import com.mv.base.RequestRestartable;
import com.mv.base.util.JSONUtil;


public class ThirdPartyConnectivityFailureException extends Exception
		implements JsonDataInformation, RequestRestartable {
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
	public String getRawJsonData() {
		Map<String, Object> rawJsonData = new HashMap<String, Object>();
		
		if (requestPayload != null) {
			rawJsonData.put("requestPayload", requestPayload);
		}

		Throwable cause = this.getCause();
		rawJsonData.put("cause", cause.getMessage());
		rawJsonData.put("stackTrace", cause.getStackTrace());
		
		return JSONUtil.toJson(rawJsonData);
	}

	@Override
	public boolean isRequestRestartable() {
		return true;
	}
}
