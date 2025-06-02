package com.chat.chattingserverapp.chat.command;

import java.time.LocalDateTime;

public record MessageCommand(
    Long roomId,
    String sender,
    String content,
    String type,
    LocalDateTime timestamp
) {
    public MessageCommand {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
