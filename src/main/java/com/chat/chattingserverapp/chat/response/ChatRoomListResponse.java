package com.chat.chattingserverapp.chat.response;

import java.time.LocalDateTime;

public record ChatRoomListResponse(
    Long chatRoomId,
    String roomName,
    String lastMessage,
    LocalDateTime lastMessageDateTime,
    Integer unreadMessageCount
) {
}
