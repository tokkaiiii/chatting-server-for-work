package com.chat.chattingserverapp.client.command;

public record LoginClientCommand(
    String username,
    String password
) {

}
