package com.chat.chattingserverapp.api;

import com.chat.chattingserverapp.utils.TestFixture;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

public class TestFixtureConfig {

  @Bean
  @Scope("prototype")
  public TestFixture textFixture(Environment environment) {
    return TestFixture.create(environment);
  }

}
