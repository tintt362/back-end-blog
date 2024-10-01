package com.trongtin.blog.identity.dto.request;

import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {

    private String comment;
}
