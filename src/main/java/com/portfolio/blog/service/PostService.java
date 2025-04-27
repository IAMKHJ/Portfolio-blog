package com.portfolio.blog.service;

import com.portfolio.blog.dto.MessageDto;
import com.portfolio.blog.dto.post.*;
import com.portfolio.blog.entity.Member;
import com.portfolio.blog.entity.Post;
import com.portfolio.blog.entity.common.Status;
import com.portfolio.blog.repository.member.MemberRepository;
import com.portfolio.blog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;
    private final ChatService chatService;

    @Transactional
    public MessageDto<?> save(PostSaveDto dto) throws IOException {

        Optional<Member> member = memberRepository.findById(dto.getMemberId());

        if(member.isPresent()){ // 글쓴이 정보가 있으면
            Member memberEntity = member.get();

            Post newPost = Post.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .member(memberEntity)
                    .category(dto.getCategory())
                    .convertContent(Jsoup.parse(dto.getContent()).text())
                    .status(Status.TRUE)
                    .build();

            Post post = postRepository.save(newPost);
            fileService.saveWithPost(dto.getFile(), post);
            chatService.createRoom(newPost.getId()); // 채팅방 생성
            return new MessageDto<>("ok", newPost.getCategory());
        }else{ // 글쓴이가 삭제된 상태
            return new MessageDto<>("no", dto.getCategory());
        }
    }

    @Transactional
    public MessageDto<?> update(Long id, PostUpdateDto dto) throws IOException {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 글을 찾을 수 없습니다."));

        post.update(dto);
        
        if(!post.getFiles().isEmpty()){//DB에 파일정보가 있으면
            if(dto.getFile()!=null && !dto.getFile().isEmpty()){//업로드하는파일 있으면
                boolean result = fileService.delete(post.getFiles().get(0)); //기존 업로드되어있던 실제파일 삭제성공 여부
    
                if(result){//삭제성공시 새로운 받은 파일 업로드
                    fileService.saveWithPost(dto.getFile(), post);
                    return new MessageDto<>("ok", post.getId());
                }else{
                    return new MessageDto<>("false");
                }
            }
        }else{
            fileService.saveWithPost(dto.getFile(), post);
            return new MessageDto<>("ok", post.getId());
        }

        return new MessageDto<>("ok", post.getId());
    }

    @Transactional(readOnly = true)
    public Slice<PostListDto> findAllSlice(Pageable pageable) {
        Slice<Post> posts = postRepository.findAll(pageable);

        //status가 TRUE인 것만 PostListDto로 변환
        return posts.stream()
                .filter(post -> post.getStatus() == Status.TRUE) // status가 TRUE인 것만
                .map(PostListDto::new) // PostListDto로 변환
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        list -> new SliceImpl<>(list, pageable, posts.hasNext())));
    }

    @Transactional(readOnly = true)
    public Page<PostListDto> search(String category, String keyword, Pageable pageable) {
        int page = pageable.getPageNumber() - 1; // page 위치에 있는 값은 0부터 시작
        int pageLimit = pageable.getPageSize(); // 한페이지에 보여줄 글 개수
        Page<Post> posts = postRepository.postListSearch(category, keyword, PageRequest.of(page, pageLimit));
        return posts
                .map(PostListDto::new);
    }

    @Transactional(readOnly = true)
    public PostDetailDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 글을 찾을 수 없습니다."));

        return PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .hit(post.getHit())
                .createdDate(post.getCreatedDate())
                .lastModifiedDate(post.getLastModifiedDate())
                .member(post.getMember())
                .file(post.getFiles().isEmpty()?null:post.getFiles().get(0))
                .category(post.getCategory())
                .build();
    }

    @Transactional
    public MessageDto<?> delete(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent()){
            if(!post.get().getFiles().isEmpty()){
                fileService.delete(post.get().getFiles().get(0));
            }
        }
        postRepository.deleteById(id);
        chatService.deleteByPostId(id);
        return new MessageDto<>("ok");
    }

    @Transactional
    public void updateHits(Long id){
        postRepository.updateHits(id);
    }

    @Transactional(readOnly = true)
    public Page<AdminPostListDto> adminPostListSearch(String searchCnd, String keyword, Pageable pageable) {
        int page = pageable.getPageNumber() - 1; // page 위치에 있는 값은 0부터 시작
        int pageLimit = pageable.getPageSize(); // 한페이지에 보여줄 글 개수

        Page<Post> posts = postRepository.adminPostListSearch(searchCnd, keyword, PageRequest.of(page, pageLimit));
        return posts
                .map(AdminPostListDto::new);
    }

    @Transactional
    public void postStatusUpdate(PostStatusUpdateDto dto){
        Post post = postRepository.findById(dto.getId())
                .orElseThrow(()-> new IllegalArgumentException("해당 글을 찾을 수 없습니다."));

        post.postStatusUpdate(dto.getStatus());
    }

}
