package com.trongtin.blog.identity.dto.response;

import jakarta.persistence.Lob;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    String title;
    String content;
    Instant createdDate;
    Instant modifiedDate;
    Long likeCount;
    @Lob
    private byte[] image;
}
