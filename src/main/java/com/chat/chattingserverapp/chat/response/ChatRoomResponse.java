package com.chat.chattingserverapp.chat.response;

import com.chat.chattingserverapp.chat.domain.ChatRoom;

public record ChatRoomResponse(
    String roomName
) {

  public static ChatRoomResponse from(ChatRoom chatRoom) {
    return new ChatRoomResponse(chatRoom.getRoomName());
  }
}
