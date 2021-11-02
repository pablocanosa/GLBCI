package com.glbci.eval.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetUserResponseDTO extends UserResponseDTO{
    private String name;
    private String email;
    private String password;
    private List<PhoneDTO> phones;
    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime lastLogin;
}
