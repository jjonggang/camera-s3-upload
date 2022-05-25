package com.example.aicctvstream.webSocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> clients = new ConcurrentHashMap<String, WebSocketSession>();

    private static ArrayList<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();
    // 사용자가 웹소켓 서버에 붙게 되면 동작하는 메소드.
    // WebSocketSession 값이 생성되는데, 해당 값을 static 변수인 clients 객체에
    // put 메서드를 활용하여 고유 아이디값을 키로, 세션을 값으로 넣어준다.
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        sessionList.add(session);
        clients.put(session.getId(), session);
        log.info(session.getId()+"연결");
    }


    // 접속이 끊어진 사용자가 발생하면 호출되는 메서드.
    // clients 객체에서 접속이 끊어진 아이디 값을 제거하도록 한다.
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionList.remove(session);
        clients.remove(session.getId());
        log.info(session.getId()+"끊김");
    }

    // 사용자의 메시지를 받게되면 동작하는 메서드
    // clients 객체에 메서드에서 접속한 사용자를 담아주었으므로, 해당 변수에는 접속한 세션의 값들이 모두 보관되고 있다.
    // handleTextMessage 메서드에서 clients 객체에 담긴 세션값들을 가져와서 반복문으로 메시지를 발송하면,
    // 자신 이외의 사용자에게 메시지를 보내게 된다.
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String id = session.getId();  //메시지를 보낸 아이디
        clients.entrySet().forEach( arg->{
            if(!arg.getKey().equals(id)) {  //같은 아이디가 아니면 메시지를 전달합니다.
                try {
                    arg.getValue().sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendMessageToAll(TextMessage message) {

        clients.entrySet().forEach( arg->{
            try {
                arg.getValue().sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
