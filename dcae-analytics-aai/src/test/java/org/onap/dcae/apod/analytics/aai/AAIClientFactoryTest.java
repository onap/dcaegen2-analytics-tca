package org.onap.dcae.apod.analytics.aai;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;


public class AAIClientFactoryTest {

	

	@Test
	public void testCreate_shouldReturn_NotNull() {
		AAIClientFactory result = AAIClientFactory.create();
		assertNotNull(result);

	}

}
