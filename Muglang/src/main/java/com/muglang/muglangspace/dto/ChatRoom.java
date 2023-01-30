package com.muglang.muglangspace.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class ChatRoom {
    private String id;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChatRoom create(@NonNull String name, String id) {
        ChatRoom created = new ChatRoom();
        created.id = id;
        created.name = name;
        //1. chatroom table에 insert
        return created;
    }

}