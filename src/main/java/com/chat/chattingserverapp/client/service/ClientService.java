package com.chat.chattingserverapp.client.service;

import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.command.LoginClientCommand;
import com.chat.chattingserverapp.client.query.IssueClientToken;
import com.chat.chattingserverapp.client.response.ClientResponse;
import java.util.Optional;
import java.util.UUID;

public interface ClientService {

  Optional<ClientResponse> register(CreateClientCommand command);

  Optional<ClientResponse> login(LoginClientCommand command);

  Optional<ClientResponse> findById(UUID id);

  Optional<ClientResponse> findByEmail(IssueClientToken query);
}
