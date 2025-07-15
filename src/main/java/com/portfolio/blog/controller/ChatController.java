package com.portfolio.blog.controller;

import com.portfolio.blog.dto.chat.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ConcurrentHashMap<String, Set<String>> participants = new ConcurrentHashMap<>();

    @MessageMapping("/chat")
    public void chat(ChatMessageDto dto) {

        String roomId = dto.getRoomId();
        String sender = dto.getSender();

        if(ChatMessageDto.MessageType.ENTER.equals(dto.getType())){
            dto.setMessage("[알림] " + sender + "님이 입장하였습니다.");
        }else if(ChatMessageDto.MessageType.TALK.equals(dto.getType())){
            dto.setMessage(sender + " : " + dto.getMessage());
        }else if(ChatMessageDto.MessageType.LEAVE.equals(dto.getType())){
            dto.setMessage("[알림] " + sender + "님이 퇴장하였습니다.");
        }

        // 채팅 참여자 수 관리 추가
        if (ChatMessageDto.MessageType.ENTER.equals(dto.getType())) { // 참여
            // roomId에 해당하는 Set이 없으면 새로 만들고, 있으면 그걸 가져옴
            participants.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(sender);
        } else if (ChatMessageDto.MessageType.LEAVE.equals(dto.getType())) { // 퇴장
            // 현재 방 참여자 목록 가져옴
            Set<String> roomParticipants = participants.get(roomId);

            if (roomParticipants != null) {
                roomParticipants.remove(sender); // 퇴장한 사용자를 목록에서 제거
                if (roomParticipants.isEmpty()) { // 방에 남은 사람이 없으면
                    participants.remove(roomId); // 방 자체를 participants 맵에서 제거
                }
            }
        }

        messagingTemplate.convertAndSend("/sub/chat/" + roomId, dto);

        int count = participants.getOrDefault(roomId, Collections.emptySet()).size(); // 참여자 수 전송 추가
        messagingTemplate.convertAndSend("/sub/chat/" + roomId + "/participants", count);

    }

}
