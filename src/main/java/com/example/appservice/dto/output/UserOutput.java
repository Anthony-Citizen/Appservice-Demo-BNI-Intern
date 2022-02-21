package com.example.appservice.dto.output;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserOutput {
    public Long id;
    public String username;
    public String email;
    public String phone;
}
