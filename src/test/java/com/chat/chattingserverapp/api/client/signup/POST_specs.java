package com.chat.chattingserverapp.api.client.signup;

import static com.chat.chattingserverapp.utils.EmailGenerator.generateEmail;
import static com.chat.chattingserverapp.utils.PasswordGenerator.generatePassword;
import static com.chat.chattingserverapp.utils.UsernameGenerator.generateUsername;
import static org.assertj.core.api.Assertions.assertThat;

import com.chat.chattingserverapp.api.ChattingApiTest;
import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.domain.Client;
import com.chat.chattingserverapp.client.infrastructure.ClientRepository;
import com.chat.chattingserverapp.client.response.ClientResponse;
import com.chat.chattingserverapp.utils.TestFixture;
import java.time.LocalDateTime;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        generateEmail(), // Assuming email is generated here
        validUsername,
        generatePassword()
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
        generateEmail(), // Assuming email is generated here
        null,
        generatePassword()
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
        generateEmail(), // Assuming email is generated here
        invalidUsername,
        generatePassword()
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
        generateEmail(), // Assuming email is generated here
        generateUsername(),
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
        generateEmail(), // Assuming email is generated here
        generateUsername(),
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
    String username = generateUsername();
    client.postForEntity(
        "/client/signup",
        new CreateClientCommand(
            generateEmail(), // Assuming email is generated here
            username,
            generatePassword()
        ),
        Void.class
    );

    // Act
    var response = client.postForEntity(
        "/client/signup",
        new CreateClientCommand(
            generateEmail(), // Assuming email is generated here
            username,
            generatePassword()
        ),
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }

  @DisplayName("비밀번호를 올바르게 암호화한다")
  @Test
  void 비밀번호를_올바르게_암호화한다(
      @Autowired TestRestTemplate client,
      @Autowired ClientRepository repository,
  @Autowired PasswordEncoder encoder
  ){
    // Arrange
    var command = new CreateClientCommand(
        generateEmail(), // Assuming email is generated here
        generateUsername(),
        generatePassword()
    );

    client.postForEntity(
        "/client/signup",
        command,
        Void.class
    );

    // Act
    Client clientUser = repository.findByUsername(command.username())
        .orElseThrow(() -> new IllegalStateException("Client not found"));

    // Assert
    assertThat(clientUser.decodePassword(encoder, command.password()))
        .isTrue();
  }
  
  @DisplayName("사용자 ID를 반환한다")
  @Test
  void 사용자_ID를_반환한다(
      @Autowired TestRestTemplate client
  ){
    // Arrange
    var command = new CreateClientCommand(
        generateEmail(),
        generateUsername(),
        generatePassword()
    );
    
    // Act
    var response = client.postForEntity(
        "/client/signup",
        command,
        ClientResponse.class
    );
    
    // Assert
    assertThat(response.getBody()).isNotNull();
  }
  
  @DisplayName("사용자 이름을 반환한다")
  @Test
  void 사용자_이름을_반환한다(
      @Autowired TestRestTemplate client
  ){
    // Arrange
    var command = new CreateClientCommand(
        generateEmail(),
        generateUsername(),
        generatePassword()
    );
    
    // Act
    var response = client.postForEntity(
        "/client/signup",
        command,
        ClientResponse.class
    );
    
    // Assert
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().username()).isEqualTo(command.username());
  }
  
  @DisplayName("생성일시를 반환한다.")
  @Test
  void 생성일시를_반환한다(
      @Autowired TestRestTemplate client
  ){
    // Arrange
    var command = new CreateClientCommand(
        generateEmail(),
        generateUsername(),
        generatePassword()
    );
    
    // Act
    var response = client.postForEntity(
        "/client/signup",
        command,
        ClientResponse.class
    );
    
    // Assert
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().createdAt()).isNotNull();
    assertThat(response.getBody().createdAt()).isInstanceOf(LocalDateTime.class);
  }


  @DisplayName("이메일 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다")
  @Test
  void 이메일_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    String username = generateUsername();
    String password = generatePassword();

    // Act
    var response = client.postForEntity(
        "/client/signup",
        new CreateClientCommand(null, username, password), // 이메일이 null로 설정됨
        Void.class
    );

    // Assert
    AssertionsForClassTypes.assertThat(response.getStatusCode().value()).isEqualTo(400);
  }

  @DisplayName("이메일 속성이 올바른 형식을 따르지 않으면 `400 Bad Request` 상태코드를 반환한다")
  @ParameterizedTest
  @MethodSource("com.chat.chattingserverapp.utils.TestDataSource#invalidEmails")
  void 이메일_속성이_올바른_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
      String invalidEmail,
      @Autowired TestFixture fixture
  ){
    // Arrange
    String username = generateUsername();
    String password = generatePassword();

    var command = new CreateClientCommand(
        invalidEmail,
        username,
        password
    );

    // Act
    ResponseEntity<Void> response = fixture.client().postForEntity(
        "/client/signup",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }
  
  @DisplayName("이메일 속성이 중복되면 `400 Bad Request` 상태코드를 반환한다")
  @Test
  void 이메일_속성이_중복되면_400_Bad_Request_상태코드를_반환한다(
      @Autowired TestFixture fixture
  ){
    // Arrange
    String email = generateEmail();

    fixture.createClient(email,generateUsername(),generatePassword());
    var command = new CreateClientCommand(
        email,
        generateUsername(),
        generatePassword()
    );

    // Act
    ResponseEntity<Void> response = fixture.client().postForEntity(
        "/client/signup",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }
}
