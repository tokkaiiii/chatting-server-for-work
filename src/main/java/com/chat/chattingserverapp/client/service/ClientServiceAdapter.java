package com.chat.chattingserverapp.client.service;

import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.infrastructure.ClientRepository;
import com.chat.chattingserverapp.client.response.ClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceAdapter implements ClientService{
  private final ClientRepository clientRepository;

  @Override
  public ClientResponse register(CreateClientCommand command) {
    return clientRepository.save(command.toClient())
        .map(client -> new ClientResponse(client.getUsername()))
        .orElseThrow(() -> new RuntimeException("Failed to register client"));
  }
}
