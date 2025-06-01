package com.chat.chattingserverapp.common.infrastructure;

import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.chat.domain.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMessageRepository extends JpaRepository<Message, Long> {

  List<Message> findByChatRoom(ChatRoom chatRoom);
}
