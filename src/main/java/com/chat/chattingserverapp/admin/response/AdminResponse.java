package com.chat.chattingserverapp.admin.response;

import com.chat.chattingserverapp.admin.domain.Admin;
import java.time.LocalDateTime;
import java.util.UUID;

public record AdminResponse(
    UUID id,
    String email,
    String username,
    LocalDateTime createdAt
) {

  public static AdminResponse from(Admin admin) {
    return new AdminResponse(
        admin.getId(),
        admin.getEmail(),
        admin.getUsername(),
        admin.getCreatedAt()
    );
  }
}
