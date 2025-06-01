package com.chat.chattingserverapp.api;

import com.chat.chattingserverapp.utils.TestFixture;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;

public class TestFixtureConfig {

  @Bean
  public TestFixture textFixture(TestRestTemplate client) {
    return new TestFixture(client);
  }

}
