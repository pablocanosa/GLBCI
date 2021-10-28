package com.glbci.eval.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private List<PhoneDTO> phones;
}
