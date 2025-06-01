package com.chat.chattingserverapp.api.client.issuetoken;

import static com.chat.chattingserverapp.JwtAssertions.conformsToJwtFormat;
import static com.chat.chattingserverapp.utils.EmailGenerator.generateEmail;
import static com.chat.chattingserverapp.utils.PasswordGenerator.generatePassword;
import static com.chat.chattingserverapp.utils.UsernameGenerator.generateUsername;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.chat.chattingserverapp.api.ChattingApiTest;
import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.query.IssueClientToken;
import com.chat.chattingserverapp.client.result.AccessTokenCarrier;
import com.chat.chattingserverapp.utils.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

@ChattingApiTest
@DisplayName("POST /client/issueToken")
public class POST_specs {

  @DisplayName("올바르게 요청하면 200 OK 상태코드와 접근 토큰을 반환한다")
  @Test
  void 올바르게_요청하면_200_OK_상태코드와_접근_토큰을_반환한다(
      @Autowired TestFixture fixture
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    fixture.createClient(email, username, password);

    // Act
    var response = fixture.client().postForEntity(
        "/client/issueToken",
        new IssueClientToken(email, password),
        AccessTokenCarrier.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(200);
    assertThat(response.getBody()).isNotNull();
    var actual = requireNonNull(response.getBody()).accessToken();
    assertThat(actual).isNotNull();
  }

  @DisplayName("접근 토큰은 JWT 형식을 따른다")
  @Test
  void 접근_토큰은_JWT_형식을_따른다(
      @Autowired TestFixture fixture
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    fixture.createClient(email, username, password);

    // Act
    var response = fixture.client().postForEntity(
        "/client/issueToken",
        new IssueClientToken(email, password),
        AccessTokenCarrier.class
    );

    // Assert
    String actual = requireNonNull(response.getBody()).accessToken();
    assertThat(actual).satisfies(conformsToJwtFormat());
  }

  @DisplayName("존재하지 않는 사용자명이 사용되면 400 Bad Request 상태코드를 반환한다")
  @Test
  void 존재하지_않는_사용자명이_사용되면_400_Bad_Request_상태코드를_반환한다(
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    String username = generateUsername();
    String password = generatePassword();

    // Act
    var response = client.postForEntity(
        "/client/issueToken",
        new IssueClientToken(username, password),
        AccessTokenCarrier.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }

  @DisplayName("잘못된 비밀번호가 사용되면 400 Bad Request 상태코드를 반환한다")
  @Test
  void 잘못된_비밀번호가_사용되면_400_Bad_Request_상태코드를_반환한다(
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    String wrongPassword = generatePassword();

    client.postForEntity(
        "/client/signup",
        new CreateClientCommand(email, username, password),
        Void.class
    );

    // Act
    var response = client.postForEntity(
        "/client/issueToken",
        new IssueClientToken(username, wrongPassword),
        AccessTokenCarrier.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }
}
