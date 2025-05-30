package com.chat.chattingserverapp.api.client.login;

import static com.chat.chattingserverapp.utils.PasswordGenerator.generatePassword;
import static com.chat.chattingserverapp.utils.UsernameGenerator.generateUsername;
import static java.util.Objects.*;
import static java.util.Optional.of;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

import com.chat.chattingserverapp.api.ChattingApiTest;
import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.command.LoginClientCommand;
import com.chat.chattingserverapp.client.domain.Client;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

@ChattingApiTest
@DisplayName("POST /client/login")
class POST_specs {


  @DisplayName("올바르게 요청하면 `200 OK` 상태코드를 반환한다")
  @Test
  void 올바르게_요청하면_200_OK_상태코드를_반환한다(
      @Autowired TestRestTemplate client
  ) {

    String username = generateUsername();
    String password = generatePassword();

    client.postForEntity(
        "/client/signup",
        new CreateClientCommand(username, password),
        Void.class
    );
    // Arrange
    var command = new LoginClientCommand(
        username,
        password
    );

    // Act
    var response = client.postForEntity(
        "/client/login",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(200);
  }

  @DisplayName("사용자 이름 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다")
  @Test
  void 사용자_이름_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    var command = new LoginClientCommand(
        null,
        generatePassword()
    );

    // Act
    var response = client.postForEntity(
        "/client/login",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }

  @DisplayName("사용자 이름 속성이 올바른 형식을 따르지 않으면 `400 Bad Request` 상태코드를 반환한다")
  @ParameterizedTest
  @MethodSource("com.chat.chattingserverapp.utils.TestDataSource#invalidUsernames")
  void 사용자_이름_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
      String username,
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    var command = new LoginClientCommand(
        username,
        generatePassword()
    );

    // Act
    var response = client.postForEntity(
        "/client/login",
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
    var command = new LoginClientCommand(
        generateUsername(),
        null
    );

    // Act
    var response = client.postForEntity(
        "/client/login",
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
      String password,
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    var command = new LoginClientCommand(
        generateUsername(),
        password
    );

    // Act
    var response = client.postForEntity(
        "/client/login",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }

  @DisplayName("사용자 이름이 존재하지 않으면 `401 Unauthorized` 상태코드를 반환한다")
  @Test
  void 사용자_이름이_존재하지_않으면_401_Unauthorized_상태코드를_반환한다(
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    String username = generateUsername();
    String password = generatePassword();

    client.postForEntity(
        "/client/signup",
        new CreateClientCommand(username, password),
        Void.class
    );

    var command = new LoginClientCommand(
        "잘못된" + username,
        password
    );

    // Act
    var response = client.postForEntity(
        "/client/login",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(401);
  }
  
  @DisplayName("비밀번호가 일치하지 않으면 `401 Unauthorized` 상태코드를 반환한다")
  @Test
  void 비밀번호가_일치하지_않으면_401_Unauthorized_상태코드를_반환한다(
      @Autowired TestRestTemplate client,
      @Autowired PasswordEncoder passwordEncoder
  ){
    // Arrange
    String username = generateUsername();
    String password = generatePassword();
    String wrongPassword = generatePassword();
    client.postForEntity(
        "/client/signup",
        new CreateClientCommand(username, password),
        Void.class
    );
    
    // Act
    var command = new LoginClientCommand(
        username,
        wrongPassword
    );

    var response = client.postForEntity(
        "/client/login",
        command,
        Void.class
    );
    
    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(401);
  }
  
  @DisplayName("로그인 시 사용자 ID를 반환한다")
  @Test
  void 로그인_시_사용자_ID를_반환한다(
      @Autowired TestRestTemplate client
  ){
    // Arrange
    String username = generateUsername();
    String password = generatePassword();
    client.postForEntity(
        "/client/signup",
        new CreateClientCommand(username, password),
        Void.class
    );

    var command = new LoginClientCommand(
        username,
        password
    );

    // Act
    var response = client.postForEntity(
        "/client/login",
        command,
        Client.class
    );

    // Assert
    assertThat(response.getBody()).isNotNull();
    var actual = requireNonNull(response.getBody()).getId();
    assertThat(actual).isNotNull();
  }
  
  @DisplayName("로그인 시 사용자 이름을 반환한다")
  @Test
  void 로그인_시_사용자_이름을_반환한다(
      @Autowired TestRestTemplate client
  ){
    // Arrange
    String username = generateUsername();
    String password = generatePassword();
    client.postForEntity(
        "/client/signup",
        new CreateClientCommand(username, password),
        Void.class
    );
    var command = new LoginClientCommand(
        username,
        password
    );

    // Act
    var response = client.postForEntity(
        "/client/login",
        command,
        Client.class
    );
    
    // Assert
    assertThat(response.getBody()).isNotNull();
    var actual = requireNonNull(response.getBody()).getUsername();
    assertThat(actual).isEqualTo(username);
  }

  @DisplayName("로그인 시 생성일시를 반환한다")
  @Test
  void 로그인_시_생성일시를_반환한다(
      @Autowired TestRestTemplate client
  ){
    // Arrange
    String username = generateUsername();
    String password = generatePassword();
    client.postForEntity(
        "/client/signup",
        new CreateClientCommand(username, password),
        Void.class
    );
    var command = new LoginClientCommand(
        username,
        password
    );

    // Act
    var response = client.postForEntity(
        "/client/login",
        command,
        Client.class
    );

    // Assert
    assertThat(response.getBody()).isNotNull();
    var actual = requireNonNull(response.getBody()).getCreatedAt();
    assertThat(actual).isNotNull();
  }
}
