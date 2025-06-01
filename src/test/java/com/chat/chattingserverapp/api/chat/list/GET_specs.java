package com.chat.chattingserverapp.api.chat.list;

import static com.chat.chattingserverapp.utils.EmailGenerator.generateEmail;
import static com.chat.chattingserverapp.utils.PasswordGenerator.generatePassword;
import static com.chat.chattingserverapp.utils.UsernameGenerator.generateUsername;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpMethod.*;

import com.chat.chattingserverapp.api.ChattingApiTest;
import com.chat.chattingserverapp.chat.response.ChatRoomListResponse;
import com.chat.chattingserverapp.utils.TestFixture;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@ChattingApiTest
@DisplayName("GET /chat/rooms")
public class GET_specs {

  @DisplayName("올바르게 요청하면 `200 ok` 상태코드를 반환한다")
  @Test
  void 올바르게_요청하면_200_ok_상태코드를_반환한다(
      @Autowired TestFixture fixture
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    fixture.createClient(email, username, password);
    fixture.setClientAsDefaultUser(email, password);

    // Act
    ResponseEntity<Void> response = fixture.client().getForEntity("/chat/rooms", Void.class);

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(200);
  }

  @DisplayName("접근 토큰을 사용하지 않으면 `401 Unauthorized` 상태코드를 반환한다")
  @Test
  void 접근_토큰을_사용하지_않으면_401_Unauthorized_상태코드를_반환한다(
      @Autowired TestFixture fixture
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    fixture.createClient(email, username, password);

    // Act
    ResponseEntity<Void> response = fixture.client().getForEntity("/chat/rooms", Void.class);

    // Assert
    assertThat(response.getStatusCode().value()).isEqualTo(401);
  }

  @DisplayName("채티방 목록을 반환한다")
  @Test
  void 채티방_목록을_반환한다(
      @Autowired TestFixture fixture
  ) {
    // Arrange
    String email = generateEmail();
    String username = generateUsername();
    String password = generatePassword();
    fixture.createClient(email, username, password);
    fixture.setClientAsDefaultUser(email, password);
    fixture.createChatRoom(username);

    // Act
    ResponseEntity<List<ChatRoomListResponse>> response = fixture.client().exchange(
        "/chat/rooms",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<ChatRoomListResponse>>() {}
    );

    // Assert
    assertThat(response.getBody()).isNotNull();

    System.out.println(response.getBody());
  }
}
