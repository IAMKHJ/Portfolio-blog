package com.portfolio.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portfolio.blog.dto.post.PostUpdateDto;
import com.portfolio.blog.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.jsoup.Jsoup;


import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@DynamicUpdate
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "post_title", nullable = false)
    private String title;

    @Column(name = "post_content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "post_convert_content", nullable = false, columnDefinition = "LONGTEXT")
    private String convertContent;

    @Column(name = "post_hit", nullable = false)
    @ColumnDefault("0")
    private int hit;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();

    private String category;

    public void update(PostUpdateDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.category = dto.getCategory();
        this.convertContent = Jsoup.parse(dto.getContent()).text();
    }

}
