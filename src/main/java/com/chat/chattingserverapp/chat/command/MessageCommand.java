package com.chat.chattingserverapp.chat.command;

public record MessageCommand(
    Long chatRoomId,
    String sender,
    String message
) {
}
