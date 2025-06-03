package com.chat.chattingserverapp.api.admin.login;

import static com.chat.chattingserverapp.utils.EmailGenerator.generateEmail;
import static com.chat.chattingserverapp.utils.PasswordGenerator.generatePassword;
import static com.chat.chattingserverapp.utils.UsernameGenerator.generateUsername;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

import com.chat.chattingserverapp.admin.command.LoginAdminCommand;
import com.chat.chattingserverapp.admin.domain.Admin;
import com.chat.chattingserverapp.admin.infrastructure.AdminRepository;
import com.chat.chattingserverapp.admin.response.AdminResponse;
import com.chat.chattingserverapp.api.ChattingApiTest;
import com.chat.chattingserverapp.utils.TestFixture;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ChattingApiTest
@DisplayName("POST /admin/login")
public class POST_specs {

  @MockitoBean
  private AdminRepository adminRepository;

  @DisplayName("올바르게 요청하면 `200 OK` 상태코드를 반환한다")
  @Test
  void 올바르게_요청하면_200_OK_상태코드를_반환한다(
      @Autowired TestFixture fixture,
      @Autowired PasswordEncoder encoder
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    String hashedPassword = encoder.encode(password);
    var admin = Admin.of(UUID.randomUUID(), email, username, hashedPassword);
    given(adminRepository.findByEmail(email)).willReturn(of(admin));

    // Act
    ResponseEntity<Void> response = fixture.client().postForEntity(
        "/admin/login",
        new LoginAdminCommand(email, password),
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
    String password = generatePassword();
    var command = new LoginAdminCommand(
        null,
        password
    );

    // Act
    ResponseEntity<Void> response = fixture.client().postForEntity(
        "/admin/login",
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
    String password = generatePassword();

    // Act
    ResponseEntity<Void> response = fixture.client().postForEntity(
        "/admin/login",
        new LoginAdminCommand(invalidEmail, password),
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
    var command = new LoginAdminCommand(
        generateEmail(),
        null
    );

    // Act
    ResponseEntity<Void> response = fixture.client().postForEntity(
        "/admin/login",
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
      @Autowired TestFixture fixture
  ) {
    // Arrange
    var command = new LoginAdminCommand(
        generateEmail(),
        invalidPassword
    );

    // Act
    ResponseEntity<Void> response = fixture.client().postForEntity(
        "/admin/login",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }

  @DisplayName("비밀번호가 일치하지 않으면 `401 Unauthorized` 상태코드를 반환한다")
  @Test
  void 비밀번호가_일치하지_않으면_401_Unauthorized_상태코드를_반환한다(
      @Autowired TestFixture fixture,
      @Autowired PasswordEncoder encoder
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    String wrongPassword = generatePassword();
    String hashedPassword = encoder.encode(password);
    var admin = Admin.of(UUID.randomUUID(), email, username, hashedPassword);
    given(adminRepository.findByEmail(email)).willReturn(of(admin));

    // Act
    ResponseEntity<AdminResponse> response = fixture.client().postForEntity(
        "/admin/login",
        new LoginAdminCommand(email, wrongPassword),
        AdminResponse.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(401);
  }
  
  @DisplayName("로그인 시 사용자 이름을 반환한다")
  @Test
  void 로그인_시_사용자_이름을_반환한다(
      @Autowired TestFixture fixture,
      @Autowired PasswordEncoder encoder
  ){
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    String hashedPassword = encoder.encode(password);
    var admin = Admin.of(UUID.randomUUID(), email, username, hashedPassword);
    given(adminRepository.findByEmail(email)).willReturn(of(admin));

    // Act
    ResponseEntity<AdminResponse> response = fixture.client().postForEntity(
        "/admin/login",
        new LoginAdminCommand(email, password),
        AdminResponse.class
    );

    // Assert
    String actual = requireNonNull(response.getBody()).username();
    assertThat(actual).isEqualTo(username);
  }

  @DisplayName("로그인 시 생성일시를 반환한다")
  @Test
  void 로그인_시_생성일시를_반환한다(
      @Autowired TestFixture fixture,
      @Autowired PasswordEncoder encoder
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    String hashedPassword = encoder.encode(password);
    var admin = Admin.of(UUID.randomUUID(), email, username, hashedPassword);
    given(adminRepository.findByEmail(email)).willReturn(of(admin));

    // Act
    ResponseEntity<AdminResponse> response = fixture.client().postForEntity(
        "/admin/login",
        new LoginAdminCommand(email, password),
        AdminResponse.class
    );

    // Assert
    var actual = requireNonNull(response.getBody()).createdAt();
    assertThat(actual).isInstanceOf(LocalDateTime.class);
  }
}
