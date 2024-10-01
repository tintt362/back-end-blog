package com.trongtin.blog.identity.dto.request;

import com.trongtin.blog.identity.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 2, max = 50, message = "USERNAME_INVALID")
    String username;
    @Size(min = 3, message = "INVALID_PASSWORD")
    String password;
    String email;
    String firstName;
    String lastName;
    @DobConstraint(min = 2, message = "INVALID_DOB")
    LocalDate dob;
}
