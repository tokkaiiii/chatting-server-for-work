package com.chat.chattingserverapp.client.response;

import com.chat.chattingserverapp.client.domain.Client;
import java.time.LocalDateTime;
import java.util.UUID;

public record ClientResponse(
    UUID id,
    String email,
    String username,
    LocalDateTime createdAt
) {

  public ClientResponse {
    if (id == null || email == null || username == null || createdAt == null) {
      throw new IllegalArgumentException("Fields cannot be null");
    }
  }

  public static ClientResponse from(Client client) {
    return new ClientResponse(
        client.getId(),
        client.getEmail(),
        client.getUsername(),
        client.getCreatedAt()
    );
  }
}
