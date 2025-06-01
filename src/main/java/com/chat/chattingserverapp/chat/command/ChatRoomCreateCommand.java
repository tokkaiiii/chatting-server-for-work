package com.chat.chattingserverapp.chat.command;

import static com.chat.chattingserverapp.chat.domain.ChatRoom.of;

import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.client.domain.Client;

public record ChatRoomCreateCommand(String roomName) {

  public ChatRoom toChatRoom(Client client) {
    return of(roomName, client);
  }
}
