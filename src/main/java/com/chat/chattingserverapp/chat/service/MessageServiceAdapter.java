package com.chat.chattingserverapp.chat.service;

import com.chat.chattingserverapp.chat.command.MessageCommand;
import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.chat.domain.Message;
import com.chat.chattingserverapp.chat.infrastructure.ChatRoomRepository;
import com.chat.chattingserverapp.chat.infrastructure.MessageRepository;
import com.chat.chattingserverapp.client.domain.Client;
import com.chat.chattingserverapp.client.infrastructure.ClientRepository;
import com.chat.chattingserverapp.admin.domain.Admin;
import com.chat.chattingserverapp.admin.service.AdminService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceAdapter implements MessageService {

  private final ChatRoomRepository chatRoomRepository;
  private final MessageRepository messageRepository;
  private final ClientRepository clientRepository;
  private final AdminService adminService;
  private final SimpMessagingTemplate messagingTemplate;

  @Override
  public void send(UUID senderId, Long chatRoomId, String message) {
    ChatRoom chatRoom = getChatRoomFrom(chatRoomId);
    
    // 클라이언트인 경우
    Optional<Client> clientOpt = clientRepository.findById(senderId);
    if (clientOpt.isPresent()) {
      Client client = clientOpt.get();
      Message savedMessage = messageRepository.send(client, chatRoom, message);
      messagingTemplate.convertAndSend("/topic/chat.room." + chatRoomId, 
          new MessageCommand(
            savedMessage.getId(),
            chatRoomId,
            client.getUsername(),
            message,
            "CHAT",
            savedMessage.getCreatedAt(),
            false
          ));
    } 
    // 어드민인 경우
    else if (adminService.findById(senderId).isPresent()) {
      Admin admin = adminService.getDefaultAdmin();
      Message savedMessage = messageRepository.send(admin, chatRoom, message);
      messagingTemplate.convertAndSend("/topic/chat.room." + chatRoomId, 
          new MessageCommand(
            savedMessage.getId(),
            chatRoomId,
            admin.getUsername(),
            message,
            "CHAT",
            savedMessage.getCreatedAt(),
            true
          ));
    }
  }

  @Override
  public List<MessageCommand> getMessages(Long chatRoomId) {
    ChatRoom chatRoom = getChatRoomFrom(chatRoomId);
    return messageRepository.findByChatRoom(chatRoom).stream()
        .map(message -> new MessageCommand(
            message.getId(),
            chatRoomId,
            message.getSenderName(),
            message.getMessage(),
            "CHAT",
            message.getCreatedAt(),
            message.getAdminSender() != null
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
