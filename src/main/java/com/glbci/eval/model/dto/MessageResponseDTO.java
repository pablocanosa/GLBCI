package com.glbci.eval.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {

    private String message;
    private LocalDateTime date;
}
