package com.chat.chattingserverapp.api.chat.create;

import static com.chat.chattingserverapp.utils.PasswordGenerator.generatePassword;
import static com.chat.chattingserverapp.utils.UsernameGenerator.generateUsername;
import static java.util.Objects.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.RequestEntity.*;

import com.chat.chattingserverapp.api.ChattingApiTest;
import com.chat.chattingserverapp.chat.command.ChatRoomCreateCommand;
import com.chat.chattingserverapp.chat.response.ChatRoomResponse;
import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.query.IssueClientToken;
import com.chat.chattingserverapp.client.result.AccessTokenCarrier;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

@ChattingApiTest
@DisplayName("POST /chat/rooms")
public class POST_specs {

  @DisplayName("올바르게 요청하면 `201 Created` 상태코드를 반환한다")
  @Test
  void 올바르게_요청하면_201_Created_상태코드를_반환한다(
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    String username = generateUsername();
    String password = generatePassword();
    String roomName = username + "'s Room";

    client.postForEntity("/client/signup", new CreateClientCommand(username, password), Void.class);

    AccessTokenCarrier carrier = client.postForObject(
        "/client/issueToken",
        new IssueClientToken(username, password),
        AccessTokenCarrier.class
    );
    String token = carrier.accessToken();

    ChatRoomCreateCommand command = new ChatRoomCreateCommand(roomName);

    // Act
    var response = client.exchange(
        post("/chat/rooms")
            .header("Authorization", " Bearer " + token)
            .body(command),
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(201);
  }

  @DisplayName("접근 토큰을 사용하지 않으면 `401 Unauthorized` 상태코드를 반환한다")
  @Test
  void 접근_토큰을_사용하지_않으면_401_Unauthorized_상태코드를_반환한다(
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    String roomName = "Test Room";
    ChatRoomCreateCommand command = new ChatRoomCreateCommand(roomName);

    // Act
    var response = client.postForEntity(
        "/chat/rooms",
        command,
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(401);
  }

  @DisplayName("채팅방 이름 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다")
  @Test
  void 채팅방_이름_속성이_지정되지_않으면_400_Bad_Request_상태코드를_반환한다(
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    String username = generateUsername();
    String password = generatePassword();
    client.postForEntity("/client/signup", new CreateClientCommand(username, password), Void.class);
    AccessTokenCarrier carrier = client.postForObject(
        "/client/issueToken",
        new IssueClientToken(username, password),
        AccessTokenCarrier.class
    );
    String token = carrier.accessToken();

    ChatRoomCreateCommand command = new ChatRoomCreateCommand(null); // 채팅방 이름이 null로 설정됨

    // Act
    var response = client.exchange(
        post("/chat/rooms")
            .header("Authorization", " Bearer " + token)
            .body(command),
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }

  @DisplayName("채팅방 이름 속성이 중복되면 `400 Bad Request` 상태코드를 반환한다")
  @Test
  void 채팅방_이름_속성이_중복되면_400_Bad_Request_상태코드를_반환한다(
      @Autowired TestRestTemplate client
  ) {
    // Arrange
    String username = generateUsername();
    String password = generatePassword();
    client.postForEntity("/client/signup", new CreateClientCommand(username, password), Void.class);
    AccessTokenCarrier carrier = client.postForObject(
        "/client/issueToken",
        new IssueClientToken(username, password),
        AccessTokenCarrier.class
    );
    String token = carrier.accessToken();

    String roomName = username + "'s Room";
    ChatRoomCreateCommand command = new ChatRoomCreateCommand(roomName);
    client.exchange(
        post("/chat/rooms")
            .header("Authorization", " Bearer " + token)
            .body(command),
        Void.class
    );

    // Act
    var response = client.exchange(
        post("/chat/rooms")
            .header("Authorization", " Bearer " + token)
            .body(command),
        Void.class
    );

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(400);
  }
  
  @DisplayName("채팅방 이름을 반환한다")
  @Test
  void 채팅방_이름을_반환한다(
      @Autowired TestRestTemplate client
  ){
    // Arrange
    String username = generateUsername();
    String password = generatePassword();
    client.postForEntity("/client/signup", new CreateClientCommand(username, password), Void.class);
    AccessTokenCarrier carrier = client.postForObject(
        "/client/issueToken",
        new IssueClientToken(username, password),
        AccessTokenCarrier.class
    );
    String token = carrier.accessToken();

    String roomName = username + "'s Room";
    ChatRoomCreateCommand command = new ChatRoomCreateCommand(roomName);
    
    // Act
    var response = client.exchange(
        post("/chat/rooms")
            .header("Authorization", " Bearer " + token)
            .body(command),
        ChatRoomResponse.class
    );

    // Assert
    String actual = requireNonNull(response.getBody()).roomName();
    assertThat(actual).isEqualTo(roomName);
  }
}
