package com.chat.chattingserverapp.chat.service;

import com.chat.chattingserverapp.chat.command.ChatRoomCreateCommand;
import com.chat.chattingserverapp.chat.infrastructure.ChatRoomRepository;
import com.chat.chattingserverapp.chat.response.ChatRoomResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceAdapter implements ChatRoomService{
  private final ChatRoomRepository chatRoomRepository;

  @Override
  public Optional<ChatRoomResponse> createChatRoom(ChatRoomCreateCommand command) {
    return chatRoomRepository.save(command.toChatRoom())
        .map(ChatRoomResponse::from);
  }
}
