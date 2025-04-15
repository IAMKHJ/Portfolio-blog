package com.portfolio.blog.controller;

import com.portfolio.blog.dto.chat.ChatMessageDto;
import com.portfolio.blog.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChatService chatService;

    @MessageMapping("/chat")
    public void chat(ChatMessageDto dto) {
        if(ChatMessageDto.MessageType.ENTER.equals(dto.getType())){
            dto.setMessage("[알림] " + dto.getSender() + "님이 입장하였습니다.");
        }else if(ChatMessageDto.MessageType.TALK.equals(dto.getType())){
            dto.setMessage(dto.getSender() + " : " + dto.getMessage());
        }else if(ChatMessageDto.MessageType.LEAVE.equals(dto.getType())){
            dto.setMessage("[알림] " + dto.getSender() + "님이 퇴장하였습니다.");
        }
        sendingOperations.convertAndSend("/sub/chat/" + dto.getRoomId(), dto);
    }

}
