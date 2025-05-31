package com.chat.chattingserverapp.chat.infrastructure;

import static java.util.Optional.of;

import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.common.infrastructure.SpringDataChatRoomRepository;
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
}
