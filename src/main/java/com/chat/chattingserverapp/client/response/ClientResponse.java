package com.chat.chattingserverapp.client.response;

import java.time.LocalDateTime;

public record ClientResponse(
    Long id,
    String username,
    LocalDateTime createdAt
) {
}
