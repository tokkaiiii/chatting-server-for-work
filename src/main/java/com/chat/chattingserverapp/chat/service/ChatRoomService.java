package com.chat.chattingserverapp.chat.service;

import com.chat.chattingserverapp.chat.command.ChatRoomCreateCommand;
import com.chat.chattingserverapp.chat.response.ChatRoomResponse;
import java.util.Optional;

public interface ChatRoomService {

  Optional<ChatRoomResponse> createChatRoom(ChatRoomCreateCommand command);

}
