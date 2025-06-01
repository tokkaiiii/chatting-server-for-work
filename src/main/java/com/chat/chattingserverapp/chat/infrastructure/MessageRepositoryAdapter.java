package com.chat.chattingserverapp.chat.infrastructure;

import static com.chat.chattingserverapp.chat.domain.Message.*;

import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.chat.domain.Message;
import com.chat.chattingserverapp.client.domain.Client;
import com.chat.chattingserverapp.common.infrastructure.SpringDataMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryAdapter implements MessageRepository {

  private final SpringDataMessageRepository jpaMessageRepository;

  @Override
  public void send(Client sender, ChatRoom chatRoom, String message) {
    jpaMessageRepository.save(of(message, sender, chatRoom));
  }
}
