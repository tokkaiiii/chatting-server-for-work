package com.chat.chattingserverapp.chat.infrastructure;

import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.client.domain.Client;

public interface MessageRepository {

  void send(Client sender, ChatRoom chatRoom,String message);

}
