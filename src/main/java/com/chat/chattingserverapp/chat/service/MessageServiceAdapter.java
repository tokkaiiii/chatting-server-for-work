package com.chat.chattingserverapp.chat.service;

import com.chat.chattingserverapp.chat.command.MessageCommand;
import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.chat.domain.Message;
import com.chat.chattingserverapp.chat.infrastructure.ChatRoomRepository;
import com.chat.chattingserverapp.chat.infrastructure.MessageRepository;
import com.chat.chattingserverapp.client.domain.Client;
import com.chat.chattingserverapp.client.infrastructure.ClientRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceAdapter implements MessageService {

  private final ChatRoomRepository chatRoomRepository;
  private final MessageRepository messageRepository;
  private final ClientRepository clientRepository;

  @Override
  public void send(UUID clientId, Long chatRoomId, String message) {
    messageRepository.send(getClientFrom(clientId), getChatRoomFrom(chatRoomId), message);
  }

  @Override
  public List<MessageCommand> getMessages(Long chatRoomId) {
    ChatRoom chatRoom = getChatRoomFrom(chatRoomId);
    return messageRepository.findByChatRoom(chatRoom).stream()
        .map(message -> new MessageCommand(
            chatRoomId,
            message.getSender().getUsername(),
            message.getMessage(),
            "CHAT",
            message.getCreatedAt()
        ))
        .collect(Collectors.toList());
  }

  private ChatRoom getChatRoomFrom(Long chatRoomId) {
    return chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new IllegalArgumentException("Chat room not found: " + chatRoomId));
  }

  private Client getClientFrom(UUID clientId) {
    return clientRepository.findById(clientId)
        .orElseThrow(() -> new IllegalArgumentException("Client not found: " + clientId));
  }
}
