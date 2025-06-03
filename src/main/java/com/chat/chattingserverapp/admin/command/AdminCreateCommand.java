package com.chat.chattingserverapp.admin.command;

import com.chat.chattingserverapp.admin.domain.Admin;
import java.util.UUID;

public record AdminCreateCommand(
    UUID id,
    String email,
    String password,
    String username
) {
    public Admin toAdmin(String hashedPassword) {
        return Admin.of(id, email, username, hashedPassword);
    }
} 