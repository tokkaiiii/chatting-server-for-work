package com.chat.chattingserverapp.api.client.login;

import static com.chat.chattingserverapp.utils.EmailGenerator.generateEmail;
import static com.chat.chattingserverapp.utils.PasswordGenerator.generatePassword;
import static com.chat.chattingserverapp.utils.UsernameGenerator.generateUsername;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.chat.chattingserverapp.api.ChattingApiTest;
import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.command.LoginClientCommand;
import com.chat.chattingserverapp.client.domain.Client;
import com.chat.chattingserverapp.utils.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@ChattingApiTest
@DisplayName("POST /client/login")
class POST_specs {


  @DisplayName("올바르게 요청하면 `200 OK` 상태코드를 반환한다")
  @Test
  void 올바르게_요청하면_200_OK_상태코드를_반환한다(
      @Autowired TestFixture fixture
  ) {

    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();

    fixture.createClient(email, username, password);
    // Arrange
    var command = new LoginClientCommand(
        email,
        password
    );

    // Act
    var response = fixture.client().postForEntity(
        "/client/login",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(200);
  }


  @DisplayName("이메일 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다")
  @Test
  void 이메일_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
      @Autowired TestFixture fixture
  ) {
    // Arrange
    var command = new LoginClientCommand(
        null,
        generatePassword()
    );

    // Act
    ResponseEntity<Void> response = fixture.client().postForEntity(
        "/client/login",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }

  @DisplayName("이메일 속성이 올바른 형식을 따르지 않으면 `400 Bad Request` 상태코드를 반환한다")
  @ParameterizedTest
  @MethodSource("com.chat.chattingserverapp.utils.TestDataSource#invalidEmails")
  void 이메일_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
      String invalidEmail,
      @Autowired TestFixture fixture
  ) {
    // Arrange
    var command = new LoginClientCommand(
        invalidEmail,
        generatePassword()
    );

    // Act
    ResponseEntity<Void> response = fixture.client().postForEntity(
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
      @Autowired TestFixture fixture
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();

    fixture.createClient(email, username, password);

    var command = new LoginClientCommand(
        email,
        null
    );

    // Act
    var response = fixture.client().postForEntity(
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
      @Autowired TestFixture fixture
  ) {
    // Arrange

    var command = new LoginClientCommand(
        generateEmail(),
        password
    );

    // Act
    var response = fixture.client().postForEntity(
        "/client/login",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }


  @DisplayName("비밀번호가 일치하지 않으면 `401 Unauthorized` 상태코드를 반환한다")
  @Test
  void 비밀번호가_일치하지_않으면_401_Unauthorized_상태코드를_반환한다(
      @Autowired TestFixture fixture
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    String wrongPassword = generatePassword();
    fixture.createClient(email, username, password);

    // Act
    var command = new LoginClientCommand(
        generateEmail(),
        wrongPassword
    );

    var response = fixture.client().postForEntity(
        "/client/login",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(401);
  }


  @DisplayName("로그인 시 사용자 이름을 반환한다")
  @Test
  void 로그인_시_사용자_이름을_반환한다(
      @Autowired TestFixture fixture
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    fixture.createClient(email, username, password);

    var command = new LoginClientCommand(
        email,
        password
    );

    // Act
    var response = fixture.client().postForEntity(
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
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    client.postForEntity(
        "/client/signup",
        new CreateClientCommand(email, username, password),
        Void.class
    );
    var command = new LoginClientCommand(
        email,
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
