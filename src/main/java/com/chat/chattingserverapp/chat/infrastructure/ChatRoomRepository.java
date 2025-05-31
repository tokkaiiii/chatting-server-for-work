package com.chat.chattingserverapp.chat.infrastructure;

import com.chat.chattingserverapp.chat.domain.ChatRoom;
import java.util.Optional;

public interface ChatRoomRepository {

  Optional<ChatRoom> save(ChatRoom chatRoom);

}
