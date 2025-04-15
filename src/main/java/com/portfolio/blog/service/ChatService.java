package com.portfolio.blog.service;

import com.portfolio.blog.entity.Chat;
import com.portfolio.blog.repository.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public void createRoom(Long id) {
        Chat newChat = Chat.builder()
                .postId(id)
                .name(UUID.randomUUID().toString())
                .build();
        chatRepository.save(newChat);
    }

    public Chat findById(Long id) {
        return chatRepository.findByPostId(id).orElse(null);
    }

    public void deleteByPostId(Long id){chatRepository.deleteByPostId(id);}

}
