package com.chat.chattingserverapp.api.client.me;

import static com.chat.chattingserverapp.utils.PasswordGenerator.generatePassword;
import static com.chat.chattingserverapp.utils.UsernameGenerator.generateUsername;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.RequestEntity.*;

import com.chat.chattingserverapp.api.ChattingApiTest;
import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.query.IssueClientToken;
import com.chat.chattingserverapp.client.result.AccessTokenCarrier;
import com.chat.chattingserverapp.client.view.ClientMeView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

@ChattingApiTest
@DisplayName("GET /client/me")
public class GET_specs {

  @DisplayName("올바르게 요청하면 `200 OK` 상태코드를 반환한다")
  @Test
  void 올바르게_요청하면_200_OK_상태코드를_반환한다(
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    String username = generateUsername();
    String password = generatePassword();

    var command = new CreateClientCommand(username, password);
    client.postForEntity("/client/signup", command, Void.class);

    AccessTokenCarrier carrier = client.postForObject(
        "/client/issueToken",
        new IssueClientToken(username, password),
        AccessTokenCarrier.class
    );
    String token = carrier.accessToken();

    // Act
    ResponseEntity<ClientMeView> response = client.exchange(
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
      @Autowired TestRestTemplate client
  ){
    // Arrange
    
    // Act
    ResponseEntity<Void> response = client.getForEntity(
        "/client/me",
        Void.class);

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(401);
  }
  
  @DisplayName("서로 다른 사용자의 식별자는 서로 다르다")
  @Test
  void 서로_다른_사용자의_식별자는_서로_다르다(
      @Autowired TestRestTemplate client
  ){
    // Arrange
    String username1 = generateUsername();
    String password1 = generatePassword();
    var command1 = new CreateClientCommand(username1, password1);
    client.postForEntity("/client/signup", command1, Void.class);
    AccessTokenCarrier carrier1 = client.postForObject(
        "/client/issueToken",
        new IssueClientToken(username1, password1),
        AccessTokenCarrier.class
    );
    String token1 = carrier1.accessToken();

    String username2 = generateUsername();
    String password2 = generatePassword();
    var command2 = new CreateClientCommand(username2, password2);
    client.postForEntity("/client/signup", command2, Void.class);
    AccessTokenCarrier carrier2 = client.postForObject(
        "/client/issueToken",
        new IssueClientToken(username2, password2),
        AccessTokenCarrier.class
    );
    String token2 = carrier2.accessToken();
    
    // Act
    ResponseEntity<ClientMeView> response1 = client.exchange(
        get("/client/me")
            .header("Authorization", "Bearer " + token1)
            .build(),
        ClientMeView.class
    );

    ResponseEntity<ClientMeView> response2 = client.exchange(
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
      @Autowired TestRestTemplate client
  ){
    // Arrange
    String username = generateUsername();
    String password = generatePassword();
    var command = new CreateClientCommand(username, password);
    client.postForEntity("/client/signup", command, Void.class);
    AccessTokenCarrier carrier1 = client.postForObject(
        "/client/issueToken",
        new IssueClientToken(username, password),
        AccessTokenCarrier.class
    );
    String token1 = carrier1.accessToken();

    AccessTokenCarrier carrier2 = client.postForObject(
        "/client/issueToken",
        new IssueClientToken(username, password),
        AccessTokenCarrier.class
    );
    String token2 = carrier2.accessToken();


    
    // Act
    ResponseEntity<ClientMeView> response1 = client.exchange(
        get("/client/me")
            .header("Authorization", "Bearer " + token1)
            .build(),
        ClientMeView.class
    );

    ResponseEntity<ClientMeView> response2 = client.exchange(
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
      @Autowired TestRestTemplate client
  ){
    // Arrange
    String username = generateUsername();
    String password = generatePassword();
    var command = new CreateClientCommand(username, password);
    client.postForEntity("/client/signup", command, Void.class);
    AccessTokenCarrier carrier = client.postForObject(
        "/client/issueToken",
        new IssueClientToken(username, password),
        AccessTokenCarrier.class
    );
    String token = carrier.accessToken();

    // Act
    ResponseEntity<ClientMeView> response = client.exchange(
        get("/client/me")
            .header("Authorization", "Bearer " + token)
            .build(),
        ClientMeView.class
    );


    // Assert
    assertNotNull(response.getBody());
    ClientMeView actual = response.getBody();
    assertThat(actual.username()).isEqualTo(username);
  }
}
