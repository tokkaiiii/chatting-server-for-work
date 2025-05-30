package com.chat.chattingserverapp.api.client.signup;

import static com.chat.chattingserverapp.utils.PasswordGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.chat.chattingserverapp.api.ChattingApiTest;
import com.chat.chattingserverapp.client.command.CreateClientCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

@ChattingApiTest
@DisplayName("POST /client/signup")
class POST_specs {

  @DisplayName("올바르게 요청하면 `201 Created` 상태코드를 반환한다")
  @ParameterizedTest
  @MethodSource("com.chat.chattingserverapp.utils.TestDataSource#validUsernames")
  void 올바르게_요청하면_201_Created_상태코드를_반환한다(
      String validUsername,
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    var command = new CreateClientCommand(
        validUsername,
        "password"
    );

    // Act
    var response = client.postForEntity(
        "/client/signup",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(201);
  }

  @DisplayName("사용자 이름 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다")
  @Test
  void 사용자_이름_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    var command = new CreateClientCommand(
        null,
        "password"
    );

    // Act
    var response = client.postForEntity(
        "/client/signup",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }

  @DisplayName("사용자 이름 속성이 올바른 형식을 따르지 않으면 `400 Bad Request` 상태코드를 반환한다.")
  @ParameterizedTest
  @MethodSource("com.chat.chattingserverapp.utils.TestDataSource#invalidUsernames")
  void 사용자_이름_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
      String invalidUsername,
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    var command = new CreateClientCommand(
        invalidUsername,
        "password"
    );

    // Act
    var response = client.postForEntity(
        "/client/signup",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }

  @DisplayName("비밀번호 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다")
  @Test
  void 비밀번호_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    var command = new CreateClientCommand(
        "유저이름",
        null
    );

    // Act
    var response = client.postForEntity(
        "/client/signup",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }

  @DisplayName("비밀번호 속성이 올바른 형식을 따르지 않으면 `400 Bad Request` 상태코드를 반환한다")
  @ParameterizedTest
  @MethodSource("com.chat.chattingserverapp.utils.TestDataSource#invalidPasswords")
  void 비밀번호_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
      String invalidPassword,
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    var command = new CreateClientCommand(
        "유저이름",
        invalidPassword
    );

    // Act
    var response = client.postForEntity(
        "/client/signup",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }

  @DisplayName("사용자 이름 속성이 중복되면 `400 Bad Request` 상태코드를 반환한다")
  @Test
  void 사용자_이름_속성이_중복되면_400_Bad_Request_상태코드를_반환한다(
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    String username = "유저이름";
    client.postForEntity(
        "/client/signup",
        new CreateClientCommand(
            username,
            generatePassword()
        ),
        Void.class
    );

    // Act
    var response = client.postForEntity(
        "/client/signup",
        new CreateClientCommand(
            username,
            generatePassword()
        ),
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }
}
