package com.mv.hr.config;

public interface HireRightApiConfiguration {
	String getUrl();
	String getProfile();
	String getUsername();
	String getPassword();
	int getReadTimeout();
	boolean isInReadOnlyMode();
	int getMaxSimultaneousGetCalls();
}
