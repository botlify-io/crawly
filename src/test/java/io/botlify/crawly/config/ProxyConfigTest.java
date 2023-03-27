package io.botlify.crawly.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProxyConfigTest {

    @Test
    void testBuildOnlyHostAndPort() {
        ProxyConfig config = new ProxyConfig.builder()
                .hostAndPort("localhost", 8080)
                .build();
        assertEquals("localhost", config.getHost());
        assertEquals(8080, config.getPort());
    }

    @Test
    void testBuildOnlyHostAndPortWithScheme() {
        ProxyConfig config = new ProxyConfig.builder()
                .hostAndPort("localhost", 8080)
                .usernameAndPassword("user", "password")
                .build();
        assertEquals("localhost", config.getHost());
        assertEquals(8080, config.getPort());
        assertEquals("user", config.getUsername());
        assertEquals("password", config.getPassword());
    }

    @Test
    void testHasValidCredentials() {
        ProxyConfig config = new ProxyConfig.builder()
                .hostAndPort("localhost", 8080)
                .usernameAndPassword("user", "password")
                .build();
        assertTrue(config.hasValidCredentials());

        config = new ProxyConfig.builder()
                .hostAndPort("localhost", 8080)
                .build();
        assertFalse(config.hasValidCredentials());
    }

}