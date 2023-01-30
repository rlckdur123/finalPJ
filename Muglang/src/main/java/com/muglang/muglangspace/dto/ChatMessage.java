package com.muglang.muglangspace.dto;

import java.util.List;

import com.muglang.muglangspace.common.CamelHashMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String chatRoomId;
    private String writer;
    private String message;
    private MessageType type;
    private List<CamelHashMap> messageList;
    private int userId;
    private int loginUserId;
    private String chatTime;
    private String roomType;
    private String profile;
    
}
