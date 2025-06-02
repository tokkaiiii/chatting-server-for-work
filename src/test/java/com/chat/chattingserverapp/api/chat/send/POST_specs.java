package com.chat.chattingserverapp.api.chat.send;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.chat.chattingserverapp.api.ChattingApiTest;
import com.chat.chattingserverapp.chat.command.MessageCommand;
import com.chat.chattingserverapp.chat.controller.MessageController;
import com.chat.chattingserverapp.chat.service.MessageService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;


@ChattingApiTest
@ExtendWith(MockitoExtension.class)
@DisplayName("POST /app/send")
public class POST_specs {

  @InjectMocks
  private MessageController messageController;

  @Mock
  private MessageService messageService;


  @Mock
  private JwtAuthenticationToken jwtAuthenticationToken;

  private final UUID id = UUID.randomUUID();

  @Test
  void 올바르게_요청하면_200_ok() {
  }
}