package com.chat.chattingserverapp.chat.command;

import java.time.LocalDateTime;

public record MessageCommand(
    Long id,
    Long roomId,
    String sender,
    String content,
    String type,
    LocalDateTime createdAt,
    boolean isAdmin
) {
    public MessageCommand {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
