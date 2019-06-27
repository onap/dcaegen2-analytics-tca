package org.onap.dcae.apod.analytics.aai;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class AAIClientFactoryTest {

@Test
public void testCreate_shouldReturn_NotNull() {
    AAIClientFactory result = AAIClientFactory.create();
    assertNotNull(result);
    }
}
