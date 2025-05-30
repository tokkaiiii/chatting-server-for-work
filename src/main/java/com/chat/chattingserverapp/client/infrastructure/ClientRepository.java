package com.chat.chattingserverapp.client.infrastructure;

import com.chat.chattingserverapp.client.domain.Client;
import java.util.Optional;

public interface ClientRepository {

  Optional<Client> save(Client client);

  Client findById(Long id);

}
