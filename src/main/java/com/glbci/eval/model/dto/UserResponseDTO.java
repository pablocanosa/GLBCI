package com.glbci.eval.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class UserResponseDTO {

    private String id;
    private String token;
    private Boolean isActive;
}
