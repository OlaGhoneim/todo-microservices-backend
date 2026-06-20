package org.example.userservice.model.request;

import lombok.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String email;
}
