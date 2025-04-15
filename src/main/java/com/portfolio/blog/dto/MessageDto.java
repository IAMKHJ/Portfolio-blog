package com.portfolio.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto<T> {
    private String key;
    private T data;

    public MessageDto(String key) {
        this.key = key;
    }
}
