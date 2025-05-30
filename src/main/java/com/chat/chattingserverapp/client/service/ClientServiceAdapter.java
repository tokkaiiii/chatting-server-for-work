package com.chat.chattingserverapp.client.service;

import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.infrastructure.ClientRepository;
import com.chat.chattingserverapp.client.response.ClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceAdapter implements ClientService{
  private final ClientRepository clientRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public ClientResponse register(CreateClientCommand command) {
    String hashedPassword = passwordEncoder.encode(command.password());
    return clientRepository.save(command.toClient(hashedPassword))
        .map(client -> new ClientResponse(client.getUsername()))
        .orElseThrow(() -> new RuntimeException("Failed to register client"));
  }
}
