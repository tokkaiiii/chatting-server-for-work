package com.chat.chattingserverapp.chat.infrastructure;

import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.chat.domain.Message;
import com.chat.chattingserverapp.client.domain.Client;
import com.chat.chattingserverapp.admin.domain.Admin;
import java.util.List;

public interface MessageRepository {

  Message send(Client sender, ChatRoom chatRoom, String message);

  Message send(Admin sender, ChatRoom chatRoom, String message);

  List<Message> findByChatRoom(ChatRoom chatRoom);
}
