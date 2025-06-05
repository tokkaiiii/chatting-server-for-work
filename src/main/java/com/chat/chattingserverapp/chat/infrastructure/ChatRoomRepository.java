package com.chat.chattingserverapp.chat.infrastructure;

import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.client.domain.Client;
import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {

  Optional<ChatRoom> save(ChatRoom chatRoom);

  Optional<ChatRoom> findById(Long id);

  List<ChatRoom> findByClient(Client client);

  List<ChatRoom> findAll();

  void delete(ChatRoom chatRoom);

  Optional<ChatRoom> findByChatRoomName(String chatRoomName);
}
