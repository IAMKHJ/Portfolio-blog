package com.portfolio.blog.repository.chat;

import com.portfolio.blog.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByPostId(Long id);

    void deleteByPostId(Long id);
}
