package com.chat.chattingserverapp.chat.command;

import static com.chat.chattingserverapp.chat.domain.ChatRoom.of;

import com.chat.chattingserverapp.chat.domain.ChatRoom;

public record ChatRoomCreateCommand(String roomName) {

  public ChatRoom toChatRoom() {
    return of(roomName);
  }
}
