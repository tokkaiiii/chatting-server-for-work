package com.chat.chattingserverapp.chat.infrastructure;

import static com.chat.chattingserverapp.chat.domain.Message.*;

import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.chat.domain.Message;
import com.chat.chattingserverapp.client.domain.Client;
import com.chat.chattingserverapp.admin.domain.Admin;
import com.chat.chattingserverapp.common.infrastructure.SpringDataMessageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryAdapter implements MessageRepository {

  private final SpringDataMessageRepository jpaMessageRepository;

  @Override
  public Message send(Client sender, ChatRoom chatRoom, String message) {
    return jpaMessageRepository.save(Message.of(message, sender, chatRoom));
  }

  @Override
  public Message send(Admin sender, ChatRoom chatRoom, String message) {
    return jpaMessageRepository.save(Message.of(message, sender, chatRoom));
  }

  @Override
  public List<Message> findByChatRoom(ChatRoom chatRoom) {
    return jpaMessageRepository.findByChatRoom(chatRoom);
  }
}
