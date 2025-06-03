package com.chat.chattingserverapp.chat.response;

import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.client.response.ClientResponse;
import com.chat.chattingserverapp.admin.response.AdminResponse;

import java.time.LocalDateTime;

public record ChatRoomResponse(
    Long chatRoomId,
    String roomName,
    ClientResponse client,
    AdminResponse admin,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

  public static ChatRoomResponse from(ChatRoom chatRoom) {
    return new ChatRoomResponse(
        chatRoom.getId(),
        chatRoom.getRoomName(),
        ClientResponse.from(chatRoom.getClient()),
        AdminResponse.from(chatRoom.getAdmin()),
        chatRoom.getCreatedAt(),
        chatRoom.getUpdatedAt()
    );
  }
}
