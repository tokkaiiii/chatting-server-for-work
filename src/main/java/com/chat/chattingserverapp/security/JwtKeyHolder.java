package com.chat.chattingserverapp.security;

import javax.crypto.SecretKey;

public record JwtKeyHolder(SecretKey key) {

}
