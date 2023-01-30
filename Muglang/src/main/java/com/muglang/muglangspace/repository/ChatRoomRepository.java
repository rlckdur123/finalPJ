package com.muglang.muglangspace.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import com.muglang.muglangspace.dto.ChatRoom;

import lombok.Getter;

@Repository
public class ChatRoomRepository {
    private final Map<String, ChatRoom> chatRoomMap;
    @Getter
    private final Collection<ChatRoom> chatRooms;

    public ChatRoomRepository() {
        chatRoomMap = Collections.unmodifiableMap(
                Stream.of(ChatRoom.create("서울", "1"), 
                		  ChatRoom.create("인천", "2"), 
                		  ChatRoom.create("부산", "3"), 
                		  ChatRoom.create("대전", "4"), 
                		  ChatRoom.create("대구", "5"), 
                		  ChatRoom.create("광주", "6"), 
                		  ChatRoom.create("울산", "7"),
                		  ChatRoom.create("제주", "8"))
                      .collect(Collectors.toMap(ChatRoom::getId, Function.identity())));
        
        chatRooms = Collections.synchronizedCollection(chatRoomMap.values());
    }

    public ChatRoom getChatRoom(String id) {
        return chatRoomMap.get(id);
    }
    
}
