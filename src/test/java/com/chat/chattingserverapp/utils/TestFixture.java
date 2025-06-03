package com.chat.chattingserverapp.utils;

import com.chat.chattingserverapp.chat.command.ChatRoomCreateCommand;
import com.chat.chattingserverapp.chat.response.ChatRoomResponse;
import com.chat.chattingserverapp.client.command.CreateClientCommand;
import com.chat.chattingserverapp.client.query.IssueClientToken;
import com.chat.chattingserverapp.client.result.AccessTokenCarrier;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

public record TestFixture(TestRestTemplate client) {

  public static TestFixture create(Environment environment) {
    var client = new TestRestTemplate();
    var uriTemplateHandler = new LocalHostUriTemplateHandler(environment);
    client.setUriTemplateHandler(uriTemplateHandler);
    return new TestFixture(client);
  }

  public void createClient(String email, String username, String password) {
    client.postForEntity("/client/signup", new CreateClientCommand(email, username, password),
        Void.class);
  }

  public void createChatRoom(String username) {
    client.postForEntity(
        "/chat/rooms",
        new ChatRoomCreateCommand(username + "'s Room"),
        ChatRoomResponse.class
    );
  }

  public String issueClientToken(String email, String password) {
    AccessTokenCarrier carrier = client.postForObject(
        "/client/issueToken",
        new IssueClientToken(email, password),
        AccessTokenCarrier.class
    );

    return carrier.accessToken();
  }

  public void setClientAsDefaultUser(String email, String password) {
    String token = issueClientToken(email, password);
    RestTemplate template = client.getRestTemplate();
    template.getInterceptors().add((request, body, execution) -> {
      if (request.getHeaders().containsKey("Authorization") == false) {
        request.getHeaders().setBearerAuth(token);
      }

      return execution.execute(request, body);
    });

  }
}
