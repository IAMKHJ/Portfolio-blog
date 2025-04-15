package com.portfolio.blog.dto.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomDto {

    private String roomId;
    private long userCount;

}
