package com.trongtin.blog.identity.dto.request.email;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendEmailRequest {
    Sender sender;  // Thêm người gửi
    List<To> to;  // Thay đổi từ Recipient sang List<To>
    String subject;
    String htmlContent;
}
