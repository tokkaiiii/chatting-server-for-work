package com.chat.chattingserverapp.chat.response;


import com.chat.chattingserverapp.admin.domain.Admin;
import java.time.LocalDateTime;
import java.util.UUID;

public record AdminResponse(
    UUID id,
    String username,
    String email,
    LocalDateTime createdAt
) {
    public static AdminResponse from(Admin admin) {
        return new AdminResponse(
            admin.getId(),
            admin.getUsername(),
            admin.getEmail(),
            admin.getCreatedAt()
        );
    }
} 