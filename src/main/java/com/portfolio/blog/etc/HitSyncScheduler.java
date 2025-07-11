package com.portfolio.blog.etc;

import com.portfolio.blog.repository.post.PostRepository;
import com.portfolio.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HitSyncScheduler {

    private final PostRepository postRepository;
    private final PostService postService;

   /* @Scheduled(fixedDelay = 60000) // 1분마다 동기화
    public void syncHits() {
        postRepository.findAll().forEach(post -> {
            postService.flushHitsToDB(post.getId());
        });
    }*/

}
