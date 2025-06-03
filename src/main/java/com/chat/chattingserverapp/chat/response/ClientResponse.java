package com.chat.chattingserverapp.chat.response;


import com.chat.chattingserverapp.client.domain.Client;
import java.time.LocalDateTime;
import java.util.UUID;

public record ClientResponse(
    UUID id,
    String username,
    String email,
    LocalDateTime createdAt
) {
    public static ClientResponse from(Client client) {
        return new ClientResponse(
            client.getId(),
            client.getUsername(),
            client.getEmail(),
            client.getCreatedAt()
        );
    }
} 