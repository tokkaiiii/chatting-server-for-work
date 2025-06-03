package com.chat.chattingserverapp.api.admin.issuetoken;

import static com.chat.chattingserverapp.JwtAssertions.conformsToJwtFormat;
import static com.chat.chattingserverapp.utils.EmailGenerator.generateEmail;
import static com.chat.chattingserverapp.utils.PasswordGenerator.generatePassword;
import static com.chat.chattingserverapp.utils.UsernameGenerator.generateUsername;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

import com.chat.chattingserverapp.admin.domain.Admin;
import com.chat.chattingserverapp.admin.infrastructure.AdminRepositoryAdapter;
import com.chat.chattingserverapp.api.ChattingApiTest;
import com.chat.chattingserverapp.client.query.IssueClientToken;
import com.chat.chattingserverapp.client.result.AccessTokenCarrier;
import com.chat.chattingserverapp.utils.PasswordGenerator;
import com.chat.chattingserverapp.utils.TestFixture;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ChattingApiTest
@DisplayName("POST /admin/issueToken")
public class POST_specs {


  @MockitoBean
  private AdminRepositoryAdapter adminRepository;

  @AfterEach
  void tearDown() {
  }

  @DisplayName("올바르게 요청하면 `200 OK` 상태코드를 반환한다")
  @Test
  void 올바르게_요청하면_200_OK_상태코드를_반환한다(
      @Autowired TestFixture fixture,
      @Autowired PasswordEncoder encoder
  ) {
    // Arrange
    String email = generateEmail();
    String password = generatePassword();
    String hashedPassword = encoder.encode(password);
    given(adminRepository.findByEmail(email)).willReturn(
        Optional.of(Admin.of(UUID.randomUUID(), email, generateUsername(), hashedPassword)));
    var command = new IssueClientToken(
        email,
        password
    );

    // Act
    var response = fixture.client().postForEntity(
        "/admin/issueToken",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(200);
  }

  @DisplayName("올바르게 요청하면 접근 토큰을 반환한다")
  @Test
  void 올바르게_요청하면_접근_토큰을_반환한다(
      @Autowired TestFixture fixture,
      @Autowired PasswordEncoder encoder
  ) {
    String email = generateEmail();
    String password = generatePassword();
    String hashedPassword = encoder.encode(password);
    given(adminRepository.findByEmail(email)).willReturn(Optional.of(
        Admin.of(UUID.randomUUID(), email, generateUsername(), hashedPassword)));
    // Arrange
    var command = new IssueClientToken(
        email,
        password
    );

    // Act
    var response = fixture.client().postForEntity(
        "/admin/issueToken",
        command,
        AccessTokenCarrier.class
    );

    // Assert
    String actual = requireNonNull(response.getBody()).accessToken();
    assertThat(actual).isNotNull();
  }

  @DisplayName("접근 토큰은 JWT 형식을 따른다")
  @Test
  void 접근_토큰은_JWT_형식을_따른다(
      @Autowired TestFixture fixture,
      @Autowired PasswordEncoder encoder
  ) {
    // Arrange
    String email = generateEmail();
    String password = generatePassword();
    String hashedPassword = encoder.encode(password);
    given(adminRepository.findByEmail(email)).willReturn(Optional.of(Admin.of(UUID.randomUUID(),
        email, generateUsername(), hashedPassword)));

    var command = new IssueClientToken(
        email,
        password
    );

    // Act
    var response = fixture.client().postForEntity(
        "/admin/issueToken",
        command,
        AccessTokenCarrier.class
    );

    // Assert
    String actual = requireNonNull(response.getBody()).accessToken();
    assertThat(actual).satisfies(conformsToJwtFormat());
  }

  @DisplayName("존재하지 않는 이메일이 사용되면 `400 Bad Request` 상태코드를 반환한다")
  @Test
  void 존재하지_않는_이메일이_사용되면_400_Bad_Request_상태코드를_반환한다(
      @Autowired TestFixture fixture,
      @Autowired PasswordEncoder encoder
  ) {
    String password = generatePassword();
    String hashedPassword = encoder.encode(password);
    given(adminRepository.findByEmail(generateEmail())).willReturn(
        Optional.of(Admin.of(UUID.randomUUID(), generateEmail(), generateUsername(),
            hashedPassword)));
    // Arrange
    var command = new IssueClientToken(
        generateEmail(),
        password
    );

    // Act
    ResponseEntity<AccessTokenCarrier> response = fixture.client()
        .postForEntity(
            "/admin/issueToken",
            command,
            AccessTokenCarrier.class
        );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }

  @DisplayName("잘못된 비밀번호가 사용되면 `400 Bad Request` 상태코드를 반환한다")
  @Test
  void 잘못된_비밀번호가_사용되면_400_Bad_Request_상태코드를_반환한다(
      @Autowired TestFixture fixture,
      @Autowired PasswordEncoder encoder
  ) {
    String password = generatePassword();
    String wrongPassword = generatePassword();
    String hashedPassword = encoder.encode(password);
    String email = generateEmail();

    given(adminRepository.findByEmail(email)).willReturn(
        Optional.of(Admin.of(UUID.randomUUID(), email, generateUsername(),
            hashedPassword)));
    // Arrange
    var command = new IssueClientToken(
        email,
        wrongPassword
    );

    // Act
    ResponseEntity<AccessTokenCarrier> response = fixture.client()
        .postForEntity(
            "/admin/issueToken",
            command,
            AccessTokenCarrier.class
        );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }
}
