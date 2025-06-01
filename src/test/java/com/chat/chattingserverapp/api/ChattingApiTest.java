package com.chat.chattingserverapp.api;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.chat.chattingserverapp.ChattingServerAppApplication;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

@Target(TYPE)
@Retention(RUNTIME)
@SpringBootTest(
    classes = {ChattingServerAppApplication.class, TestFixtureConfig.class},
    webEnvironment = RANDOM_PORT
)
public @interface ChattingApiTest {

}
