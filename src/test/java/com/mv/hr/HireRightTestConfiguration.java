package com.mv.hr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.mv.hr.config.HireRightApiConfiguration;

public class HireRightTestConfiguration implements HireRightApiConfiguration {
	private Properties properties;

	public HireRightTestConfiguration() throws IOException {
		String filePath = System.getProperty("envConfigPropertiesFilePath");

		System.out.println("=============================================================");
		System.out.println("# Loading HireRight configuration from file:");
		System.out.println("# " + filePath);
		System.out.println("=============================================================");

		FileInputStream inputStream = new FileInputStream(new File(filePath));
		properties = new Properties();
		properties.load(inputStream);
		inputStream.close();
	}

	@Override
	public String getUrl() {
		return properties.getProperty("endpoint.url");
	}

	@Override
	public String getProfile() {
		return properties.getProperty("profile");
	}

	@Override
	public String getUsername() {
		return properties.getProperty("username");
	}

	@Override
	public String getPassword() {
		return properties.getProperty("password");
	}

	@Override
	public int getReadTimeout() {
		return readIntProperty("timeout");
	}

	@Override
	public boolean isInReadOnlyMode() {
		return "true".equals(properties.getProperty("readonly"));
	}

	@Override
	public int getMaxSimultaneousGetCalls() {
		return readIntProperty("pool.get.size");
	}

	@Override
	public int getMaxPostCallRetries() {
		return readIntProperty("retries.post");
	}

	@Override
	public int getMaxGetCallRetries() {
		return readIntProperty("retries.get");
	}

	@Override
	public int getMillisecondsBetweenRetries() {
		return readIntProperty("retries.pause");
	}

	private int readIntProperty(String propertyName) {
		String propertyValue = properties.getProperty(propertyName);
		return StringUtils.isEmpty(propertyValue) ? 0 : Integer.parseInt(propertyValue);
	}
}
