package com.chat.chattingserverapp;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Base64;
import org.assertj.core.api.ThrowingConsumer;

public class JwtAssertions {

  public static ThrowingConsumer<String> conformsToJwtFormat() {
    return s -> {
      String[] parts = s.split("\\.");
      assertThat(parts).hasSize(3);
      assertThat(parts[0]).matches(JwtAssertions::isBase64UrlEncodedJson);
      assertThat(parts[1]).matches(JwtAssertions::isBase64UrlEncodedJson);
      assertThat(parts[2]).matches(JwtAssertions::isBase64UrlEncoded);
    };
  }

  private static boolean isBase64UrlEncodedJson(String s) {
    try {
      new ObjectMapper().readTree(Base64.getUrlDecoder().decode(s));
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  private static boolean isBase64UrlEncoded(String s) {
    try {
      Base64.getUrlDecoder().decode(s);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
