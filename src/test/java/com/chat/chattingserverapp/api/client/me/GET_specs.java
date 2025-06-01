package com.chat.chattingserverapp.api.client.me;

import static com.chat.chattingserverapp.utils.EmailGenerator.generateEmail;
import static com.chat.chattingserverapp.utils.PasswordGenerator.generatePassword;
import static com.chat.chattingserverapp.utils.UsernameGenerator.generateUsername;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.RequestEntity.get;

import com.chat.chattingserverapp.api.ChattingApiTest;
import com.chat.chattingserverapp.client.view.ClientMeView;
import com.chat.chattingserverapp.utils.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@ChattingApiTest
@DisplayName("GET /client/me")
public class GET_specs {

  @DisplayName("올바르게 요청하면 `200 OK` 상태코드를 반환한다")
  @Test
  void 올바르게_요청하면_200_OK_상태코드를_반환한다(
      @Autowired TestFixture fixture
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    fixture.createClient(email, username, password);

    String token = fixture.issueClientToken(email, password);

    // Act
    ResponseEntity<ClientMeView> response = fixture.client().exchange(
        get("/client/me")
            .header("Authorization", "Bearer " + token)
            .build(),
        ClientMeView.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(200);
  }

  @DisplayName("접근 토큰을 사용하지 않으면 `401 Unauthorized` 상태코드를 반환한다")
  @Test
  void 접근_토큰을_사용하지_않으면_401_Unauthorized_상태코드를_반환한다(
      @Autowired TestFixture fixture
  ) {
    // Arrange

    // Act
    ResponseEntity<Void> response = fixture.client().getForEntity(
        "/client/me",
        Void.class);

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(401);
  }

  @DisplayName("서로 다른 사용자의 식별자는 서로 다르다")
  @Test
  void 서로_다른_사용자의_식별자는_서로_다르다(
      @Autowired TestFixture fixture
  ) {
    // Arrange
    String email1 = generateEmail();
    String username1 = generateUsername();
    String password1 = generatePassword();
    fixture.createClient(email1, username1, password1);

    String token1 = fixture.issueClientToken(email1, password1);

    String email2 = generateEmail();
    String username2 = generateUsername();
    String password2 = generatePassword();
    fixture.createClient(email2, username2, password2);
    String token2 = fixture.issueClientToken(email2, password2);

    // Act
    ResponseEntity<ClientMeView> response1 = fixture.client().exchange(
        get("/client/me")
            .header("Authorization", "Bearer " + token1)
            .build(),
        ClientMeView.class
    );

    ResponseEntity<ClientMeView> response2 = fixture.client().exchange(
        get("/client/me")
            .header("Authorization", "Bearer " + token2)
            .build(),
        ClientMeView.class
    );

    // Assert
    assertNotNull(response1.getBody());
    assertNotNull(response2.getBody());
    assertThat(response1.getBody().id()).isNotEqualTo(response2.getBody().id());
  }

  @DisplayName("같은 사용자의 식별자는 항상 같다")
  @Test
  void 같은_사용자의_식별자는_항상_같다(
      @Autowired TestFixture fixture
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();

    fixture.createClient(email, username, password);
    String token1 = fixture.issueClientToken(email, password);

    String token2 = fixture.issueClientToken(email, password);

    // Act
    ResponseEntity<ClientMeView> response1 = fixture.client().exchange(
        get("/client/me")
            .header("Authorization", "Bearer " + token1)
            .build(),
        ClientMeView.class
    );

    ResponseEntity<ClientMeView> response2 = fixture.client().exchange(
        get("/client/me")
            .header("Authorization", "Bearer " + token2)
            .build(),
        ClientMeView.class
    );

    // Assert
    assertNotNull(response1.getBody());
    assertNotNull(response2.getBody());
    assertThat(response1.getBody().id()).isEqualTo(response2.getBody().id());
  }

  @DisplayName("사용자의 기본 정보가 올바르게 설정된다")
  @Test
  void 사용자의_기본_정보가_올바르게_설정된다(
      @Autowired TestFixture fixture
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    fixture.createClient(email, username, password);
    fixture.setClientAsDefaultUser(email, password);

    // Act
    ResponseEntity<ClientMeView> response = fixture.client()
        .getForEntity("/client/me", ClientMeView.class);

    // Assert
    assertNotNull(response.getBody());
    ClientMeView actual = response.getBody();
    assertThat(actual.username()).isEqualTo(username);
  }
}
