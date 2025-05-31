package com.chat.chattingserverapp.client.controller;

import com.chat.chattingserverapp.client.service.ClientService;
import com.chat.chattingserverapp.client.view.ClientMeView;
import java.security.Principal;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ClientMeController(
    ClientService clientService
) {

  @GetMapping("/client/me")
  public ClientMeView me(Principal principal) {
    UUID id = UUID.fromString(principal.getName());
   return clientService.findById(id)
        .map(clientResponse -> new ClientMeView(id, clientResponse.username()))
        .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + id));
  }

}
