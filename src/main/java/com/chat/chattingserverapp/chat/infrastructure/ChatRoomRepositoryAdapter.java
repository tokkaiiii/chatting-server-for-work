package com.chat.chattingserverapp.chat.infrastructure;

import static java.util.Optional.of;

import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.client.domain.Client;
import com.chat.chattingserverapp.common.infrastructure.SpringDataChatRoomRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryAdapter implements ChatRoomRepository{
  private final SpringDataChatRoomRepository jpaChatRoomRepository;

  @Override
  public Optional<ChatRoom> save(ChatRoom chatRoom) {
    return of(jpaChatRoomRepository.save(chatRoom));
  }

  @Override
  public Optional<ChatRoom> findById(Long id) {
    return jpaChatRoomRepository.findById(id);
  }

  @Override
  public List<ChatRoom> findByClient(Client client) {
    return jpaChatRoomRepository.findByClient(client);
  }
}
