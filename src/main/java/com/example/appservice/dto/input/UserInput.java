package com.example.appservice.dto.input;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInput {
    private String username;
    private String email;
    private String phone;
}
