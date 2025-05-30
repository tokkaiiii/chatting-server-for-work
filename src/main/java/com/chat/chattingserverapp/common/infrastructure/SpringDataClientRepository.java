package com.chat.chattingserverapp.common.infrastructure;

import com.chat.chattingserverapp.client.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataClientRepository extends JpaRepository<Client, Long> {

}
