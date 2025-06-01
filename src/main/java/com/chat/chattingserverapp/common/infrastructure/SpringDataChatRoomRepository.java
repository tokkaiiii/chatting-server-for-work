package com.chat.chattingserverapp.common.infrastructure;

import com.chat.chattingserverapp.chat.domain.ChatRoom;
import com.chat.chattingserverapp.client.domain.Client;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  List<ChatRoom> findByClient(Client client);
}
