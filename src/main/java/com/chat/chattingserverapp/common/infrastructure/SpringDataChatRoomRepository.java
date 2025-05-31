package com.chat.chattingserverapp.common.infrastructure;

import com.chat.chattingserverapp.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
