package com.chat.chattingserverapp.common.infrastructure;

import com.chat.chattingserverapp.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMessageRepository extends JpaRepository<Message, Long> {

}
