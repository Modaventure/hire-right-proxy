package com.mv.hr;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.givenThat;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mv.base.exception.ThirdPartyBadResponseException;
import com.mv.base.exception.ThirdPartyConnectivityFailureException;

public class FailuresTest {
	private static final int REST_MOCK_PORT = 8089;
	private static HireRightTestConfiguration configuration;
	private static final String GET_DOCUMENTS_LIST_URL = "/GetDocumentList/profile/1/2";

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(REST_MOCK_PORT);

	private HireRightProxy proxy;

	@BeforeClass
	public static void createConfiguration() throws Exception {
		configuration = new HireRightTestConfiguration("failures-test.properties");
	}

	@Before
	public void setUp() throws Exception {
		proxy = new HireRightProxy(configuration);
	}

	@Test(expected = ThirdPartyBadResponseException.class)
	public void failToParseMalformedResponse() throws Exception {
		givenThat(get(urlEqualTo(GET_DOCUMENTS_LIST_URL))
				.willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

		proxy.getDocumentList("1", "2");
	}

	@Test(expected = ThirdPartyBadResponseException.class)
	public void failToParseBadResponse() throws Exception {
		givenThat(get(urlEqualTo(GET_DOCUMENTS_LIST_URL))
				.willReturn(aResponse().withStatus(400)));

		proxy.getDocumentList("1", "2");
	}

	@Test(expected = ThirdPartyConnectivityFailureException.class)
	public void failToConnect() throws Exception {
		givenThat(get(urlEqualTo(GET_DOCUMENTS_LIST_URL))
				.willReturn(aResponse().withFixedDelay(1000)));

		proxy.getDocumentList("1", "2");
	}
}
